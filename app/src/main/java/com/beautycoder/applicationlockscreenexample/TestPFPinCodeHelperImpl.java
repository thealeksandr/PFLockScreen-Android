package com.beautycoder.applicationlockscreenexample;

import android.content.Context;

import com.beautycoder.pflockscreen.security.IPFPinCodeHelper;
import com.beautycoder.pflockscreen.security.PFResult;
import com.beautycoder.pflockscreen.security.callbacks.PFPinCodeHelperCallback;

public class TestPFPinCodeHelperImpl implements IPFPinCodeHelper {

    @Override
    public void encodePin(Context context, String pin, PFPinCodeHelperCallback<String> callBack) {
        //Just an example : D
        if (callBack == null) {
            return;
        }
        callBack.onResult(new PFResult<String>(pin + "1111"));
    }

    @Override
    public void checkPin(
            Context context,
            String encodedPin,
            String pin,
            PFPinCodeHelperCallback<Boolean> callback
    ) {
        if (callback == null) {
            return;
        }
        callback.onResult(new PFResult<Boolean>(encodedPin.equals(pin + "1111")));
    }

    @Override
    public void delete(PFPinCodeHelperCallback<Boolean> callback) {
        //Delete all stuff related to pincode encryption
        //Like any additional keys etc;
        //Or anything on the server side
    }

    @Override
    public void isPinCodeEncryptionKeyExist(PFPinCodeHelperCallback<Boolean> callback) {
        //If you use anyadditional keys. For encryprion or anything else. Here should be check that
        //all that keys or whatever exist and you actually can perform decryption.
        //This is necessary for default PFPinCodeHelper with fingerprint and keystore.
        //Maybe your won't need it.
        callback.onResult(new PFResult<Boolean>(true));
    }
}
