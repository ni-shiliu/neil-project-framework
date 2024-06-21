package com.neil.myth.core.service;

/**
 * @author nihao
 * @date 2024/6/12
 */
@FunctionalInterface
public interface MythMqReceiveService {

    Boolean processMessage(byte[] message) throws Exception;

}
