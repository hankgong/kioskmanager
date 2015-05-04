package com.avi.kioskmanager;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hgong on 01/05/15.
 */
public class StartupFragment extends PreferenceFragment {

    SharedPreferences.Editor mPe;
    SharedPreferences mPr;
    PackageManager mPm;

    //It seems that long string or string with dot can't be used as entryValue, so a hashmap is created
    //List will display appliction name
    //package name will be stored in the preference
    //integer converted to string like "0", "1" will be used for entryValue

    Map<String, String> mPackage2App = new HashMap<String, String>();

    List<ApplicationInfo> mAppList;
    List<ApplicationInfo> mAllApps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPm = getActivity().getPackageManager();
        mPr = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mPe = mPr.edit();

        mAppList = new ArrayList<ApplicationInfo>();
        mAllApps = mPm.getInstalledApplications(PackageManager.GET_GIDS);

        for (ApplicationInfo app : mAllApps) {
            if(mPm.getLaunchIntentForPackage(app.packageName) != null) {
                // apps with launcher intent
                if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
                    // updated system apps

                } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    // system apps

                } else {
                    // user installed apps
                    mAppList.add(app);
                }
            }
        }

        addPreferencesFromResource(R.xml.startup_fragment);

        final ListPreference listPreference = (ListPreference) findPreference("startup");
        setListPreferenceData(listPreference);

        if (listPreference != null) {
            listPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    setListPreferenceData(listPreference);
                    return false;
                }
            });

            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    for(int i=0; i< mAppList.size()+1; i++){
                        if(listPreference.getEntryValues()[i].equals(newValue.toString())) {
                            Log.i("Chosen: ", newValue.toString());
                            mPe.putString("startup", mPackage2App.get(listPreference.getEntries()[i]));
                            mPe.commit();
                            listPreference.setSummary(listPreference.getEntries()[i]);
                        }
                    }

                    return false;
                }
            });

        }
    }


    protected void setListPreferenceData(ListPreference lp) {

        CharSequence[] entries = new CharSequence[mAppList.size()+1];
        CharSequence[] entryValues = new CharSequence[mAppList.size()+1];

        String name;
        name = mPr.getString("startup", "None");
        Log.i("startup name", name);

        entries[0] = "None";
        entryValues[0] = "0";
        mPackage2App.put("None", "None");
        int index = 0;

        for (int i=0; i< mAppList.size(); i++) {
            entries[i+1] = mAppList.get(i).loadLabel(mPm).toString();
            entryValues[i+1] = Integer.toString(i+1);
            mPackage2App.put(mAppList.get(i).loadLabel(mPm).toString(), mAppList.get(i).packageName);

            System.out.println(mAppList.get(i).packageName + " " + mPm.getLaunchIntentForPackage(mAppList.get(i).packageName));
            if (name.equals(mAppList.get(i).packageName)) {
                index = i+1;
            }
        }


        lp.setEntries(entries);
        lp.setEntryValues(entryValues);
        lp.setSummary(entries[index]);
    }
}
