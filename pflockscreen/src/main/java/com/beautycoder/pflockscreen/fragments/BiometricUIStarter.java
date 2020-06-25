package com.beautycoder.pflockscreen.fragments;

import android.content.Context;
import android.content.res.Resources;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.beautycoder.pflockscreen.R;

import java.util.concurrent.Executor;

public class BiometricUIStarter {
    private final BiometricManager biometricManager;
    private final BiometricPrompt biometricPrompt;
    private String title = "Biometric Authentication";
    private String description;
    private boolean confirmationRequired;
    private String usePin;

    public BiometricUIStarter(BiometricManager bioManager, Context context, Fragment fragment, BiometricPrompt.AuthenticationCallback callback) {
        biometricManager = bioManager;
        usePin = context.getResources().getString(R.string.use_pin_pf);
        biometricPrompt = instanceOfBiometricPrompt(context, fragment, callback);
    }

    private BiometricPrompt instanceOfBiometricPrompt(Context context, Fragment fragment, BiometricPrompt.AuthenticationCallback callback) {
        Executor executor = ContextCompat.getMainExecutor(context);
        return new BiometricPrompt(fragment, executor ,callback);
    }

    public boolean isBiometricAuthAvailable() {
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }

    public boolean isBiometricAuthNotSet() {
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED;
    }

    private BiometricPrompt.PromptInfo getPromptInfo() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setDescription(description)
                .setDeviceCredentialAllowed(false)
                .setNegativeButtonText(usePin)
                .setConfirmationRequired(confirmationRequired)
                .build();
    }

    public void startUI() {
        biometricPrompt.authenticate(getPromptInfo());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setConfirmationRequired(boolean confirmationRequired) {
        this.confirmationRequired = confirmationRequired;
    }
}
