package com.beautycoder.pflockscreen.security;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * Created by Aleksandr Nikiforov on 2018/02/09.
 *
 * PFFingerprintPinCodeHelper - helper class to encode/decode pin code string,
 * validate pin code etc.
 */
public class PFFingerprintPinCodeHelper {

    private static final String FINGERPRINT_ALIAS = "fp_fingerprint_lock_screen_key_store";
    private static final String PIN_ALIAS = "fp_pin_lock_screen_key_store";

    private static final PFFingerprintPinCodeHelper ourInstance = new PFFingerprintPinCodeHelper();

    public static PFFingerprintPinCodeHelper getInstance() {
        return ourInstance;
    }

    private final IPFSecurityUtils pfSecurityUtils
            = PFSecurityUtilsFactory.getPFSecurityUtilsInstance();

    private PFFingerprintPinCodeHelper() {

    }

    /**
     * Encode pin code.
     * @param context any context.
     * @param pin pin code string.
     * @return encoded pin code string.
     * @throws PFSecurityException throw exception if something went wrong.
     */
    public String encodePin(Context context, String pin)
            throws PFSecurityException {
        return pfSecurityUtils.encode(context, PIN_ALIAS, pin, false);
    }

    /**
     * Check if pin code is valid.
     * @param context any context.
     * @param encodedPin encoded pin code string.
     * @param pin pin code string to check.
     * @return true if pin codes matches.
     * @throws PFSecurityException  throw exception if something went wrong.
     */
    public boolean checkPin(Context context, String encodedPin, String pin)
            throws PFSecurityException {
        final String pinCode = pfSecurityUtils.decode(PIN_ALIAS, encodedPin);
        return pinCode.equals(pin);
    }


    private boolean isFingerPrintAvailable(Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    private boolean isFingerPrintReady(Context context) {
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }

    /**
     * Delete pin code encryption key.
     * @throws PFSecurityException throw exception if something went wrong.
     */
    public void delete() throws PFSecurityException {
        pfSecurityUtils.deleteKey(PIN_ALIAS);
    }

    /**
     * Check if pin code encryption key is exist.
     * @return true if key exist in KeyStore.
     * @throws PFSecurityException throw exception if something went wrong.
     */
    public boolean isPinCodeEncryptionKeyExist() throws PFSecurityException {
        return pfSecurityUtils.isKeystoreContainAlias(PIN_ALIAS);
    }

}
