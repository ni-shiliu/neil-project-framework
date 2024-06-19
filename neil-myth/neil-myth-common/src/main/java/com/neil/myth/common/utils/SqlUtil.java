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
                        .append("  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',\n" +
                                "  `trans_id` varchar(64) NOT NULL COMMENT '事务ID',\n" +
                                "  `role` varchar(64) NOT NULL COMMENT '角色',\n" +
                                "  `target_class` varchar(255) NULL,\n" +
                                "  `target_method` varchar(255) NULL,\n" +
                                "  `status` varchar(64) NOT NULL COMMENT '状态',\n" +
                                "  `participants` text NULL COMMENT '参与者',\n" +
                                "  `args` text NULL COMMENT '参数',\n" +
                                "  `error_msg` text NULL COMMENT '失败原因',\n" +
                                "  `gmt_created` datetime NOT NULL COMMENT '创建时间',\n" +
                                "  `gmt_modified` datetime NOT NULL COMMENT '更新时间',\n" +
                                "  PRIMARY KEY (`id`),\n" +
                                "  INDEX `uniq_trans_id`(`trans_id`),\n" +
                                "  INDEX `idx_gmt_created`(`gmt_created`),\n" +
                                "  INDEX `idx_gmt_modified`(`gmt_modified`)")
                        .append(")");
                break;
            default:
                throw new RuntimeException("dbType类型不支持,目前仅支持mysql.");
        }
        return createTableSql.toString();
    }
}
