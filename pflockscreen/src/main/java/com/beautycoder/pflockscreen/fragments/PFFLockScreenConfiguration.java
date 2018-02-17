package com.beautycoder.pflockscreen.fragments;

import android.content.Context;
import android.content.res.Resources;

import com.beautycoder.pflockscreen.R;

/**
 * Created by aleksandr on 2018/02/14.
 */

public class PFFLockScreenConfiguration {

    private String mLeftButton = "";
    private boolean mUseFingerprint = false;
    private String mTitle = "";

    private PFFLockScreenConfiguration(Builder builder) {
        mLeftButton = builder.mLeftButton;
        mUseFingerprint = builder.mUseFingerprint;
        mTitle = builder.mTitle;
    }

    String getLeftButton() {
        return mLeftButton;
    }

    boolean isUseFingerprint() {
        return mUseFingerprint;
    }

    String getTitle() {
        return mTitle;
    }

    static class Builder {

        private String mLeftButton = "";
        private boolean mUseFingerprint = false;
        private String mTitle = "";

        public Builder(Context context) {
            mTitle = context.getResources().getString(R.string.lock_screen_title_pf);
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setLeftButton(String leftButton) {
            mLeftButton = leftButton;
            return this;
        }

        public Builder setUseFingerprint(boolean useFingerprint) {
            mUseFingerprint = useFingerprint;
            return this;
        }

        public PFFLockScreenConfiguration build() {
            return new PFFLockScreenConfiguration(this);
        }



    }

}
