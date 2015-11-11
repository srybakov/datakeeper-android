package com.epicsquad.datakeeper.service;

import android.content.Context;
import com.epicsquad.datakeeper.model.domain.SMS;

import java.io.IOException;
import java.util.Set;

public interface SMSService {

    void save(SMS sms);

    Set<SMS> findAll();

    void deleteSentSMSes() throws IOException;

    void sendDataToServer(Context context) throws IOException;

}
