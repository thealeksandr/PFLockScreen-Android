package com.beautycoder.pflockscreen.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beautycoder.pflockscreen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleksandr on 2018/02/07.
 */

public class PFCodeView extends LinearLayout {

    private static final int DEFAULT_CODE_LENGTH = 4;
    List<ImageView> codeViews = new ArrayList<>();
    private String code = "";
    private int codeLength = 4;
    private OnPFCodeListener mListener;


    public PFCodeView(Context context) {
        super(context);
        init();
    }

    public PFCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PFCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PFCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                      int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_code_pf_lockscreen, this);
        for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
            final ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.code_fp_size),
                    getResources().getDimensionPixelSize(R.dimen.code_fp_size));
            int margin = getResources().getDimensionPixelSize(R.dimen.code_fp_margin);
            layoutParams.setMargins(margin, margin, margin, margin);
            imageView.setLayoutParams(layoutParams);
            imageView.setBackgroundColor(android.R.color.holo_green_dark);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.circle_code_empty_pf_lockscreen));
            addView(imageView);
            codeViews.add(imageView);
        }
    }

    public int input(String number) {
        if (code.length() == codeLength) {
            return code.length();
        }
        codeViews.get(code.length()).setImageDrawable(getResources()
                .getDrawable(R.drawable.circle_code_fill_pf_lockscreen));
        code += number;
        if (code.length() == codeLength && mListener != null) {
            mListener.onCodeCompleted(code);
        }
        return code.length();
    }

    public int delete() {
        if (mListener != null) {
            mListener.onCodeNotCompleted(code);
        }
        if (code.length() == 0) {
            return code.length();
        }
        code = code.substring(0, code.length() - 1);
        codeViews.get(code.length()).setImageDrawable(getResources()
                .getDrawable(R.drawable.circle_code_empty_pf_lockscreen));
        return code.length();
    }

    public void setListener(OnPFCodeListener listener) {
        mListener = listener;
    }

    public interface OnPFCodeListener {

        void onCodeCompleted(String code);

        void onCodeNotCompleted(String code);

    }
}
