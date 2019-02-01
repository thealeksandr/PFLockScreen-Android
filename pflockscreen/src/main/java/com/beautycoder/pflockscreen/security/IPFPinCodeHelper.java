package com.beautycoder.pflockscreen.security;

import android.content.Context;

import com.beautycoder.pflockscreen.security.callbacks.PFSecurityCallback;

public interface IPFPinCodeHelper {

    /**
     * Encode pin
     * @param context any context.
     * @param pin pin code string to check.
     * @param callback PFSecurityCallback callback object.
     * @return true if pin codes matches.
     * @throws PFSecurityException  throw exception if something went wrong.
     */
    void encodePin(Context context, String pin, PFSecurityCallback<String> callBack);

    /**
     * Check if pin code is valid.
     * @param context any context.
     * @param encodedPin encoded pin code string.
     * @param pin pin code string to check.
     * @param callback PFSecurityCallback callback object.
     * @return true if pin codes matches.
     * @throws PFSecurityException  throw exception if something went wrong.
     */
    void checkPin(
            Context context,
            String encodedPin,
            String pin,
            PFSecurityCallback<Boolean> callback
    );

    /**
     * Delete pin code encryption key.
     * @param callback PFSecurityCallback callback object.
     * @throws PFSecurityException throw exception if something went wrong.
     */
    void delete(PFSecurityCallback<Boolean> callback);

    /**
     * Check if pin code encryption key is exist.
     * @param callback PFSecurityCallback callback object.
     * @return true if key exist in KeyStore.
     * @throws PFSecurityException throw exception if something went wrong.
     */
    void isPinCodeEncryptionKeyExist(PFSecurityCallback<Boolean> callback);

}
