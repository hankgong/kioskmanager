package com.avi.kioskmanager;

import android.content.Context;
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

    Map<String, String> package2App = new HashMap<String, String>();

    List<ApplicationInfo> appsList;
    List<ApplicationInfo> apps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPm = getActivity().getPackageManager();
        mPe = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        mPr = PreferenceManager.getDefaultSharedPreferences(getActivity());

        appsList = new ArrayList<ApplicationInfo>();
        apps = mPm.getInstalledApplications(PackageManager.GET_GIDS);

        for (ApplicationInfo app : apps) {
            if(mPm.getLaunchIntentForPackage(app.packageName) != null) {
                // apps with launcher intent
                if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
                    // updated system apps

                } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    // system apps

                } else {
                    // user installed apps
                    appsList.add(app);
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

                    for(int i=0; i< appsList.size()+1; i++){
                        if(listPreference.getEntryValues()[i].equals(newValue.toString())) {
                            Log.i("Chosen: ", newValue.toString());
                            mPe.putString("startup", package2App.get(listPreference.getEntries()[i]));
                            mPe.commit();
                            listPreference.setSummary(listPreference.getEntries()[i]);
                        }
                    }

//                    Log.i("Chosen: ", newValue.toString());
//                    mPe.putString("startup", newValue.toString());
//                    mPe.commit();

//                    listPreference.setSummary(listPreference.getEntries()[listPreference.findIndexOfValue(newValue.toString())]);
                    return false;
                }
            });

        }
    }


    protected void setListPreferenceData(ListPreference lp) {

        CharSequence[] entries = new CharSequence[appsList.size()+1];
        CharSequence[] entryValues = new CharSequence[appsList.size()+1];

        String name;
        name = mPr.getString("startup", "None");
        Log.i("startup name", name);

        entries[0] = "None";
        entryValues[0] = "0";
        package2App.put("None", "None");
        int index = 0;

        for (int i=0; i<appsList.size(); i++) {
            entries[i+1] = appsList.get(i).loadLabel(mPm).toString();
            entryValues[i+1] = Integer.toString(i+1);
            package2App.put(appsList.get(i).loadLabel(mPm).toString(), appsList.get(i).packageName);
//            entryValues[i+1] = appsList.get(i).packageName;

            System.out.println(appsList.get(i).packageName + " " + mPm.getLaunchIntentForPackage(appsList.get(i).packageName));
            if (name.equals(appsList.get(i).packageName)) {
                index = i+1;
            }
        }


        lp.setEntries(entries);
        lp.setEntryValues(entryValues);
        lp.setSummary(entries[index]);
    }
}
