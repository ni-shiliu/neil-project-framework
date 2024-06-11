package com.neil.myth.common.exception;

/**
 * @author nihao
 * @date 2024/6/7
 */
public class MythException extends RuntimeException {


    private static final long serialVersionUID = -7546139912932183887L;

    public MythException() {
    }

    public MythException(final String message) {
        super(message);
    }

    public MythException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MythException(final Throwable cause) {
        super(cause);
    }
}
