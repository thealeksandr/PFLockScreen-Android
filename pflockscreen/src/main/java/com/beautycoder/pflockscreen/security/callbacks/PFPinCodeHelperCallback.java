package com.beautycoder.pflockscreen.security.callbacks;

import com.beautycoder.pflockscreen.security.PFResult;

public interface PFPinCodeHelperCallback<T> {
    void onResult(PFResult<T> result);
}
