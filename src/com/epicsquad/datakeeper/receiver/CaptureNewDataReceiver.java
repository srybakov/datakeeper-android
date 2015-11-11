package com.epicsquad.datakeeper.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import com.epicsquad.datakeeper.ApplicationService;
import com.epicsquad.datakeeper.common.ThreadExecutor;
import com.epicsquad.datakeeper.common.Utils;

public class CaptureNewDataReceiver extends BroadcastReceiver {

    private static final String RECEIVER_CAPTURE_NEW_DATA = "receiverCaptureNewData";
    private static final long EXECUTE_PERIOD = DateUtils.MINUTE_IN_MILLIS * 10;

    @Override
    public void onReceive(Context context, Intent intent) {
        ThreadExecutor.getInstance().runTask(new StartServiceTask(context));
    }

    public static void setAlarm(final Context context) {
        final AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, CaptureNewDataReceiver.class);
        intent.setAction(RECEIVER_CAPTURE_NEW_DATA);
        final PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), EXECUTE_PERIOD, pi);
    }

    public static void cancelAlarm(final Context context) {
        final Intent intent = new Intent(context, CaptureNewDataReceiver.class);
        intent.setAction(RECEIVER_CAPTURE_NEW_DATA);
        final PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private class StartServiceTask implements Runnable {

        private final Context context;

        private StartServiceTask(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            try {
                context.stopService(new Intent(context, ApplicationService.class));
                context.startService(new Intent(context, ApplicationService.class));
            } catch (Exception e) {
                Utils.processException(e, CaptureNewDataReceiver.class, " Cannot start ApplicationService");
            }
        }
    }
}
