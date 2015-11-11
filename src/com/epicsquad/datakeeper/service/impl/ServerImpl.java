package com.epicsquad.datakeeper.service.impl;

import com.epicsquad.datakeeper.model.JsonAndroidClient;
import com.epicsquad.datakeeper.service.Server;
import com.google.gson.Gson;

public class ServerImpl implements Server {

    @Override
    public boolean registerAndroidClient(String userEmail, String mobileModel, String imei) {
        JsonAndroidClient jsonAndroidClient = new JsonAndroidClient(userEmail, mobileModel, imei);
        String json = new Gson().toJson(jsonAndroidClient);

        return true;
    }
}
