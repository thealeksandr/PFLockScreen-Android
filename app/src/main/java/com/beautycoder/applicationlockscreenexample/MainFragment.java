package com.beautycoder.applicationlockscreenexample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aleksandr on 2018/02/14.
 */

public class MainFragment extends Fragment {
    ComponentName componentName;
    DevicePolicyManager devicePolicyManager;
    Button lock, enable;
    public static final int RESULT_ENABLE = 11;
    private ActivityManager activityManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        devicePolicyManager = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        componentName = new ComponentName(getActivity(), Admin.class);

        lock = view.findViewById(R.id.lock);
        enable = view.findViewById(R.id.enable);

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean active = devicePolicyManager.isAdminActive(componentName);
                if (active) {
                    startActivity(new Intent(getContext(), MainActivity.class));
                    devicePolicyManager.lockNow();
                } else {
                    Toast.makeText(getContext(), "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
                }
            }
        });

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Required Permission");
                startActivityForResult(intent, RESULT_ENABLE);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(componentName);
        enable.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == getActivity().RESULT_OK) {
                    Toast.makeText(getContext(), "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else if (resultCode == getActivity().RESULT_FIRST_USER) {
                    Toast.makeText(getContext(), "Enabled Permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
