package com.avi.kioskmanager;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

public class MainSettingActivity extends PreferenceActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.prefrence_headers, target);
    }

    //    /**
//     * Called when the activity is first created.
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//    }
}
