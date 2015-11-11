package com.epicsquad.datakeeper.model.domain;

public enum SMSType {

    INCOMING("Incoming"),
    OUTGOING("Outgoing");

    private String name;


    SMSType(String name) {
        this.name = name;
    }
}
