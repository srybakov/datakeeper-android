package com.epicsquad.datakeeper.service;

import com.epicsquad.datakeeper.model.domain.Call;
import com.epicsquad.datakeeper.model.domain.Location;
import com.epicsquad.datakeeper.model.domain.SMS;

import java.io.IOException;

public interface SenderService {

    void sendCall(Call call) throws IOException;

    void sendLocation(Location location) throws IOException;

    void sendSMS(SMS sms) throws IOException;
}
