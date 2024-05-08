package com.neil.project.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neil.project.config.JacksonConfig;

/**
 * @author nihao
 * @date 2024/5/8
 */
public class ObjectMapperUtil {

    private static final ObjectMapper om;

    static {
        JacksonConfig jacksonConfig = new JacksonConfig();
        om = jacksonConfig.getObjectMapper();
    }

    private ObjectMapperUtil() {
        throw new AssertionError("Instantiating utility class.");
    }

    public static ObjectMapper getInstance() {
        return om;
    }
}
