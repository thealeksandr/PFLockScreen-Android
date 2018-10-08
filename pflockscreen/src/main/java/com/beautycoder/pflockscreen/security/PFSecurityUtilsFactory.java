package com.beautycoder.pflockscreen.security;

public class PFSecurityUtilsFactory {

    public static IPFSecurityUtils getPFSecurityUtilsInstance() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return PFSecurityUtils.getInstance();
        } else {
            return PFSecurityUtilsOld.getInstance();
        }
    }

}
