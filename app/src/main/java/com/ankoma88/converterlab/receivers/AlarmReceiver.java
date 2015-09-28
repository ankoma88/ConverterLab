package com.ankoma88.converterlab.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.ankoma88.converterlab.services.UpdateService;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService UpdateService to load data
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static final String TAG = AlarmReceiver.class.getSimpleName();

    private AlarmManager mAlarmManager;
    private PendingIntent mAlarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, UpdateService.class);
        startWakefulService(context, service);
        Log.i(TAG, "Alarm intent received");
    }

    /**
     * Sets a repeating alarm that runs every 30 minutes (inexact for consuming less battery).
     * When the alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     */
    public void setAlarm(Context context) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        mAlarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR,
                mAlarmIntent);

        setupRestartAlarmOnBoot(context, PackageManager.COMPONENT_ENABLED_STATE_ENABLED);

        Log.i(TAG, "Alarm started");
    }

    private void setupRestartAlarmOnBoot(Context context, int componentEnabledStateDisabled) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                componentEnabledStateDisabled,
                PackageManager.DONT_KILL_APP);
    }
}
