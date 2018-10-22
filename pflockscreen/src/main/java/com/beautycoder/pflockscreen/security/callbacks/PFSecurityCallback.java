package com.beautycoder.pflockscreen.security.callbacks;

import com.beautycoder.pflockscreen.security.PFSecurityResult;

public interface PFSecurityCallback<T> {
    void onResult(PFSecurityResult<T> result);
}
