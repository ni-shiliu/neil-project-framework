package com.neil.project.exception;

import cn.hutool.core.util.ArrayUtil;
import lombok.Getter;

import java.text.MessageFormat;

/**
 * @author nihao
 * @date 2024/5/23
 */
@Getter
public enum ErrorCode implements ErrorNozzle {

    HTTP_METHOD_NOT_ALLOWED("400000", "请求方法不支持"),

    HTTP_MESSAGE_NOT_READABLE("400001", "请求数据错误或不可读"),

    UNSUPPORTED_MEDIA_TYPE("400002", "不支持的资源类型"),

    NOT_ACCEPTABLE("400003", "不支持的返回类型"),

    MISSING_PATH_VARIABLE("400004", "路径变量缺失"),

    MISSING_SERVLET_REQUEST_PARAMETER("400005", "请求参数缺失"),

    SERVLET_REQUEST_BINDING_EXCEPTION("40006", "参数绑定失败"),

    CONVERSION_NOT_SUPPORTED("400007", "转换不支持"),

    TYPE_MISMATCH("400008", "类型不匹配"),

    HTTP_MESSAGE_NOT_WRITEABLE("400009", "返回数据错误或不可写"),

    MISSING_SERVLET_REQUEST_PART("400010", "部份请求缺失"),

    NO_HANDLER_FOUND("400011", "未找到匹配请求的方法"),

    ASYNC_REQUEST_TIMEOUT("400012", "异步请求超时"),

    PARAM_ILLEGAL("400013", "参数不合法"),

    NEED_LOGIN("400014", "用户未登录或验签失败"),

    REQUEST_EXPIRED("400015", "请求失效"),

    INVALID_SIGN("400016", "签名不正确"),

    INVALID_TOKEN("400017", "token不正确"),

    SYS_ERROR("500000", "系统错误"),

    FILE_MAX_UPLOAD_SIZE_EXCEEDED("500001", "文件上传超过最大限制")


    ;

    private String code;

    private String pattern;

     private ErrorCode(String code, String pattern) {
        this.code = code;
        this.pattern = pattern;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getErrorMsg(Object... params) {
        if (ArrayUtil.isEmpty(params)) {
            params = new String[] { "" };
        }
        MessageFormat msgFmt = new MessageFormat(this.pattern);
        return msgFmt.format(params);
    }
}
