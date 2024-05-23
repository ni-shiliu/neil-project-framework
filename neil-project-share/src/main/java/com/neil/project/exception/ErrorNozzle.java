package com.neil.project.exception;

/**
 * @author nihao
 * @date 2024/5/23
 */
public interface ErrorNozzle {

    /**
     * 错误代码
     *
     * @return
     */
    String getCode();

    /**
     * 获取错误信息
     *
     * @param var 参数列表
     * @return
     */
    String getErrorMsg(Object... var);
}
