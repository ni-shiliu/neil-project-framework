package com.neil.myth.common.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MythInvocation implements Serializable {
    private static final long serialVersionUID = -5545865505664243565L;


    private Class targetClass;

    private String methodName;

    private Class[] parameterTypes;

    private Object[] args;

}


