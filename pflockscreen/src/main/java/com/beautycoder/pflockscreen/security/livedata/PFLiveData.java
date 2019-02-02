package com.beautycoder.pflockscreen.security.livedata;

import android.arch.lifecycle.LiveData;

public class PFLiveData<T> extends LiveData<T> {

    public void setData(T data) {
        setValue(data);
    }

}
