package com.epicsquad.datakeeper.model;

import com.epicsquad.datakeeper.model.domain.Location;

import java.io.IOException;
import java.util.Set;

public interface LocationDao {

    void save(Location location);

    Location find(String id);

    Set<Location> findAll();

    Set<String> findAllIds();

    boolean isSentToServer(String id);

    void delete(String id) throws IOException;

    void markAsSent(Location location) throws IOException;

}
