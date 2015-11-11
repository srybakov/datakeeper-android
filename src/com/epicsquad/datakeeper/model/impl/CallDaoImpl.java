package com.epicsquad.datakeeper.model.impl;

import com.epicsquad.datakeeper.common.CommonExceptionHandler;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.model.CallDao;
import com.epicsquad.datakeeper.model.domain.Call;
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

public class CallDaoImpl implements CallDao {

    private static final String SAVED_CALL_SUFFIX = ".savedCall";
    private static final String SENT_CALL_SUFFIX = ".sent";

    public CallDaoImpl() {
        File callDao = new File(getCallDaoDirPath());
        if (!callDao.exists()){
            callDao.mkdir();
        }
        Thread.setDefaultUncaughtExceptionHandler(new CommonExceptionHandler());
    }

    @Override
    public void save(Call call) {
        PrintWriter out = null;
        try {
            int hashCode = call.hashCode();
            String callFileDirPath = getCallDaoDirPath() + "/" + hashCode + "/";
            String callFilePath = callFileDirPath + hashCode + SAVED_CALL_SUFFIX;
            File callDir = new File(callFileDirPath);
            File callFile = new File(callFilePath);
            if (!callDir.exists()){
                callDir.mkdir();
                Gson gson = new Gson();
                String callAsJson = gson.toJson(call);
                out = new PrintWriter(new BufferedWriter(new FileWriter(callFile, true)));
                out.println(callAsJson);
                out.close();
            }
        } catch (Exception e){
            Utils.processException(e, CallDaoImpl.class, "save");
        } finally {
            if (out != null){
                out.close();
            }
        }
    }

    @Override
    public Call find(String id) {
        File callFile = new File(getCallDaoDirPath() + "/" + id + "/" + id + SAVED_CALL_SUFFIX);
        String callAsJson = Utils.readFileAsString(callFile);
        Gson gson = new Gson();
        return gson.fromJson(callAsJson, Call.class);
    }

    @Override
    public Set<Call> findAll() {
        Set<Call> result = new HashSet<Call>();
        try {
            File callDao = new File(getCallDaoDirPath());
            for (File callPath : callDao.listFiles()){
                File[] filesInsideCallPath = callPath.listFiles(new AllCallFilenameFilter());
                File callFile = filesInsideCallPath[0];
                String callAsJson = Utils.readFileAsString(callFile);
                Gson gson = new Gson();
                Call call = gson.fromJson(callAsJson, Call.class);
                result.add(call);
            }
        } catch (Exception e){
            Utils.processException(e, CallDaoImpl.class, "findAll");
        }
        return result;
    }

    @Override
    public Set<String> findAllIds() {
        String[] filePathsInCallDaoDir = new File(getCallDaoDirPath()).list();
        return new HashSet<String>(Arrays.asList(filePathsInCallDaoDir));
    }

    @Override
    public boolean isSentToServer(String id) {
        String callDir = getCallDaoDirPath() + "/" + id + "/";
        File sentFileMarker = new File(callDir + id + SAVED_CALL_SUFFIX + SENT_CALL_SUFFIX);
        return sentFileMarker.exists();
    }

    @Override
    public void delete(String id) throws IOException {
        String callToDeletePath = getCallDaoDirPath() + "/" + id;
        File callToDelete = new File(callToDeletePath);
        deleteRecursively(callToDelete);
    }

    @Override
    public void markAsSent(Call call) throws IOException {
        int hashCode = call.hashCode();
        String callFileDirPath = getCallDaoDirPath() + "/" + hashCode + "/";
        String markerFilePath = callFileDirPath + hashCode + SAVED_CALL_SUFFIX + SENT_CALL_SUFFIX;
        new File(markerFilePath).createNewFile();
    }

    private static String getCallDaoDirPath(){
        return Utils.getApplicationRootDir() + "/callDao";
    }

    private static class AllCallFilenameFilter implements FilenameFilter {
        @Override
        public boolean accept(File file, String s) {
            return s.contains(SAVED_CALL_SUFFIX);
        }
    }

}
