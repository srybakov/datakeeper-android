package com.epicsquad.datakeeper.service.impl;

import android.content.Context;
import com.epicsquad.datakeeper.common.CommonExceptionHandler;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.model.SMSDao;
import com.epicsquad.datakeeper.model.domain.SMS;
import com.epicsquad.datakeeper.model.impl.SMSDaoImpl;
import com.epicsquad.datakeeper.service.SMSService;
import com.epicsquad.datakeeper.service.SenderService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SMSServiceImpl implements SMSService {

    public SMSServiceImpl() {
        Thread.setDefaultUncaughtExceptionHandler(new CommonExceptionHandler());
    }

    private SMSDao smsDao = new SMSDaoImpl();
    private SenderService senderService = new SenderServiceImpl();

    @Override
    public void save(SMS sms) {
        smsDao.save(sms);
    }

    @Override
    public Set<SMS> findAll() {
        return smsDao.findAll();
    }

    @Override
    public void deleteSentSMSes() throws IOException {
        Set<String> smsesIds = smsDao.findAllIds();
        Set<String> sentSMSes = new HashSet<String>();
        for (String id : smsesIds){
            if (smsDao.isSentToServer(id)){
                sentSMSes.add(id);
            }
        }
        for (String id : sentSMSes){
            smsDao.delete(id);
        }
    }


    @Override
    public void sendDataToServer(Context context) throws IOException {
        if (Utils.isOnline(context)){
            Set<SMS> toSend = findSMSesToSend();
            for (SMS sms : toSend){
                senderService.sendSMS(sms);
                smsDao.markAsSent(sms);
            }
        }
    }

    private Set<SMS> findSMSesToSend(){
        Set<String> allSMSesId = smsDao.findAllIds();
        Set<SMS> toSend = new HashSet<SMS>();
        for (String smsId : allSMSesId){
            if (!smsDao.isSentToServer(smsId)){
                toSend.add(smsDao.find(smsId));
            }
        }
        return toSend;
    }
}
