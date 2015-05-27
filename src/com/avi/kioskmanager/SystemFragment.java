package com.avi.kioskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyStore;

/**
 * Created by hgong on 01/05/15.
 */
public class SystemFragment extends PreferenceFragment {

    SharedPreferences.Editor mPe;
    SharedPreferences mPr;
    PackageManager mPm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPm = getActivity().getPackageManager();
        mPr = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mPe = mPr.edit();

        addPreferencesFromResource(R.xml.system_fragment);

        final SwitchPreference systembarSwitchPreference = (SwitchPreference)findPreference("SystemBar");


//        systembarSwitchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//
//                Log.i("ssystem:", "clicked.................");
//                systembarSwitchPreference.setChecked(mPr.getBoolean("system_bar", false));
//
//                return false;
//            }
//        });

        systembarSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                Log.i("system bar", ".........");
                if(systembarSwitchPreference.isChecked()) {
//                    Log.i("System bar", "turn on");
                    mPe.putBoolean("system_bar", true);
                    mPe.commit();
                    try {
                        Runtime rt = Runtime.getRuntime();
                        Process proc = rt.exec(
//                                "su","-c", "am startservice -n com.android.systemui/.SystemUIService"});
//                                "su -c /system/bin/am 744 startservice -n com.android.systemui/.SystemUIService");
                                "su");
                        rt.exec("su");
                        proc = rt.exec("/system/bin/am startservice -n com.android.systemui/.SystemUIService");

                        BufferedReader in = new BufferedReader((new InputStreamReader(proc.getInputStream())));
                        int read;
                        char[] buffer = new char[4096];
                        StringBuffer output = new StringBuffer();
                        while ((read = in.read(buffer)) > 0) {
                            output.append(buffer, 0, read);
                        }

                        System.out.print("turning..." + output);

                        proc.waitFor();


                        proc.destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
//                    Log.i("System bar", "turn off");
                    mPe.putBoolean("system_bar", false);
                    mPe.commit();

                    try {
//                        Log.i("System bar", "turn off");
                        Runtime rt = Runtime.getRuntime();
                        Process proc = rt.exec(

//                                Process proc = Runtime.getRuntime().exec(new String[]{
//                                "su","-c","service call activity 42 s16 com.android.systemui"});
//                                "/system/bin/service 744 call activity 42 s16 com.android.systemui"});
                                        "su");
                        rt.exec("su");
                        proc = rt.exec("/system/bin/service call activity 42 s16 com.android.systemui");

                        BufferedReader in = new BufferedReader((new InputStreamReader(proc.getInputStream())));
                        int read;
                        char[] buffer = new char[4096];
                        StringBuffer output = new StringBuffer();
                        while ((read = in.read(buffer)) > 0) {
                            output.append(buffer, 0, read);
                        }

                        System.out.print("turning..." + output);

//                        proc.waitFor();


//                        proc.destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

    }
}
