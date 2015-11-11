package com.epicsquad.datakeeper.model;

import com.epicsquad.datakeeper.model.domain.Contact;

import java.util.List;

public interface ContactDao {

    List<Contact> findAll();

}
