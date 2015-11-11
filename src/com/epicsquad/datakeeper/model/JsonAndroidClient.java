package com.epicsquad.datakeeper.model;

import java.io.Serializable;

public class JsonAndroidClient implements Serializable {

    private String userEmail;
    private String model;
    private String imei;

    public JsonAndroidClient(String userEmail, String model, String imei) {
        this.userEmail = userEmail;
        this.model = model;
        this.imei = imei;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
