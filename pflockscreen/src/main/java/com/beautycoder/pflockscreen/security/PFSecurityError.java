package com.beautycoder.pflockscreen.security;

public class PFSecurityError {

    private final String mMessage;
    private final Integer mCode;

    /**
     * Constructor.
     * @param message exception message.
     * @param code error code.
     */
    PFSecurityError(String message, Integer code) {
        mMessage = message;
        mCode = code;
    }

    /**
     * Get error message.
     * @return error message.
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * Get error code.
     * @return error code.
     */
    public Integer getCode() {
        return mCode;
    }
}
