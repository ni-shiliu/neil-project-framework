package com.neil.project.common;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/5/6
 */
@Data
public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 2188813950875028891L;

    private String code;

    private T result;

    private String errorMessage;

    public static <T> BaseResult<T> success(T value) {
        BaseResult<T> result = new BaseResult<>();
        result.setCode("0");
        result.setResult(value);
        return result;
    }

    public static <T> BaseResult<T> success() {
        BaseResult<T> result = new BaseResult<>();
        result.setCode("0");
        return result;
    }

    public static <T> BaseResult<T> fail(String code, String message) {
        BaseResult<T> result = new BaseResult<>();
        result.setCode(code);
        result.setErrorMessage(message);
        return result;
    }

    public boolean isSuccess() {
        return Strings.isNotBlank(code) && "0".equals(code);
    }

}
