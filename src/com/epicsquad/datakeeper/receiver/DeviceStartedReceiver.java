package com.epicsquad.datakeeper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.epicsquad.datakeeper.common.Utils;

public class DeviceStartedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.setupApplication(context);
    }

}
