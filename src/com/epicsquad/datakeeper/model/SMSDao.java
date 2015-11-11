package com.epicsquad.datakeeper.model;

import com.epicsquad.datakeeper.model.domain.SMS;

import java.io.IOException;
import java.util.Set;

public interface SMSDao {

    void save(SMS sms);

    SMS find(String id);

    Set<SMS> findAll();

    Set<String> findAllIds();

    boolean isSentToServer(String id);

    void delete(String id) throws IOException;

    void markAsSent(SMS sms) throws IOException;

}
