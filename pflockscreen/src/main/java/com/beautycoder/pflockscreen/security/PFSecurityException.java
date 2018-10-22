package com.beautycoder.pflockscreen.security;

/**
 * Created by aleksandr on 2018/02/10.
 *
 * Exception class for PFSecurityUtils.
 */
public class PFSecurityException extends Exception {

    private final Integer mCode;

    /**
     * Constructor.
     * @param message exception message.
     * @param code error code.
     */
    public PFSecurityException(String message, Integer code) {
        super(message);
        mCode = code;
    }

    /**
     * Get error code.
     * @return error code.
     */
    public Integer getCode() {
        return mCode;
    }

    /**
     * Get PFSecurityError object.
     * @return PFSecurityError from PFSecurityException message and error code.
     */
    public PFSecurityError getError() {
        return new PFSecurityError(getMessage(), getCode());
    }
}
