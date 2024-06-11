package com.neil.myth.core.repository;

import com.alibaba.druid.pool.DruidDataSource;
import com.neil.myth.annotation.MythSPI;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.common.config.MythDbConfig;
import com.neil.myth.common.exception.MythException;
import com.neil.myth.common.serializer.Serializer;
import com.neil.myth.common.utils.RepositoryPathUtil;
import com.neil.myth.common.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


/**
 * @author nihao
 * @date 2024/6/11
 */
@MythSPI("db")
@Slf4j
public class JdbcRepository implements MythRepository {

    private DruidDataSource dataSource;

    private String tableName;

    private Serializer serializer;


    @Override
    public int create(MythTransaction mythTransaction) {
        return 0;
    }

    @Override
    public int remove(String transId) {
        return 0;
    }

    @Override
    public int update(MythTransaction mythTransaction) throws MythException {
        return 0;
    }

    @Override
    public void updateFailTransaction(MythTransaction mythTransaction) throws MythException {

    }

    @Override
    public void updateParticipant(MythTransaction mythTransaction) throws MythException {

    }

    @Override
    public int updateStatus(String transId, Integer status) throws MythException {
        return 0;
    }

    @Override
    public MythTransaction findByTransId(String transId) {
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

    @Override
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
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
}
