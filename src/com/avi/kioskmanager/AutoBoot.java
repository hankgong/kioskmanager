package com.avi.kioskmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hgong on 04/05/15.
 */
public class AutoBoot extends BroadcastReceiver {
    public AutoBoot() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            System.out.println("Called after booting");
            context.startService(new Intent(context, BootupService.class));
        }
    }
}
