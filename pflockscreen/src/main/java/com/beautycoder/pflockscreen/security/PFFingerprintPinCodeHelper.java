package com.beautycoder.pflockscreen.security;

import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * Created by aleksandr on 2018/02/09.
 */

public class PFFingerprintPinCodeHelper {

    private static final String FINGERPRINT_ALIAS = "fp_fingerprint_lock_screen_key_store";
    private static final String PIN_ALIAS = "fp_pin_lock_screen_key_store";

    private static final PFFingerprintPinCodeHelper ourInstance = new PFFingerprintPinCodeHelper();

    public static PFFingerprintPinCodeHelper getInstance() {
        return ourInstance;
    }

    private PFFingerprintPinCodeHelper() {

    }

    public String savePin(Context context, String pin, boolean usefingerprint)
            throws PFSecurityException {
        return PFSecurityUtils.getInstance().encode(PIN_ALIAS, pin, false);
    }

    public boolean checkPin(Context context, String encodedPin, String pin)
            throws PFSecurityException {
        String pinCode = PFSecurityUtils.getInstance().decode(PIN_ALIAS, encodedPin);
        return pinCode.equals(pin);
    }


    private boolean isFingerPrintAvailable(Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    private boolean isFingerPrintReady(Context context) {
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }

    public void delete() throws PFSecurityException {
        PFSecurityUtils.getInstance().deleteKey(PIN_ALIAS);
    }

    public boolean isPinCodeExist() throws PFSecurityException {
        return PFSecurityUtils.getInstance().isKeystoreContainAlias(PIN_ALIAS);
    }

}
