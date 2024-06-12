package com.neil.myth.common.serializer;

import com.neil.myth.annotation.MythSPI;
import com.neil.myth.common.exception.MythException;

/**
 * @author nihao
 * @date 2024/6/7
 */
@MythSPI
public interface Serializer {

    byte[] serialize(Object obj) throws MythException;

    <T> T deSerialize(byte[] param, Class<T> clazz) throws MythException;
}
