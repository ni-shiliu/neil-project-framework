package com.neil.myth.common.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Getter
public enum DbTypeEnum {

    MYSQL("mysql");

    private String code;

    DbTypeEnum(String code) {
        this.code = code;
    }

    public static DbTypeEnum getByName(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        for (DbTypeEnum dbTypeEnum : DbTypeEnum.values()) {
            if (code.contains(dbTypeEnum.getCode())) {
                return dbTypeEnum;
            }
        }
        return null;
    }

}
