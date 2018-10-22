package com.beautycoder.pflockscreen.viewmodels;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.beautycoder.pflockscreen.security.PFSecurityManager;
import com.beautycoder.pflockscreen.security.PFSecurityResult;
import com.beautycoder.pflockscreen.security.callbacks.PFSecurityCallback;
import com.beautycoder.pflockscreen.security.livedata.PFLiveData;


public class PFPinCodeViewModel {

    public LiveData<PFSecurityResult<String>> encodePin(Context context, String pin) {
        final PFLiveData<PFSecurityResult<String>> liveData = new PFLiveData<>();
        PFSecurityManager.getInstance().getPinCodeHelper().encodePin(
                context,
                pin,
                new PFSecurityCallback<String>() {
                    @Override
                    public void onResult(PFSecurityResult<String> result) {
                        liveData.setData(result);
                    }
                }
        );
        return liveData;
    }

    public LiveData<PFSecurityResult<Boolean>> checkPin(Context context, String encodedPin, String pin) {
        final PFLiveData<PFSecurityResult<Boolean>> liveData = new PFLiveData<>();
        PFSecurityManager.getInstance().getPinCodeHelper().checkPin(
                context,
                encodedPin,
                pin,
                new PFSecurityCallback<Boolean>() {
                    @Override
                    public void onResult(PFSecurityResult<Boolean> result) {
                        liveData.setData(result);
                    }
                }
        );
        return liveData;
    }

    public LiveData<PFSecurityResult<Boolean>> delete() {
        final PFLiveData<PFSecurityResult<Boolean>> liveData = new PFLiveData<>();
        PFSecurityManager.getInstance().getPinCodeHelper().delete(
                new PFSecurityCallback<Boolean>() {
                    @Override
                    public void onResult(PFSecurityResult<Boolean> result) {
                        liveData.setData(result);
                    }
                }
        );
        return liveData;
    }

    public LiveData<PFSecurityResult<Boolean>> isPinCodeEncryptionKeyExist() {
        final PFLiveData<PFSecurityResult<Boolean>> liveData = new PFLiveData<>();
        PFSecurityManager.getInstance().getPinCodeHelper().isPinCodeEncryptionKeyExist(
                new PFSecurityCallback<Boolean>() {
                    @Override
                    public void onResult(PFSecurityResult<Boolean> result) {
                        liveData.setData(result);
                    }
                }
        );
        return liveData;
    }

}
