package com.neil.myth.common.constant;

/**
 * @author nihao
 * @date 2024/6/11
 */
public interface CommonConstant {

    String LINE_SEPARATOR = System.getProperty("line.separator");

    String DB_MYSQL = "mysql";

    String PATH_SUFFIX = "/myth";

    String DB_SUFFIX = "myth_";

    String RECOVER_REDIS_KEY_PRE = "myth:transaction:%s";

    String MYTH_TRANSACTION_CONTEXT = "MYTH_TRANSACTION_CONTEXT";

    String TOPIC_TAG_SEPARATOR = ",";

    int SUCCESS = 1;

    int ERROR = 0;
}
