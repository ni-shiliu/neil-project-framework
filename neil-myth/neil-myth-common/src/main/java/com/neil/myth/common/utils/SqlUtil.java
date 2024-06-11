package com.neil.myth.common.utils;

import com.neil.myth.common.enums.DbTypeEnum;
import com.neil.myth.common.exception.MythException;

import java.util.Objects;

/**
 * @author nihao
 * @date 2024/6/11
 */
public class SqlUtil {

    public static String buildCreateTableSql(final String driverClassName, final String tableName) {
        StringBuilder createTableSql = new StringBuilder();
        DbTypeEnum dbTypeEnum = DbTypeEnum.getByName(driverClassName);
        if (Objects.isNull(dbTypeEnum)) {
            throw new MythException("dbType类型不支持,目前仅支持mysql.");
        }
        switch (dbTypeEnum) {
            case MYSQL:
                createTableSql.append("CREATE TABLE IF NOT EXISTS `")
                        .append(tableName).append("` (\n")
                        .append("  `trans_id` varchar(64) NOT NULL,\n")
                        .append("  `target_class` varchar(256) ,\n")
                        .append("  `target_method` varchar(128) ,\n")
                        .append("  `retried_count` int(3) NOT NULL,\n")
                        .append("  `gmt_created` datetime NOT NULL,\n")
                        .append("  `gmt_modified` datetime NOT NULL,\n")
                        .append("  `status` varchar(64) NOT NULL,\n")
                        .append("  `invocation` longblob,\n")
                        .append("  `error_msg` text ,\n")
                        .append("   PRIMARY KEY (`trans_id`),\n")
                        .append("   KEY `status_gmt_modified` (`gmt_modified`,`status`) USING BTREE \n")
                        .append(")");
                break;
            default:
                throw new RuntimeException("dbType类型不支持,目前仅支持mysql.");
        }
        return createTableSql.toString();
    }
}
