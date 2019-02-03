package com.beautycoder.pflockscreen.security.livedata;

import androidx.lifecycle.LiveData;

public class PFLiveData<T> extends LiveData<T> {

    public void setData(T data) {
        setValue(data);
    }

}
