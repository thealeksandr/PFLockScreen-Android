package com.beautycoder.pflockscreen;

import android.content.Context;
import androidx.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by Aleksandr Nikiforov on 2018/02/14.
 */
public class PFFLockScreenConfiguration {

    private String mLeftButton = "";
    private String mNextButton = "";
    private View.OnClickListener mOnLeftButtonClickListener = null;
    private boolean mUseFingerprint = false;
    private boolean mAutoShowFingerprint = false;
    private String mTitle = "";
    private int mMode = MODE_AUTH;
    private int mCodeLength = 4;
    private boolean mClearCodeOnError = false;

    private PFFLockScreenConfiguration(Builder builder) {
        mLeftButton = builder.mLeftButton;
        mNextButton = builder.mNextButton;
        mUseFingerprint = builder.mUseFingerprint;
        mAutoShowFingerprint = builder.mAutoShowFingerprint;
        mTitle = builder.mTitle;
        mOnLeftButtonClickListener = builder.mOnLeftButtonClickListener;
        mMode = builder.mMode;
        mCodeLength = builder.mCodeLength;
        mClearCodeOnError = builder.mClearCodeOnError;
    }

    public String getLeftButton() {
        return mLeftButton;
    }

    public String getNextButton() {
        return mNextButton;
    }

    public boolean isUseFingerprint() {
        return mUseFingerprint;
    }

    public boolean isAutoShowFingerprint() {
        return mAutoShowFingerprint;
    }

    public String getTitle() {
        return mTitle;
    }

    public View.OnClickListener getOnLeftButtonClickListener() {
        return mOnLeftButtonClickListener;
    }

    public int getCodeLength() {
        return mCodeLength;
    }

    public boolean isClearCodeOnError() {
        return mClearCodeOnError;
    }

    @PFLockScreenMode
    public int getMode() {
        return this.mMode;
    }

    public static class Builder {

        private String mLeftButton = "";
        private String mNextButton = "";
        private View.OnClickListener mOnLeftButtonClickListener = null;
        private boolean mUseFingerprint = false;
        private boolean mAutoShowFingerprint = false;
        private String mTitle = "";
        private int mMode = 0;
        private int mCodeLength = 4;
        private boolean mClearCodeOnError = false;


        public Builder(Context context) {
            mTitle = context.getResources().getString(R.string.lock_screen_title_pf);
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setLeftButton(String leftButton, View.OnClickListener onClickListener) {
            mLeftButton = leftButton;
            mOnLeftButtonClickListener = onClickListener;
            return this;
        }

        public Builder setNextButton(String nextButton) {
            mNextButton = nextButton;
            return this;
        }

        public Builder setUseFingerprint(boolean useFingerprint) {
            mUseFingerprint = useFingerprint;
            return this;
        }

        public Builder setAutoShowFingerprint(boolean autoShowFingerprint) {
            mAutoShowFingerprint = autoShowFingerprint;
            return this;
        }

        public Builder setMode(@PFLockScreenMode int mode) {
            mMode = mode;
            return this;
        }

        public Builder setCodeLength(int codeLength) {
            this.mCodeLength = codeLength;
            return this;
        }

        public Builder setClearCodeOnError(boolean clearCodeOnError) {
            mClearCodeOnError = clearCodeOnError;
            return this;
        }

        public PFFLockScreenConfiguration build() {
            return new PFFLockScreenConfiguration(
                    this);
        }


    }

    @Retention(SOURCE)
    @IntDef({MODE_CREATE, MODE_AUTH})
    public @interface PFLockScreenMode {}
    public static final int MODE_CREATE = 0;
    public static final int MODE_AUTH = 1;

}
