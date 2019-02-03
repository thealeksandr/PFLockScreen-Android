package com.beautycoder.pflockscreen.viewmodels;

import androidx.lifecycle.LiveData;
import android.content.Context;

import com.beautycoder.pflockscreen.security.PFSecurityManager;
import com.beautycoder.pflockscreen.security.PFResult;
import com.beautycoder.pflockscreen.security.callbacks.PFPinCodeHelperCallback;
import com.beautycoder.pflockscreen.security.livedata.PFLiveData;


public class PFPinCodeViewModel {

    public LiveData<PFResult<String>> encodePin(Context context, String pin) {
        final PFLiveData<PFResult<String>> liveData = new PFLiveData<>();
        PFSecurityManager.getInstance().getPinCodeHelper().encodePin(
                context,
                pin,
                new PFPinCodeHelperCallback<String>() {
                    @Override
                    public void onResult(PFResult<String> result) {
                        liveData.setData(result);
                    }
                }
        );
        return liveData;
    }

    public LiveData<PFResult<Boolean>> checkPin(Context context, String encodedPin, String pin) {
        final PFLiveData<PFResult<Boolean>> liveData = new PFLiveData<>();
        PFSecurityManager.getInstance().getPinCodeHelper().checkPin(
                context,
                encodedPin,
                pin,
                new PFPinCodeHelperCallback<Boolean>() {
                    @Override
                    public void onResult(PFResult<Boolean> result) {
                        liveData.setData(result);
                    }
                }
        );
        return liveData;
    }

    public LiveData<PFResult<Boolean>> delete() {
        final PFLiveData<PFResult<Boolean>> liveData = new PFLiveData<>();
        PFSecurityManager.getInstance().getPinCodeHelper().delete(
                new PFPinCodeHelperCallback<Boolean>() {
                    @Override
                    public void onResult(PFResult<Boolean> result) {
                        liveData.setData(result);
                    }
                }
        );
        return liveData;
    }

    public LiveData<PFResult<Boolean>> isPinCodeEncryptionKeyExist() {
        final PFLiveData<PFResult<Boolean>> liveData = new PFLiveData<>();
        PFSecurityManager.getInstance().getPinCodeHelper().isPinCodeEncryptionKeyExist(
                new PFPinCodeHelperCallback<Boolean>() {
                    @Override
                    public void onResult(PFResult<Boolean> result) {
                        liveData.setData(result);
                    }
                }
        );
        return liveData;
    }

}
