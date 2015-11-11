package com.epicsquad.datakeeper;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.observer.CallObserver;
import com.epicsquad.datakeeper.observer.SMSObserver;
import com.epicsquad.datakeeper.receiver.LocationUpdateReceiver;

public class ApplicationService extends Service {

    private static final int GPS_MINIMUM_TIME_BETWEEN_UPDATES = 5 * 60 * 1000;
    private static final int GPS_MINIMUM_DISTANCE_CHANGE_FOR_UPDATES_METERS = 10;

    private static final int NETWORK_MINIMUM_TIME_BETWEEN_UPDATES = 1 * 60 * 1000;
    private static final int NETWORK_MINIMUM_DISTANCE_CHANGE_FOR_UPDATES_METERS = 10;

    private ContentResolver contentResolver = null;
    private LocationManager locationManager = null;

    private SMSObserver smsObserver = null;
    private CallObserver callObserver = null;
    private LocationUpdateReceiver locationUpdateReceiver = null;

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        doWork();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    void doWork() {
        if (contentResolver == null){
            contentResolver = getApplicationContext().getContentResolver();
        }

        if (locationManager == null){
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }

        if (smsObserver == null){
            smsObserver = new SMSObserver(new Handler(), getApplicationContext());
        } else {
            contentResolver.unregisterContentObserver(smsObserver);
        }

        if (callObserver == null){
            callObserver = new CallObserver(new Handler(), getApplicationContext());
        } else {
            contentResolver.unregisterContentObserver(callObserver);
        }

        if (locationUpdateReceiver == null){
            locationUpdateReceiver = new LocationUpdateReceiver();
        } else {
            locationManager.removeUpdates(locationUpdateReceiver);
        }

        contentResolver.registerContentObserver(Uri.parse(Utils.SMS_URI_PATH), true, smsObserver);
        contentResolver.registerContentObserver(Uri.parse(Utils.CALL_LOG_URI_PATH), true, callObserver);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_MINIMUM_TIME_BETWEEN_UPDATES,
                GPS_MINIMUM_DISTANCE_CHANGE_FOR_UPDATES_METERS, locationUpdateReceiver);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, NETWORK_MINIMUM_TIME_BETWEEN_UPDATES,
                NETWORK_MINIMUM_DISTANCE_CHANGE_FOR_UPDATES_METERS, locationUpdateReceiver);
    }
}
