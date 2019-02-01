package com.beautycoder.applicationlockscreenexample;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aleksandr on 2018/02/09.
 */

public class PreferencesSettings {

    private static final String PREF_FILE = "settings_pref";

    static void saveToPref(Context context, String str) {
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("code", str);
        editor.apply();
    }

    static String getCode(Context context) {
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        final String defaultValue = "";
        return sharedPref.getString("code", defaultValue);
    }

}
