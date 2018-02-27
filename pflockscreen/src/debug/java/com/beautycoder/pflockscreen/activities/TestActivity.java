package com.beautycoder.pflockscreen.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

/**
 * Created by aleksandr on 2018/02/27.
 */
@VisibleForTesting
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(android.R.id.content);
        setContentView(frameLayout);
    }

}


