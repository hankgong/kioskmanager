package com.avi.kioskmanager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by hgong on 04/05/15.
 */
public class BootupService extends Service{
    SharedPreferences mPr;
    PackageManager mPm;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Bootup Service Stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }



//    @Override
//    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
//        mPr = PreferenceManager.getDefaultSharedPreferences(this);
//        mPm = getBaseContext().getPackageManager();
//        Toast.makeText(this, "Bootup Service Started", Toast.LENGTH_LONG).show();
//
//        if (mPr.getBoolean("system_bar", true) == false) {
//
//            try {
//                Process proc = Runtime.getRuntime().exec(new String[]{
//                        "su","-c","service call activity 42 s16 com.android.systemui"});
//                proc.waitFor();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (!mPr.getString("startup", "None").equals("None")) {
////            Intent intent = new Intent(getBaseContext(), com.example.myapplication2.app)
//            Intent newIntent = new Intent(mPm.getLaunchIntentForPackage(mPr.getString("startup", "None")));
//            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(newIntent);
//
//        }
//
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPr = PreferenceManager.getDefaultSharedPreferences(this);
        mPm = getBaseContext().getPackageManager();
        Toast.makeText(this, "Bootup Service Started", Toast.LENGTH_LONG).show();

        //If setting is disable system bar, let's do it. Rooted Android is required
        if (mPr.getBoolean("system_bar", true) == false) {

            try {
                Process proc = Runtime.getRuntime().exec(new String[]{
                        "su","-c","service call activity 42 s16 com.android.systemui"});
                proc.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Start the default activity of the chosen package
        if (!mPr.getString("startup", "None").equals("None")) {
            Intent newIntent = new Intent(mPm.getLaunchIntentForPackage(mPr.getString("startup", "None")));
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);

        }

        return super.onStartCommand(intent, flags, startId);
    }
}
