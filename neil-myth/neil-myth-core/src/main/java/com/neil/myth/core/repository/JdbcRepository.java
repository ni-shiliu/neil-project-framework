package com.neil.myth.core.repository;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.neil.myth.annotation.MythSPI;
import com.neil.myth.common.bean.entity.MythParticipant;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.common.config.MythDbConfig;
import com.neil.myth.common.enums.MythRoleEnum;
import com.neil.myth.common.enums.MythStatusEnum;
import com.neil.myth.common.exception.MythException;
import com.neil.myth.common.utils.RepositoryPathUtil;
import com.neil.myth.common.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author nihao
 * @date 2024/6/11
 */
@MythSPI("db")
@Slf4j
public class JdbcRepository implements MythRepository {

    private DruidDataSource dataSource;

    private String tableName;

    @Override
    public int create(MythTransaction mythTransaction) {
        StringBuilder sql = new StringBuilder()
                .append("insert into ")
                .append(tableName)
                .append("(trans_id, role, target_class, target_method, status, retry_count, participants, args, error_msg, gmt_created, gmt_modified)")
                .append(" values(?,?,?,?,?,?,?,?,?,?,?)");
        return executeUpdate(sql.toString(),
                mythTransaction.getTransId(),
                mythTransaction.getRole().name(),
                mythTransaction.getTargetClass(),
                mythTransaction.getTargetMethod(),
                mythTransaction.getStatus().name(),
                mythTransaction.getRetryCount(),
                JSONUtil.toJsonStr(mythTransaction.getParticipants()),
                mythTransaction.getArgs(),
                mythTransaction.getErrorMsg(),
                mythTransaction.getGmtCreated(),
                mythTransaction.getGmtModified());
    }

    @Override
    public void updateFailTransaction(MythTransaction mythTransaction) throws MythException {
        String sql = "update " + tableName + " set status=?, error_msg=?, retry_count=?, gmt_modified = ? where trans_id = ?  ";
        mythTransaction.setGmtModified(LocalDateTime.now());
        executeUpdate(sql, mythTransaction.getStatus().name(),
                mythTransaction.getErrorMsg(),
                mythTransaction.getRetryCount(),
                mythTransaction.getGmtModified(),
                mythTransaction.getTransId());
    }

    @Override
    public void updateParticipant(MythTransaction mythTransaction) throws MythException {
        String sql = "update " + tableName + " set participants=?  where trans_id = ?  ";
        executeUpdate(sql, JSONUtil.toJsonStr(mythTransaction.getParticipants()), mythTransaction.getTransId());
    }

    @Override
    public int updateStatus(MythTransaction mythTransaction) throws MythException {
        String sql = "update " + tableName + " set status=?, retry_count=?, gmt_modified = ?  where trans_id = ?  ";
        return executeUpdate(sql, mythTransaction.getStatus().name(),
                mythTransaction.getRetryCount(),
                LocalDateTime.now(),
                mythTransaction.getTransId());
    }

    @Override
    public MythTransaction findByTransId(String transId) {
        String selectSql = "select * from " + tableName + " where trans_id=?";
        List<Map<String, Object>> list = executeQuery(selectSql, transId);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().filter(Objects::nonNull)
                    .map(this::buildByResultMap).collect(Collectors.toList()).get(0);
        }
        return null;
    }

    @Override
    public List<MythTransaction> listAllByDelay(Date date) {
        return null;
    }

    @Override
    public void init(String modelName, MythConfig mythConfig) throws MythException {
        dataSource = new DruidDataSource();
        final MythDbConfig tccDbConfig = mythConfig.getMythDbConfig();
        dataSource.setUrl(tccDbConfig.getUrl());
        dataSource.setDriverClassName(tccDbConfig.getDriverClassName());
        dataSource.setUsername(tccDbConfig.getUsername());
        dataSource.setPassword(tccDbConfig.getPassword());
        dataSource.setInitialSize(tccDbConfig.getInitialSize());
        dataSource.setMaxActive(tccDbConfig.getMaxActive());
        dataSource.setMinIdle(tccDbConfig.getMinIdle());
        dataSource.setMaxWait(tccDbConfig.getMaxWait());
        dataSource.setValidationQuery(tccDbConfig.getValidationQuery());
        dataSource.setTestOnBorrow(tccDbConfig.getTestOnBorrow());
        dataSource.setTestOnReturn(tccDbConfig.getTestOnReturn());
        dataSource.setTestWhileIdle(tccDbConfig.getTestWhileIdle());
        dataSource.setPoolPreparedStatements(tccDbConfig.getPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(tccDbConfig.getMaxPoolPreparedStatementPerConnectionSize());
        this.tableName = RepositoryPathUtil.buildDbTableName(modelName);
        executeUpdate(SqlUtil.buildCreateTableSql(tccDbConfig.getDriverClassName(), tableName));
    }

    private int executeUpdate(final String sql, final Object... params) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("executeUpdate-> " + e.getMessage());
        }
        return 0;
    }

    private List<Map<String, Object>> executeQuery(final String sql, final Object... params) {
        List<Map<String, Object>> list = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int columnCount = md.getColumnCount();
                list = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> rowData = new HashMap<>(16);
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.put(md.getColumnName(i), rs.getObject(i));
                    }
                    list.add(rowData);
                }
            }
        } catch (SQLException e) {
            log.error("executeQuery-> " + e.getMessage());
        }
        return list;
    }

    private MythTransaction buildByResultMap(final Map<String, Object> map) {
        MythTransaction mythTransaction = new MythTransaction();
        mythTransaction.setId(Long.valueOf(map.get("id").toString()));
        mythTransaction.setTransId((String) map.get("trans_id"));
        mythTransaction.setTargetClass(map.get("target_class").toString());
        mythTransaction.setTargetMethod(map.get("target_method").toString());
        mythTransaction.setGmtCreated(LocalDateTime.parse(map.get("gmt_created").toString()));
        mythTransaction.setGmtModified(LocalDateTime.parse(map.get("gmt_modified").toString()));
        mythTransaction.setStatus(MythStatusEnum.byCode(map.get("status").toString()));
        mythTransaction.setRole(MythRoleEnum.byCode(map.get("role").toString()));
        mythTransaction.setParticipants(JSONUtil.toBean(map.get("participants").toString(),
                new TypeReference<List<MythParticipant>>() {}, false));

        return mythTransaction;
    }
}
