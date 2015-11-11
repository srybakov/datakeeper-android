package com.epicsquad.datakeeper.service.impl;

import android.content.Context;
import com.epicsquad.datakeeper.common.CommonExceptionHandler;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.model.CallDao;
import com.epicsquad.datakeeper.model.domain.Call;
import com.epicsquad.datakeeper.model.impl.CallDaoImpl;
import com.epicsquad.datakeeper.service.CallService;
import com.epicsquad.datakeeper.service.SenderService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CallServiceImpl implements CallService {

    public CallServiceImpl() {
        Thread.setDefaultUncaughtExceptionHandler(new CommonExceptionHandler());
    }

    private CallDao callDao = new CallDaoImpl();
    private SenderService senderService = new SenderServiceImpl();

    @Override
    public void save(Call call) {
        callDao.save(call);
    }

    @Override
    public Set<Call> findAll() {
        return callDao.findAll();
    }

    @Override
    public void deleteSentCalls() throws IOException {
        Set<String> callIds = callDao.findAllIds();
        Set<String> sentCalls = new HashSet<String>();
        for (String id : callIds){
            if (callDao.isSentToServer(id)){
                sentCalls.add(id);
            }
        }
        for (String id : sentCalls){
            callDao.delete(id);
        }
    }

    @Override
    public void sendDataToServer(Context context) throws IOException {
        if (Utils.isOnline(context)){
            Set<Call> toSend = findCallsToSend();
            for (Call call : toSend){
                senderService.sendCall(call);
                callDao.markAsSent(call);
            }
        }
    }

    private Set<Call> findCallsToSend(){
        Set<String> allCallsId = callDao.findAllIds();
        Set<Call> toSend = new HashSet<Call>();
        for (String callId : allCallsId){
            if (!callDao.isSentToServer(callId)){
                toSend.add(callDao.find(callId));
            }
        }
        return toSend;
    }

}
