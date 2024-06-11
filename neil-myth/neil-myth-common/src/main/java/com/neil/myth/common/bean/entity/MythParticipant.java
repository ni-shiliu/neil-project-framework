package com.neil.myth.common.bean.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Data
public class MythParticipant implements Serializable {

    private static final long serialVersionUID = -210590704127687690L;

    private Class targetClass;

    private String methodName;

    private Class[] parameterTypes;

    private Object[] args;
}
