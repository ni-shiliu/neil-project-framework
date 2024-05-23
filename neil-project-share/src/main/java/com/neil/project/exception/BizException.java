package com.neil.project.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author nihao
 * @date 2024/5/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 3518849216586157765L;

    private String errorCode;

    private String errorMsg;

    public BizException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BizException(ErrorNozzle code, Object... args) {
        super(code.getErrorMsg(args));
        this.errorCode = code.getCode();
        this.errorMsg = super.getMessage();
    }

    public BizException(ErrorNozzle code) {
        super(code.getErrorMsg());
        this.errorCode = code.getCode();
        this.errorMsg = super.getMessage();
    }

    public BizException(Throwable cause) {
        super(cause);
    }

}
