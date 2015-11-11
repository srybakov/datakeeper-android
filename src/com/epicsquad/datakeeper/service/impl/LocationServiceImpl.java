package com.epicsquad.datakeeper.service.impl;

import android.content.Context;
import com.epicsquad.datakeeper.common.CommonExceptionHandler;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.model.LocationDao;
import com.epicsquad.datakeeper.model.domain.Location;
import com.epicsquad.datakeeper.model.impl.LocationDaoImpl;
import com.epicsquad.datakeeper.service.LocationService;
import com.epicsquad.datakeeper.service.SenderService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LocationServiceImpl implements LocationService {

    public LocationServiceImpl() {
        Thread.setDefaultUncaughtExceptionHandler(new CommonExceptionHandler());
    }

    private LocationDao locationDao = new LocationDaoImpl();
    private SenderService senderService = new SenderServiceImpl();

    @Override
    public void save(Location location) {
        locationDao.save(location);
    }

    @Override
    public Set<Location> findAll() {
        return locationDao.findAll();
    }

    @Override
    public void deleteSentLocations() throws IOException {
        Set<String> locationIds = locationDao.findAllIds();
        Set<String> sentLocations = new HashSet<String>();
        for (String id : locationIds){
            if (locationDao.isSentToServer(id)){
                sentLocations.add(id);
            }
        }
        for (String id : sentLocations){
            locationDao.delete(id);
        }
    }

    @Override
    public void sendDataToServer(Context context) throws IOException {
        if (Utils.isOnline(context)){
            Set<Location> toSend = findLocationsToSend();
            for (Location location : toSend){
                senderService.sendLocation(location);
                locationDao.markAsSent(location);
            }
        }
    }

    private Set<Location> findLocationsToSend(){
        Set<String> allLocationsId = locationDao.findAllIds();
        Set<Location> toSend = new HashSet<Location>();
        for (String locationId : allLocationsId){
            if (!locationDao.isSentToServer(locationId)){
                toSend.add(locationDao.find(locationId));
            }
        }
        return toSend;
    }
}
