package com.epicsquad.datakeeper.model.impl;

import com.epicsquad.datakeeper.common.CommonExceptionHandler;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.model.LocationDao;
import com.epicsquad.datakeeper.model.domain.Location;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.epicsquad.datakeeper.common.Utils.deleteRecursively;

public class LocationDaoImpl implements LocationDao {

    private static final String SAVED_LOCATION_SUFFIX = ".savedLocation";
    private static final String SENT_LOCATION_SUFFIX = ".sent";

    public LocationDaoImpl() {
        File locationDao = new File(getLocationDaoDirPath());
        if (!locationDao.exists()){
            locationDao.mkdir();
        }
        Thread.setDefaultUncaughtExceptionHandler(new CommonExceptionHandler());
    }

    @Override
    public void save(Location location) {
        PrintWriter out = null;
        try {
            int hashCode = location.hashCode();
            String locationFileDirPath = getLocationDaoDirPath() + "/" + hashCode + "/";
            String locationFilePath = locationFileDirPath + hashCode + SAVED_LOCATION_SUFFIX;
            File locationDir = new File(locationFileDirPath);
            File locationFile = new File(locationFilePath);
            if (!locationDir.exists()){
                locationDir.mkdir();
                Gson gson = new Gson();
                String locationAsJson = gson.toJson(location);
                out = new PrintWriter(new BufferedWriter(new FileWriter(locationFile, true)));
                out.println(locationAsJson);
                out.close();
            }
        } catch (Exception e){
            Utils.processException(e, LocationDaoImpl.class, "save");
        } finally {
            if (out != null){
                out.close();
            }
        }
    }

    @Override
    public Location find(String id) {
        File locationFile = new File(getLocationDaoDirPath() + "/" + id + "/" + id + SAVED_LOCATION_SUFFIX);
        String locationAsJson = Utils.readFileAsString(locationFile);
        Gson gson = new Gson();
        return gson.fromJson(locationAsJson, Location.class);
    }

    @Override
    public Set<Location> findAll() {
        Set<Location> result = new HashSet<Location>();
        try {
            File locationDao = new File(getLocationDaoDirPath());
            for (File locationPath : locationDao.listFiles()){
                File[] filesInsideLocationPath = locationPath.listFiles(new AllLocationFilenameFilter());
                File loacationFile = filesInsideLocationPath[0];
                String locationAsJson = Utils.readFileAsString(loacationFile);
                Gson gson = new Gson();
                Location location = gson.fromJson(locationAsJson, Location.class);
                result.add(location);
            }
        } catch (Exception e){
            Utils.processException(e, LocationDaoImpl.class, "findAll");
        }
        return result;
    }

    @Override
    public Set<String> findAllIds() {
        String[] filePathsInLocationDaoDir = new File(getLocationDaoDirPath()).list();
        return new HashSet<String>(Arrays.asList(filePathsInLocationDaoDir));
    }

    @Override
    public boolean isSentToServer(String id) {
        String locationDir = getLocationDaoDirPath() + "/" + id + "/";
        File sentFileMarker = new File(locationDir + id + SAVED_LOCATION_SUFFIX + SENT_LOCATION_SUFFIX);
        return sentFileMarker.exists();
    }

    @Override
    public void delete(String id) throws IOException {
        String locationToDeletePath = getLocationDaoDirPath() + "/" + id;
        File locationToDelete = new File(locationToDeletePath);
        deleteRecursively(locationToDelete);
    }

    @Override
    public void markAsSent(Location location) throws IOException {
        int hashCode = location.hashCode();
        String locationFileDirPath = getLocationDaoDirPath() + "/" + hashCode + "/";
        String markerFilePath = locationFileDirPath + hashCode + SAVED_LOCATION_SUFFIX + SENT_LOCATION_SUFFIX;
        new File(markerFilePath).createNewFile();
    }

    private static String getLocationDaoDirPath(){
        return Utils.getApplicationRootDir() + "/locationDao";
    }

    private static class AllLocationFilenameFilter implements FilenameFilter {
        @Override
        public boolean accept(File file, String s) {
            return s.contains(SAVED_LOCATION_SUFFIX);
        }
    }
}
