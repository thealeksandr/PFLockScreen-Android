package com.beautycoder.pflockscreen.security;

import android.content.Context;

import com.beautycoder.pflockscreen.security.callbacks.PFPinCodeHelperCallback;

public interface IPFPinCodeHelper {

    /**
     * Encode pin
     * @param context any context.
     * @param pin pin code string to check.
     * @param callback PFPinCodeHelperCallback callback object.
     * @return true if pin codes matches.
     * @throws PFSecurityException  throw exception if something went wrong.
     */
    void encodePin(Context context, String pin, PFPinCodeHelperCallback<String> callBack);

    /**
     * Check if pin code is valid.
     * @param context any context.
     * @param encodedPin encoded pin code string.
     * @param pin pin code string to check.
     * @param callback PFPinCodeHelperCallback callback object.
     * @return true if pin codes matches.
     * @throws PFSecurityException  throw exception if something went wrong.
     */
    void checkPin(
            Context context,
            String encodedPin,
            String pin,
            PFPinCodeHelperCallback<Boolean> callback
    );

    /**
     * Delete pin code encryption key.
     * @param callback PFPinCodeHelperCallback callback object.
     * @throws PFSecurityException throw exception if something went wrong.
     */
    void delete(PFPinCodeHelperCallback<Boolean> callback);

    /**
     * Check if pin code encryption key is exist.
     * @param callback PFPinCodeHelperCallback callback object.
     * @return true if key exist in KeyStore.
     * @throws PFSecurityException throw exception if something went wrong.
     */
    void isPinCodeEncryptionKeyExist(PFPinCodeHelperCallback<Boolean> callback);

}
