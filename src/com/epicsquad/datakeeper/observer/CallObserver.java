package com.epicsquad.datakeeper.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.widget.Toast;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.model.domain.Call;
import com.epicsquad.datakeeper.model.domain.CallType;
import com.epicsquad.datakeeper.service.CallService;
import com.epicsquad.datakeeper.service.impl.CallServiceImpl;

public class CallObserver extends ContentObserver{

    private final CallService callService = new CallServiceImpl();
    private final Context context;

    public CallObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        try {
            super.onChange(selfChange);
            Toast.makeText(context, "Call intercepted", Toast.LENGTH_LONG).show();
            Uri allCalls = Uri.parse(Utils.CALL_LOG_URI_PATH);
            Cursor result = context.getContentResolver().query(allCalls, null, null, null, CallLog.Calls.DATE
                    + " DESC LIMIT 1");

            result.moveToFirst();

            String number = result.getString(result.getColumnIndex(CallLog.Calls.NUMBER));
            long duration = result.getLong(result.getColumnIndex(CallLog.Calls.DURATION));
            long date = result.getLong(result.getColumnIndex(CallLog.Calls.DATE));
            int type = Integer.parseInt(result.getString(result.getColumnIndex(CallLog.Calls.TYPE)));
            CallType callType = getCallType(type);

            Call call = new Call();
            call.setTimestamp(date);
            call.setNumber(number);
            call.setDuration(duration);
            call.setType(callType);
            callService.save(call);
        } catch (Exception e){
            Utils.processException(e, CallObserver.class, "onChange");
        }
    }

    private CallType getCallType(int type){
        CallType callType;
        switch (type){
            case CallLog.Calls.INCOMING_TYPE:
                callType = CallType.INCOMING;
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                callType = CallType.OUTGOING;
                break;
            case CallLog.Calls.MISSED_TYPE:
                callType = CallType.MISSED;
                break;
            default:
                callType = CallType.MISSED;
        }
        return callType;
    }
}
