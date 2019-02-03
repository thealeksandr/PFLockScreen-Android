package com.beautycoder.pflockscreen.security;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

public class PFSecurityUtilsOld implements IPFSecurityUtils {

    private static final PFSecurityUtilsOld ourInstance = new PFSecurityUtilsOld();

    public static PFSecurityUtilsOld getInstance() {
        return ourInstance;
    }

    private PFSecurityUtilsOld() {

    }

    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    private static final String PROVIDER =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                    ? "AndroidKeyStoreBCWorkaround"
                    : "AndroidOpenSSL";

    /**
     * Load AndroidKeyStore.
     * @return true if keystore loaded successfully
     */
    private KeyStore loadKeyStore() throws PFSecurityException {
        try {
            final KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return keyStore;
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | CertificateException
                | IOException e) {
            e.printStackTrace();
            throw new PFSecurityException(
                    "Can not load keystore:" + e.getMessage(),
                    PFSecurityUtilsErrorCodes.ERROR_LOAD_KEY_STORE
            );
        }
    }

    private boolean generateKeyIfNecessary(
            @NonNull Context context,
            @NonNull KeyStore keyStore,
            String alias,
            boolean isAuthenticationRequired)
    {
        try {
            return keyStore.containsAlias(alias)
                    || generateKey(context, alias, isAuthenticationRequired);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String encode(
            @NonNull Context context,
            String alias,
            String input,
            boolean isAuthorizationRequared
    ) throws PFSecurityException {
        try {
            final byte[] bytes = rsaEncrypt(context, input.getBytes(), alias);
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PFSecurityException(
                    "Error while encoding : " + e.getMessage(),
                    PFSecurityUtilsErrorCodes.ERROR_ENCODING
            );
        }
    }

    private byte[] rsaEncrypt(
            @NonNull Context context,
            byte[] secret,
            String keystoreAlias
    ) throws Exception {
        final KeyStore keyStore = loadKeyStore();
        generateKeyIfNecessary(context, keyStore, keystoreAlias, false);
        final KeyStore.PrivateKeyEntry privateKeyEntry
                = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keystoreAlias, null);
        final Cipher inputCipher = Cipher.getInstance(RSA_MODE, PROVIDER);
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CipherOutputStream cipherOutputStream
                = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();
        final byte[] vals = outputStream.toByteArray();
        return vals;
    }

    @Override
    public String decode(
            String alias,
            String encodedString
    ) throws PFSecurityException {
        try {
            final byte[] bytes = Base64.decode(encodedString, Base64.NO_WRAP);
            return new String(rsaDecrypt(bytes, alias));
        } catch (Exception e) {
            e.printStackTrace();
            throw  new PFSecurityException(
                    "Error while decoding: " + e.getMessage(),
                    PFSecurityUtilsErrorCodes.ERROR_DEENCODING
            );
        }
    }

    private  byte[] rsaDecrypt(
            byte[] encrypted,
            String keystoreAlias
    ) throws Exception {
        final KeyStore keyStore = loadKeyStore();
        final KeyStore.PrivateKeyEntry privateKeyEntry =
                (KeyStore.PrivateKeyEntry)keyStore.getEntry(keystoreAlias, null);
        final Cipher output = Cipher.getInstance(RSA_MODE, PROVIDER);
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        final CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(encrypted), output);
        final ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte)nextByte);
        }
        final byte[] bytes = new byte[values.size()];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i).byteValue();
        }
        return bytes;
    }

    private boolean generateKey(
            Context context,
            String keystoreAlias,
            boolean isAuthenticationRequired
    ) {
        return generateKeyOld(context, keystoreAlias, isAuthenticationRequired);
    }

    private boolean generateKeyOld(
            Context context,
            String keystoreAlias,
            boolean isAuthenticationRequired
    ) {
        try {
            final Calendar start = Calendar.getInstance();
            final Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 25);

            final KeyPairGenerator keyGen = KeyPairGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");

            final KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(keystoreAlias)
                    .setSubject(new X500Principal("CN=" + keystoreAlias))
                    .setSerialNumber(BigInteger.valueOf(Math.abs(keystoreAlias.hashCode())))
                    .setEndDate(end.getTime())
                    .setStartDate(start.getTime())
                    .setSerialNumber(BigInteger.ONE)
                    .setSubject(new X500Principal(
                            "CN = Secured Preference Store, O = Devliving Online")
                    )
                    .build();

            keyGen.initialize(spec);
            keyGen.generateKeyPair();
            return true;

        } catch ( NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException exc) {
            exc.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isKeystoreContainAlias(String alias) throws PFSecurityException {
        final KeyStore keyStore = loadKeyStore();
        try {
            return keyStore.containsAlias(alias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new PFSecurityException(
                    e.getMessage(),
                    PFSecurityUtilsErrorCodes.ERROR_KEY_STORE
            );
        }
    }

    /**
     * Delete key from KeyStore.
     * @param alias KeyStore alias.
     * @throws PFSecurityException throw Exception if something went wrong.
     */
    @Override
    public void deleteKey(String alias) throws PFSecurityException {
        final KeyStore keyStore = loadKeyStore();
        try {
            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new PFSecurityException(
                    "Can not delete key: " + e.getMessage(),
                    PFSecurityUtilsErrorCodes.ERROR_DELETE_KEY
            );
        }
    }

}
