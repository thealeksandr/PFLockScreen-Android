package com.beautycoder.pflockscreen.security;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

/**
 * Created by Aleksandr Nikiforov on 2018/02/07.
 *
 * Class to work with AndroidKeyStore.
 */
public class PFSecurityUtils {

    private static final PFSecurityUtils ourInstance = new PFSecurityUtils();

    static public PFSecurityUtils getInstance() {
        return ourInstance;
    }

    private PFSecurityUtils() {

    }

    /**
     * Load AndroidKeyStore.
     * @return true if keystore loaded successfully
     */
    private KeyStore loadKeyStore() throws PFSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return keyStore;
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | CertificateException
                | IOException e) {
            e.printStackTrace();
            throw new PFSecurityException("Can not load keystore:" + e.getMessage());
        }
    }

    public String encode(String alias, String input, boolean isAuthorizationRequared)
            throws PFSecurityException {
        try {
            Cipher cipher = getEncodeCipher(alias, isAuthorizationRequared);
            byte[] bytes = cipher.doFinal(input.getBytes());
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new PFSecurityException("Error while encoding : " + e.getMessage());
        }
    }

    // More information about this hack
    // from https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.html
    // from https://code.google.com/p/android/issues/detail?id=197719
    private Cipher getEncodeCipher(String alias, boolean isAuthenticationRequired)
            throws PFSecurityException {
            Cipher cipher = getCipherInstance();
        KeyStore keyStore = loadKeyStore();
        generateKeyIfNecessary(keyStore, alias, isAuthenticationRequired);
        initEncodeCipher(cipher, alias, keyStore);
        return cipher;

    }

    private boolean generateKeyIfNecessary(@NonNull KeyStore keyStore, String alias,
                           boolean isAuthenticationRequired) {
        try {
            return keyStore.containsAlias(alias) || generateKey(alias, isAuthenticationRequired);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return false;

    }

    public String decode(String encodedString, Cipher cipher) throws PFSecurityException  {
        try {
            byte[] bytes = Base64.decode(encodedString, Base64.NO_WRAP);
            return new String(cipher.doFinal(bytes));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw  new PFSecurityException("Error while decoding: " + e.getMessage());
        }
    }

    public String decode(String alias, String encodedString) throws PFSecurityException  {
        try {
            Cipher cipher = getCipherInstance();
            initDecodeCipher(cipher, alias);
            byte[] bytes = Base64.decode(encodedString, Base64.NO_WRAP);
            return new String(cipher.doFinal(bytes));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw  new PFSecurityException("Error while decoding: " + e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean generateKey(String keystoreAlias, boolean isAuthenticationRequired)  {
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
            keyGenerator.initialize(
                    new KeyGenParameterSpec.Builder(keystoreAlias,
                            KeyProperties.PURPOSE_ENCRYPT |
                                    KeyProperties.PURPOSE_DECRYPT)
                            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                            .setUserAuthenticationRequired(isAuthenticationRequired)
                            .build());
            keyGenerator.generateKeyPair();
            return true;

        } catch ( NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException exc) {
            exc.printStackTrace();
            return false;
        }
    }


    private Cipher getCipherInstance() throws PFSecurityException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new PFSecurityException("Can not get instance of Cipher object" + e.getMessage());
        }
    }


    private void initDecodeCipher(Cipher cipher, String alias) throws PFSecurityException {
        try {
            KeyStore keyStore = loadKeyStore();
            PrivateKey key  = (PrivateKey) keyStore.getKey(alias, null);
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException
                | InvalidKeyException e) {
            e.printStackTrace();
            throw  new PFSecurityException("Error while decoding: " + e.getMessage());
        }

    }



    private void initEncodeCipher(Cipher cipher, String alias, KeyStore keyStore)
            throws PFSecurityException {
        try {
            PublicKey key = keyStore.getCertificate(alias).getPublicKey();
            PublicKey unrestricted = KeyFactory.getInstance(key.getAlgorithm()).generatePublic(
                    new X509EncodedKeySpec(key.getEncoded()));
            OAEPParameterSpec spec = new OAEPParameterSpec("SHA-256", "MGF1",
                    MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
            cipher.init(Cipher.ENCRYPT_MODE, unrestricted, spec);
        } catch (KeyStoreException | InvalidKeySpecException |
                NoSuchAlgorithmException | InvalidKeyException |
                InvalidAlgorithmParameterException e) {
            throw new PFSecurityException("Can not initialize Encode Cipher:" + e.getMessage());
        }
    }

    public boolean isKeystoreContainAlias(String alias) throws PFSecurityException {
        KeyStore keyStore = loadKeyStore();
        try {
            return keyStore.containsAlias(alias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new PFSecurityException(e.getMessage());
        }
    }


    /**
     * Delete key from KeyStore.
     * @param alias KeyStore alias.
     * @throws PFSecurityException throw Exception if something went wrong.
     */
    public void deleteKey(String alias) throws PFSecurityException {
        KeyStore keyStore = loadKeyStore();
        try {
            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new PFSecurityException("Can not delete key: " + e.getMessage());
        }
    }

    /*public  FingerprintManagerCompat.CryptoObject getCryptoObject() {
        if (prepare() && initCipher(Cipher.ENCRYPT_MODE)) {
            return new FingerprintManagerCompat.CryptoObject(mCipher);
        }
        return null;
    }*/
}
