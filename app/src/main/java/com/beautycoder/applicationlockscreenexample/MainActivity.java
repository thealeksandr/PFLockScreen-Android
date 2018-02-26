package com.beautycoder.applicationlockscreenexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beautycoder.pflockscreen.fragments.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.security.PFFingerprintPinCodeHelper;
import com.beautycoder.pflockscreen.security.PFSecurityException;

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
            showMainFragment();
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
        PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(this)
                .setTitle("Unlock with your pin code or fingerprint")
                .setLeftButton("Can't remeber", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setUseFingerprint(true);
        PFLockScreenFragment fragment = new PFLockScreenFragment();

        try {
            boolean isPinExist = PFFingerprintPinCodeHelper.getInstance().isPinCodeEncryptionKeyExist();
            builder.setMode(isPinExist
                    ? PFFLockScreenConfiguration.MODE_AUTH
                    : PFFLockScreenConfiguration.MODE_CREATE);
            if (isPinExist) {
                fragment.setEncodedPinCode(PreferencesSettings.getCode(this));
                fragment.setLoginListener(mLoginListener);
            }
        } catch (PFSecurityException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Can not get pin code info", Toast.LENGTH_SHORT).show();
            return;
        }
        fragment.setConfiguration(builder.build());
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
