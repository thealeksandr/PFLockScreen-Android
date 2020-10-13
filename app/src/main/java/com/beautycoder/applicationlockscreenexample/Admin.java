package com.beautycoder.applicationlockscreenexample;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Admin extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        Toast.makeText(context, "Enabled Permission", Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "Disabled Permission", Toast.LENGTH_SHORT).show();
        super.onDisabled(context, intent);
    }
}
