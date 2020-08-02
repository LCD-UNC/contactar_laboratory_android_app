package com.rfdetke.digitriadlaboratory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, ExperimentService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}
