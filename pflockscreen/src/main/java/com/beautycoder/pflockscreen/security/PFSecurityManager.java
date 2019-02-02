package com.beautycoder.pflockscreen.security;

public class PFSecurityManager {
    private static final PFSecurityManager ourInstance = new PFSecurityManager();

    public static PFSecurityManager getInstance() {
        return ourInstance;
    }

    private PFSecurityManager() {
    }

    private IPFPinCodeHelper mPinCodeHelper = PFFingerprintPinCodeHelper.getInstance();

    public void setPinCodeHelper(IPFPinCodeHelper pinCodeHelper) {
        mPinCodeHelper = pinCodeHelper;
    }

    public IPFPinCodeHelper getPinCodeHelper() {
        return mPinCodeHelper;
    }
}
