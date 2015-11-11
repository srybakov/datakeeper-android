package com.epicsquad.datakeeper.model;

import com.epicsquad.datakeeper.model.domain.Call;

import java.io.IOException;
import java.util.Set;

public interface CallDao {

    void save(Call call);

    Call find(String id);

    Set<Call> findAll();

    Set<String> findAllIds();

    boolean isSentToServer(String id);

    void delete(String id) throws IOException;

    void markAsSent(Call call) throws IOException;

}
