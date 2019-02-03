package com.beautycoder.pflockscreen.fragments;

import android.app.AlertDialog;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.R;
import com.beautycoder.pflockscreen.security.PFResult;
import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;
import com.beautycoder.pflockscreen.views.PFCodeView;

/**
 * Created by Aleksandr Nikiforov on 2018/02/07.
 * <p>
 * Lock Screen Fragment. Support pin code authorization and
 * fingerprint authorization for API 23 +.
 */
public class PFLockScreenFragment extends Fragment {

    private static final String TAG = PFLockScreenFragment.class.getName();

    private static final String FINGERPRINT_DIALOG_FRAGMENT_TAG = "FingerprintDialogFragment";

    private View mFingerprintButton;
    private View mDeleteButton;
    private TextView mLeftButton;
    private Button mNextButton;
    private PFCodeView mCodeView;

    private boolean mUseFingerPrint = true;
    private boolean mFingerprintHardwareDetected = false;
    private boolean mIsCreateMode = false;

    private OnPFLockScreenCodeCreateListener mCodeCreateListener;
    private OnPFLockScreenLoginListener mLoginListener;
    private String mCode = "";
    private String mEncodedPinCode = "";

    private PFFLockScreenConfiguration mConfiguration;
    private View mRootView;

    private final PFPinCodeViewModel mPFPinCodeViewModel = new PFPinCodeViewModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lock_screen_pf, container,
                false);
        mFingerprintButton = view.findViewById(R.id.button_finger_print);
        mDeleteButton = view.findViewById(R.id.button_delete);

        mLeftButton = view.findViewById(R.id.button_left);
        mNextButton = view.findViewById(R.id.button_next);

        mDeleteButton.setOnClickListener(mOnDeleteButtonClickListener);
        mDeleteButton.setOnLongClickListener(mOnDeleteButtonOnLongClickListener);
        mFingerprintButton.setOnClickListener(mOnFingerprintClickListener);

        mCodeView = view.findViewById(R.id.code_view);
        initKeyViews(view);

        mCodeView.setListener(mCodeListener);

        if (!mUseFingerPrint) {
            mFingerprintButton.setVisibility(View.GONE);
        }

        mFingerprintHardwareDetected = isFingerprintApiAvailable(getContext());

        mRootView = view;
        applyConfiguration(mConfiguration);

        return view;
    }

    @Override
    public void onStart() {
        if (!mIsCreateMode && mUseFingerPrint && mConfiguration.isAutoShowFingerprint() &&
                isFingerprintApiAvailable(getActivity()) && isFingerprintsExists(getActivity())) {
            mOnFingerprintClickListener.onClick(mFingerprintButton);
        }
        super.onStart();
    }

    public void setConfiguration(PFFLockScreenConfiguration configuration) {
        this.mConfiguration = configuration;
        applyConfiguration(configuration);
    }

    private void applyConfiguration(PFFLockScreenConfiguration configuration) {
        if (mRootView == null || configuration == null) {
            return;
        }
        final TextView titleView = mRootView.findViewById(R.id.title_text_view);
        titleView.setText(configuration.getTitle());
        if (TextUtils.isEmpty(configuration.getLeftButton())) {
            mLeftButton.setVisibility(View.GONE);
        } else {
            mLeftButton.setText(configuration.getLeftButton());
            mLeftButton.setOnClickListener(configuration.getOnLeftButtonClickListener());
        }

        if (!TextUtils.isEmpty(configuration.getNextButton())) {
            mNextButton.setText(configuration.getNextButton());
        }

        mUseFingerPrint = configuration.isUseFingerprint();
        if (!mUseFingerPrint) {
            mFingerprintButton.setVisibility(View.GONE);
            mDeleteButton.setVisibility(View.VISIBLE);
        }
        mIsCreateMode = mConfiguration.getMode() == PFFLockScreenConfiguration.MODE_CREATE;

        if (mIsCreateMode) {
            mLeftButton.setVisibility(View.GONE);
            mFingerprintButton.setVisibility(View.GONE);
        }

        if (mIsCreateMode) {
            mNextButton.setOnClickListener(mOnNextButtonClickListener);
        } else {
            mNextButton.setOnClickListener(null);
        }
        mCodeView.setCodeLength(mConfiguration.getCodeLength());
    }

    private void initKeyViews(View parent) {
        parent.findViewById(R.id.button_0).setOnClickListener(mOnKeyClickListener);
        parent.findViewById(R.id.button_1).setOnClickListener(mOnKeyClickListener);
        parent.findViewById(R.id.button_2).setOnClickListener(mOnKeyClickListener);
        parent.findViewById(R.id.button_3).setOnClickListener(mOnKeyClickListener);
        parent.findViewById(R.id.button_4).setOnClickListener(mOnKeyClickListener);
        parent.findViewById(R.id.button_5).setOnClickListener(mOnKeyClickListener);
        parent.findViewById(R.id.button_6).setOnClickListener(mOnKeyClickListener);
        parent.findViewById(R.id.button_7).setOnClickListener(mOnKeyClickListener);
        parent.findViewById(R.id.button_8).setOnClickListener(mOnKeyClickListener);
        parent.findViewById(R.id.button_9).setOnClickListener(mOnKeyClickListener);
    }

    private final View.OnClickListener mOnKeyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof TextView) {
                final String string = ((TextView) v).getText().toString();
                if (string.length() != 1) {
                    return;
                }
                final int codeLength = mCodeView.input(string);
                configureRightButton(codeLength);
            }
        }
    };

    private final View.OnClickListener mOnDeleteButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int codeLength = mCodeView.delete();
            configureRightButton(codeLength);
        }
    };

    private final View.OnLongClickListener mOnDeleteButtonOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            mCodeView.clearCode();
            configureRightButton(0);
            return true;
        }
    };

    private final View.OnClickListener mOnFingerprintClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                    !isFingerprintApiAvailable(getActivity())) {
                return;
            }


            if (!isFingerprintsExists(getActivity())) {
                showNoFingerprintDialog();
                return;
            }

            final PFFingerprintAuthDialogFragment fragment
                    = new PFFingerprintAuthDialogFragment();
            fragment.show(getFragmentManager(), FINGERPRINT_DIALOG_FRAGMENT_TAG);
            fragment.setAuthListener(new PFFingerprintAuthListener() {
                @Override
                public void onAuthenticated() {
                    if (mLoginListener != null) {
                        mLoginListener.onFingerprintSuccessful();
                    }
                    fragment.dismiss();
                }

                @Override
                public void onError() {
                    if (mLoginListener != null) {
                        mLoginListener.onFingerprintLoginFailed();
                    }
                }
            });
        }
    };

    private void configureRightButton(int codeLength) {
        if (mIsCreateMode) {
            if (codeLength > 0) {
                mDeleteButton.setVisibility(View.VISIBLE);
            } else {
                mDeleteButton.setVisibility(View.GONE);
            }
            return;
        }

        if (codeLength > 0) {
            mFingerprintButton.setVisibility(View.GONE);
            mDeleteButton.setVisibility(View.VISIBLE);
            mDeleteButton.setEnabled(true);
            return;
        }

        if (mUseFingerPrint && mFingerprintHardwareDetected) {
            mFingerprintButton.setVisibility(View.VISIBLE);
            mDeleteButton.setVisibility(View.GONE);
        } else {
            mFingerprintButton.setVisibility(View.GONE);
            mDeleteButton.setVisibility(View.VISIBLE);
        }

        mDeleteButton.setEnabled(false);

    }

    private boolean isFingerprintApiAvailable(Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    private boolean isFingerprintsExists(Context context) {
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }


    private void showNoFingerprintDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.no_fingerprints_title_pf)
                .setMessage(R.string.no_fingerprints_message_pf)
                .setCancelable(true)
                .setNegativeButton(R.string.cancel_pf, null)
                .setPositiveButton(R.string.settings_pf, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
                    }
                }).create().show();
    }


    private final PFCodeView.OnPFCodeListener mCodeListener = new PFCodeView.OnPFCodeListener() {
        @Override
        public void onCodeCompleted(String code) {
            if (mIsCreateMode) {
                mNextButton.setVisibility(View.VISIBLE);
                mCode = code;
                return;
            }
            mCode = code;
            mPFPinCodeViewModel.checkPin(getContext(), mEncodedPinCode, mCode).observe(
                    PFLockScreenFragment.this,
                    new Observer<PFResult<Boolean>>() {
                        @Override
                        public void onChanged(@Nullable PFResult<Boolean> result) {
                            if (result == null) {
                                return;
                            }
                            if (result.getError() != null) {
                                return;
                            }
                            final boolean isCorrect = result.getResult();
                            if (mLoginListener != null) {
                                if (isCorrect) {
                                    mLoginListener.onCodeInputSuccessful();
                                } else {
                                    mLoginListener.onPinLoginFailed();
                                    errorAction();
                                }
                            }
                            if (!isCorrect && mConfiguration.isClearCodeOnError()) {
                                mCodeView.clearCode();
                            }
                        }
                    });

        }

        @Override
        public void onCodeNotCompleted(String code) {
            if (mIsCreateMode) {
                mNextButton.setVisibility(View.GONE);
                return;
            }
        }
    };


    private final View.OnClickListener mOnNextButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPFPinCodeViewModel.encodePin(getContext(), mCode).observe(
                    PFLockScreenFragment.this,
                    new Observer<PFResult<String>>() {
                        @Override
                        public void onChanged(@Nullable PFResult<String> result) {
                            if (result == null) {
                                return;
                            }
                            if (result.getError() != null) {
                                Log.d(TAG, "Can not encode pin code");
                                deleteEncodeKey();
                                return;
                            }
                            final String encodedCode = result.getResult();
                            if (mCodeCreateListener != null) {
                                mCodeCreateListener.onCodeCreated(encodedCode);
                            }
                        }
                    }
            );
        }
    };


    private void deleteEncodeKey() {
        mPFPinCodeViewModel.delete().observe(
                this,
                new Observer<PFResult<Boolean>>() {
                    @Override
                    public void onChanged(@Nullable PFResult<Boolean> result) {
                        if (result == null) {
                            return;
                        }
                        if (result.getError() != null) {
                            Log.d(TAG, "Can not delete the alias");
                            return;
                        }

                    }
                }
        );
    }

    private void errorAction() {
        final Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            v.vibrate(400);
        }
        final Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake_pf);
        mCodeView.startAnimation(animShake);
    }




    /*private void showFingerprintAlertDialog(Context context) {
        new AlertDialog.Builder(context).setTitle("Fingerprint").setMessage(
                "Would you like to use fingerprint for future login?")
                .setPositiveButton("Use fingerprint", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if (isFingerprintsExists(getContext())) {
                    //PFFingerprintPinCodeHelper.getInstance().encodePin()
                //}
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }*/


    /**
     * Set OnPFLockScreenCodeCreateListener.
     *
     * @param listener OnPFLockScreenCodeCreateListener object.
     */
    public void setCodeCreateListener(OnPFLockScreenCodeCreateListener listener) {
        mCodeCreateListener = listener;
    }

    /**
     * Set OnPFLockScreenLoginListener.
     *
     * @param listener OnPFLockScreenLoginListener object.
     */
    public void setLoginListener(OnPFLockScreenLoginListener listener) {
        mLoginListener = listener;
    }

    /**
     * Set Encoded pin code.
     *
     * @param encodedPinCode encoded pin code string, that was created before.
     */
    public void setEncodedPinCode(String encodedPinCode) {
        mEncodedPinCode = encodedPinCode;
    }


    /**
     * Pin Code create callback interface.
     */
    public interface OnPFLockScreenCodeCreateListener {

        /**
         * Callback method for pin code creation.
         *
         * @param encodedCode encoded pin code string.
         */
        void onCodeCreated(String encodedCode);

    }


    /**
     * Login callback interface.
     */
    public interface OnPFLockScreenLoginListener {

        /**
         * Callback method for successful login attempt with pin code.
         */
        void onCodeInputSuccessful();

        /**
         * Callback method for successful login attempt with fingerprint.
         */
        void onFingerprintSuccessful();

        /**
         * Callback method for unsuccessful login attempt with pin code.
         */
        void onPinLoginFailed();

        /**
         * Callback method for unsuccessful login attempt with fingerprint.
         */
        void onFingerprintLoginFailed();

    }


}


