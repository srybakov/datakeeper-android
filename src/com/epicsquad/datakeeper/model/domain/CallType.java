package com.epicsquad.datakeeper.model.domain;

public enum CallType{

    INCOMING("Incoming"),
    OUTGOING("Outgoing"),
    MISSED("Missed");

    private String name;

    CallType(String name){
        this.name = name;
    }
}
