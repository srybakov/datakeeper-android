package com.epicsquad.datakeeper.service;

import android.content.Context;
import com.epicsquad.datakeeper.model.domain.Call;

import java.io.IOException;
import java.util.Set;

public interface CallService {

    void save(Call call);

    Set<Call> findAll();

    void deleteSentCalls() throws IOException;

    void sendDataToServer(Context context) throws IOException;

}