//private static final String KEY_STORE_NAME = "fp_lock_screen_key_store";
//private Cipher cipher;
//private KeyStore keyStore;
//private KeyguardManager keyguardManager;
//private static KeyGenerator keyGenerator;
//private Cipher defaultCipher;


 /* try {
            generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        keyguardManager = (KeyguardManager) getContext().getSystemService(KEYGUARD_SERVICE);*/

        /*if (!isFingerprintApiAvailable(getContext())) {
            mOnFingerprintButton.setImageDrawable(getResources()
                    .getDrawable(R.drawable.delete_lockscreen_pf));
        }
        try {
            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }
        prepareSensor();*/

//private boolean isDeviceLockScreenIsProtected(Context context) {
// return keyguardManager.isKeyguardSecure();
//}


    /*private void prepareSensor() {
            cipherInit();
            FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
            if (cryptoObject != null) {
                Toast.makeText(getContext(), "use fingerprint to login", Toast.LENGTH_LONG).show();
                FingerprintHelper mFingerprintHelper = new FingerprintHelper(this.getContext());
                mFingerprintHelper.startAuth(cryptoObject);
            }
    }


    public class FingerprintHelper extends FingerprintManagerCompat.AuthenticationCallback {
        private Context mContext;
        private CancellationSignal mCancellationSignal;

        FingerprintHelper(Context context) {
            mContext = context;
        }

        void startAuth(FingerprintManagerCompat.CryptoObject cryptoObject) {
            mCancellationSignal = new CancellationSignal();
            FingerprintManagerCompat manager = FingerprintManagerCompat.from(mContext);
            manager.authenticate(cryptoObject, 0, mCancellationSignal, this, null);
        }

        void cancel() {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Toast.makeText(mContext, errString, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Toast.makeText(mContext, helpString, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            Cipher cipher = result.getCryptoObject().getCipher();
            Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationFailed() {
            Toast.makeText(mContext, "try again", Toast.LENGTH_SHORT).show();
        }

    }*/

    /*

    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey("key_name",
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }*/


