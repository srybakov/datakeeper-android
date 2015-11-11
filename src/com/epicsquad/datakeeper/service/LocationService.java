package com.epicsquad.datakeeper.service;

import android.content.Context;
import com.epicsquad.datakeeper.model.domain.Location;

import java.io.IOException;
import java.util.Set;

public interface LocationService {

    void save(Location location);

    Set<Location> findAll();

    void deleteSentLocations() throws IOException;

    void sendDataToServer(Context context) throws IOException;

}
