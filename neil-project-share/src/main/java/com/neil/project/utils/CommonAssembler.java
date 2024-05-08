package com.neil.project.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nihao
 * @date 2024/5/8
 */
public interface CommonAssembler {

    static <T, U> U toDto(T entity, Class<U> dtoClass) {
        return ObjectMapperUtil.getInstance().convertValue(entity, dtoClass);
    }

    static <T, U> U toEntity(T dto, Class<U> entityClass) {
        return ObjectMapperUtil.getInstance().convertValue(dto, entityClass);
    }

    static <T, U> List<U> toDto(List<T> entityList, Class<U> dtoClass) {
        TypeFactory typeFactory = ObjectMapperUtil.getInstance().getTypeFactory();
        JavaType returnType = typeFactory.constructParametricType(ArrayList.class, dtoClass);
        return ObjectMapperUtil.getInstance().convertValue(entityList, returnType);
    }

    static <T, U> List<U> toEntity(List<T> dtoList, Class<U> entityClass) {
        TypeFactory typeFactory = ObjectMapperUtil.getInstance().getTypeFactory();
        JavaType returnType = typeFactory.constructParametricType(ArrayList.class, entityClass);
        return ObjectMapperUtil.getInstance().convertValue(dtoList, returnType);
    }
}
