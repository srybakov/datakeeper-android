package com.epicsquad.datakeeper.model.domain;

import java.io.Serializable;
import java.util.List;

public class Contact implements Serializable{

    private static final long serialVersionUID = 1L;

    public Contact() {
    }

    private String name;
    private List<String> number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNumber() {
        return number;
    }

    public void setNumber(List<String> number) {
        this.number = number;
    }
}
