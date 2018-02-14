package com.beautycoder.applicationlockscreenexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.security.FingerprintPinCodeHelper;
import com.beautycoder.pflockscreen.security.PFSecurityException;
import com.beautycoder.pflockscreen.security.SecurityUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showLockScreenFragment();
    }


    private PFLockScreenFragment.OnPFLockScreenCodeCreateListener mCodeCreateListener =
            new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {
        @Override
        public void onCodeCreated(String encodedCode) {
            Toast.makeText(MainActivity.this, "Code created", Toast.LENGTH_SHORT).show();
            PreferencesSettings.saveToPref(MainActivity.this, encodedCode);

        }
    };

    private PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener =
            new PFLockScreenFragment.OnPFLockScreenLoginListener() {

        @Override
        public void onCodeInputSuccessful() {
            Toast.makeText(MainActivity.this, "Code successfull", Toast.LENGTH_SHORT).show();
            showMainFragment();
        }

        @Override
        public void onFingerprintSuccessful() {
            Toast.makeText(MainActivity.this, "Fingerprint successfull", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPinLoginFailed() {
            Toast.makeText(MainActivity.this, "Pin failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFingerprintLoginFailed() {
            Toast.makeText(MainActivity.this, "Fingerprint failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void showLockScreenFragment() {
        PFLockScreenFragment fragment = new PFLockScreenFragment();
        try {
            boolean isPinExist = FingerprintPinCodeHelper.getInstance().isPincodeExist();
            fragment.setCreatePasswordMode(!isPinExist);
            if (isPinExist) {
                fragment.setEncodedPinCode(PreferencesSettings.getCode(this));
                fragment.setLoginListener(mLoginListener);
            }
        } catch (PFSecurityException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Can not get pin code info", Toast.LENGTH_SHORT).show();
            return;
        }
        fragment.setCodeCreateListener(mCodeCreateListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }

    private  void showMainFragment() {
        MainFragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }



}
