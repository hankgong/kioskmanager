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
        mPe = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        mPr = PreferenceManager.getDefaultSharedPreferences(getActivity());

        addPreferencesFromResource(R.xml.system_fragment);

        final SwitchPreference systembarSwitchPreference = (SwitchPreference)findPreference("SystemBar");

        Log.i("Switch:", " " +systembarSwitchPreference.getSwitchTextOn());




        systembarSwitchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                systembarSwitchPreference.setChecked(mPr.getBoolean("system_bar", false));

                return false;
            }
        });

        systembarSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.i("system bar", ".........");
                if(systembarSwitchPreference.isChecked()) {
                    Log.i("System bar", "turn on");
                    mPe.putBoolean("system_bar", true);
                    mPe.commit();
                    try {
                        Log.i("System bar", "turn on");
//                        Process proc = Runtime.getRuntime().exec(new String[]{
//                                "am","startservice","-n","com.android.systemui/.SystemUIService"});
                        Process proc = Runtime.getRuntime().exec(new String[]{
                                "su","-c", "am startservice -n com.android.systemui/.SystemUIService"});
                        proc.waitFor();
                        proc.destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.i("System bar", "turn off");
                    mPe.putBoolean("system_bar", false);
                    mPe.commit();

                    try {
                        Log.i("System bar", "turn off");
                        Process proc = Runtime.getRuntime().exec(new String[]{
                                "su","-c","service call activity 42 s16 com.android.systemui"});
//                        proc.waitFor();
//                        Process proc = Runtime.getRuntime().exec(new String[]{
//                                "am","startservice","-n","com.android.systemui/.SystemUIService"});
                        proc.waitFor();
                        proc.destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

    }
}
