package com.contactar.digitriadlaboratory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.contactar.digitriadlaboratory.views.NewRunActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, ExperimentService.class);
        serviceIntent.putExtra(NewRunActivity.EXTRA_RUN_ID, intent.getLongExtra(NewRunActivity.EXTRA_RUN_ID, 0));
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}
