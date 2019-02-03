package com.beautycoder.pflockscreen;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.beautycoder.pflockscreen.security.PFFingerprintPinCodeHelper;
import com.beautycoder.pflockscreen.security.PFResult;
import com.beautycoder.pflockscreen.security.PFSecurityUtils;
import com.beautycoder.pflockscreen.security.PFSecurityUtilsOld;
import com.beautycoder.pflockscreen.security.callbacks.PFPinCodeHelperCallback;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by aleksandr on 2018/02/26.
 */
@RunWith(AndroidJUnit4.class)
public class PFSecurityUtilsTest {

    @Test
    public void pfSecurityUtils() throws Exception {
        // Context of the app under test.
        final String alias = "test_alias";
        final String pinCode = "1234";

        final Context appContext = InstrumentationRegistry.getTargetContext();

        PFSecurityUtils.getInstance().deleteKey(alias);

        final boolean isAliasFalse = PFSecurityUtils.getInstance().isKeystoreContainAlias(alias);
        assertFalse(isAliasFalse);

        final String encoded = PFSecurityUtils.getInstance().encode(null, alias, pinCode, false);
        assertNotNull(encoded);

        final boolean isAliasTrue = PFSecurityUtils.getInstance().isKeystoreContainAlias(alias);
        assertTrue(isAliasTrue);

        final String decoded = PFSecurityUtils.getInstance().decode(alias, encoded);
        assertEquals(decoded, pinCode);

        PFSecurityUtils.getInstance().deleteKey(alias);
        final boolean isAliasFalse2 = PFSecurityUtils.getInstance().isKeystoreContainAlias(alias);
        assertFalse(isAliasFalse2);
    }


    @Test
    public void pfSecurityUtilsOld() throws Exception {
        // Context of the app under test.
        final String alias = "test_alias_old";
        final String pinCode = "1234";

        final Context appContext = InstrumentationRegistry.getTargetContext();

        PFSecurityUtilsOld.getInstance().deleteKey(alias);

        final boolean isAliasFalse = PFSecurityUtilsOld.getInstance().isKeystoreContainAlias(alias);
        assertFalse(isAliasFalse);

        final String encoded = PFSecurityUtilsOld.getInstance().encode(appContext, alias, pinCode, false);
        assertNotNull(encoded);

        final boolean isAliasTrue = PFSecurityUtilsOld.getInstance().isKeystoreContainAlias(alias);
        assertTrue(isAliasTrue);

        final String decoded = PFSecurityUtilsOld.getInstance().decode(alias, encoded);
        assertEquals(decoded, pinCode);

        PFSecurityUtilsOld.getInstance().deleteKey(alias);
        final boolean isAliasFalse2 = PFSecurityUtilsOld.getInstance().isKeystoreContainAlias(alias);
        assertFalse(isAliasFalse2);
    }

    @Test
    public void pfFingerPrintPinCodeHelper() throws Exception {
        // Context of the app under test.
        final String pinCode = "1234";

        final Context appContext = InstrumentationRegistry.getTargetContext();

        PFFingerprintPinCodeHelper.getInstance().delete(new PFPinCodeHelperCallback<Boolean>() {
            @Override
            public void onResult(PFResult<Boolean> result) {
                assertNull(result.getError());
            }
        });

        PFFingerprintPinCodeHelper.getInstance().isPinCodeEncryptionKeyExist(
                new PFPinCodeHelperCallback<Boolean>() {
                    @Override
                    public void onResult(PFResult<Boolean> result) {
                        assertNull(result.getError());
                        assertFalse(result.getResult());
                    }
                });

        final StringBuilder stringBuilder = new StringBuilder();
        PFFingerprintPinCodeHelper.getInstance().encodePin(appContext, pinCode,
                new PFPinCodeHelperCallback<String>() {
            @Override
            public void onResult(PFResult<String> result) {
                assertNull(result.getError());
                final String encoded = result.getResult();
                stringBuilder.append(encoded);
                assertNotNull(encoded);
            }
        });

        PFFingerprintPinCodeHelper.getInstance().isPinCodeEncryptionKeyExist(
                new PFPinCodeHelperCallback<Boolean>() {
                    @Override
                    public void onResult(PFResult<Boolean> result) {
                        assertNull(result.getError());
                        assertTrue(result.getResult());
                    }
                }
        );

        final String encoded = stringBuilder.toString();
        PFFingerprintPinCodeHelper.getInstance().checkPin(appContext, encoded, pinCode,
                new PFPinCodeHelperCallback<Boolean>() {
            @Override
            public void onResult(PFResult<Boolean> result) {
                assertNull(result.getError());
                assertTrue(result.getResult());
            }
        });


        PFFingerprintPinCodeHelper.getInstance().checkPin(appContext, encoded, "1122",
                new PFPinCodeHelperCallback<Boolean>() {
            @Override
            public void onResult(PFResult<Boolean> result) {
                assertNull(result.getError());
                assertFalse(result.getResult());
            }
        });

        PFFingerprintPinCodeHelper.getInstance().delete(new PFPinCodeHelperCallback<Boolean>() {
            @Override
            public void onResult(PFResult<Boolean> result) {
                assertNull(result.getError());
            }
        });

        PFFingerprintPinCodeHelper.getInstance().isPinCodeEncryptionKeyExist(new PFPinCodeHelperCallback<Boolean>() {
            @Override
            public void onResult(PFResult<Boolean> result) {
                assertNull(result.getError());
                assertFalse(result.getResult());
            }
        });

    }

}
