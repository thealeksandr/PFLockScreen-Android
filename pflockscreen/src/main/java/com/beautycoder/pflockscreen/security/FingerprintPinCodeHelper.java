package com.beautycoder.pflockscreen.security;

import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * Created by aleksandr on 2018/02/09.
 */

public class FingerprintPinCodeHelper {

    private static final String FINGERPRINT_ALIAS = "fp_fingerprint_lock_screen_key_store";
    private static final String PIN_ALIAS = "fp_pin_lock_screen_key_store";

    private static final FingerprintPinCodeHelper ourInstance = new FingerprintPinCodeHelper();

    public static FingerprintPinCodeHelper getInstance() {
        return ourInstance;
    }

    private FingerprintPinCodeHelper() {

    }

    public String savePin(Context context, String pin, boolean usefingerprint)
            throws PFSecurityException {
        return SecurityUtils.getInstance().encode(PIN_ALIAS, pin, false);
    }

    public boolean checkPin(Context context, String encodedPin, String pin)
            throws PFSecurityException {
        String pinCode = SecurityUtils.getInstance().decode(PIN_ALIAS, encodedPin);
        return pinCode.equals(pin);
    }

    public void useFingerprint(Context context) {

    }

    private boolean isFingerPrintAvailable(Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    private boolean isFingerPrintReady(Context context) {
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }

    public void delete() {

    }

    public boolean isPincodeExist() throws PFSecurityException {
        return SecurityUtils.getInstance().isKeystoreContainAlias(PIN_ALIAS);
    }

}
