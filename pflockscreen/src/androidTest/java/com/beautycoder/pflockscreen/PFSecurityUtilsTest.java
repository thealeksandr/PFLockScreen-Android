package com.beautycoder.pflockscreen;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.beautycoder.pflockscreen.security.PFSecurityUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by aleksandr on 2018/02/26.
 */
@RunWith(AndroidJUnit4.class)
public class PFSecurityUtilsTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        String alias = "test_alias";
        String pinCode = "1234";

        Context appContext = InstrumentationRegistry.getTargetContext();

        PFSecurityUtils.getInstance().deleteKey(alias);

        boolean isAliasFalse = PFSecurityUtils.getInstance().isKeystoreContainAlias(alias);
        assertFalse(isAliasFalse);

        String encoded = PFSecurityUtils.getInstance().encode(alias, pinCode, false);
        assertNotNull(encoded);

        boolean isAliasTrue = PFSecurityUtils.getInstance().isKeystoreContainAlias(alias);
        assertTrue(isAliasTrue);

        String decoded = PFSecurityUtils.getInstance().decode(alias, encoded);
        assertEquals(decoded, pinCode);

        PFSecurityUtils.getInstance().deleteKey(alias);
        boolean isAliasFalse2 = PFSecurityUtils.getInstance().isKeystoreContainAlias(alias);
        assertFalse(isAliasFalse2);

    }
}
