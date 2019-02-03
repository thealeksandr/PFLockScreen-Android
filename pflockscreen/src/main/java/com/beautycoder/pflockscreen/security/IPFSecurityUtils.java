package com.beautycoder.pflockscreen.security;

import android.content.Context;
import androidx.annotation.NonNull;

public interface IPFSecurityUtils {

    String encode(@NonNull Context context, String alias, String input, boolean isAuthorizationRequared)
            throws PFSecurityException ;

    String decode(String alias, String encodedString) throws PFSecurityException;

    boolean isKeystoreContainAlias(String alias) throws PFSecurityException;

    void deleteKey(String alias) throws PFSecurityException;

}
