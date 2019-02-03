package com.beautycoder.pflockscreen.security;

import android.content.Context;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.beautycoder.pflockscreen.security.callbacks.PFPinCodeHelperCallback;

/**
 * Created by Aleksandr Nikiforov on 2018/02/09.
 *
 * PFFingerprintPinCodeHelper - helper class to encode/decode pin code string,
 * validate pin code etc.
 */
public class PFFingerprintPinCodeHelper implements IPFPinCodeHelper {


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
    @Override
    public void encodePin(Context context, String pin, PFPinCodeHelperCallback<String> callback) {
        try {
            final String encoded = pfSecurityUtils.encode(context, PIN_ALIAS, pin, false);
            if (callback != null) {
                callback.onResult(new PFResult(encoded));
            }
        } catch (PFSecurityException e) {
            if (callback != null) {
                callback.onResult(new PFResult(e.getError()));
            }
        }
    }

    /**
     * Check if pin code is valid.
     * @param context any context.
     * @param encodedPin encoded pin code string.
     * @param pin pin code string to check.
     * @return true if pin codes matches.
     * @throws PFSecurityException  throw exception if something went wrong.
     */
    @Override
    public void checkPin(Context context, String encodedPin, String pin, PFPinCodeHelperCallback<Boolean> callback) {
        try {
            final String pinCode = pfSecurityUtils.decode(PIN_ALIAS, encodedPin);
            if (callback != null) {
                callback.onResult(new PFResult(pinCode.equals(pin)));
            }
        } catch (PFSecurityException e) {
            if (callback != null) {
                callback.onResult(new PFResult(e.getError()));
            }
        }
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
    @Override
    public void delete(PFPinCodeHelperCallback<Boolean> callback) {
        try {
            pfSecurityUtils.deleteKey(PIN_ALIAS);
            if (callback != null) {
                callback.onResult(new PFResult(true));
            }
        } catch (PFSecurityException e) {
            if (callback != null) {
                callback.onResult(new PFResult(e.getError()));
            }
        }
    }

    /**
     * Check if pin code encryption key is exist.
     * @return true if key exist in KeyStore.
     * @throws PFSecurityException throw exception if something went wrong.
     */
    @Override
    public void isPinCodeEncryptionKeyExist(PFPinCodeHelperCallback<Boolean> callback) {
        try {
            final boolean isExist = pfSecurityUtils.isKeystoreContainAlias(PIN_ALIAS);
            if (callback != null) {
                callback.onResult(new PFResult(isExist));
            }
        } catch (PFSecurityException e) {
            if (callback != null) {
                callback.onResult(new PFResult(e.getError()));
            }
        }
    }

}
