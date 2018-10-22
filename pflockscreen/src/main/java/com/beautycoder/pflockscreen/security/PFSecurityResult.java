package com.beautycoder.pflockscreen.security;

public class PFSecurityResult<T> {

    private PFSecurityError mError = null;
    private T mResult = null;

    public PFSecurityResult(PFSecurityError mError) {
        this.mError = mError;
    }

    public PFSecurityResult(T result) {
        mResult = result;
    }

    public PFSecurityError getError() {
        return mError;
    }

    public T getResult() {
        return mResult;
    }
}
