package com.neil.myth.common.enums;

import cn.hutool.core.util.StrUtil;

/**
 * @author nihao
 * @date 2024/6/11
 */
public enum DbTypeEnum {

    MYSQL;

    public static DbTypeEnum getByName(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        for (DbTypeEnum dbTypeEnum : DbTypeEnum.values()) {
            if (dbTypeEnum.name().equals(code)) {
                return dbTypeEnum;
            }
        }
        return null;
    }

}
