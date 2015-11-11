package com.epicsquad.datakeeper.model.impl;

import com.epicsquad.datakeeper.common.CommonExceptionHandler;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.model.SMSDao;
import com.epicsquad.datakeeper.model.domain.SMS;
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

public class SMSDaoImpl implements SMSDao {

    private static final String SAVED_SMS_SUFFIX = ".savedSMS";
    private static final String SENT_SMS_SUFFIX = ".sent";

    public SMSDaoImpl() {
        File smsDao = new File(getSMSDaoDirPath());
        if (!smsDao.exists()){
            smsDao.mkdir();
        }
        Thread.setDefaultUncaughtExceptionHandler(new CommonExceptionHandler());
    }

    @Override
    public void save(SMS sms) {
        PrintWriter out = null;
        try {
            int hashCode = sms.hashCode();
            String smsFileDirPath = getSMSDaoDirPath() + "/" + hashCode + "/";
            String smsFilePath = smsFileDirPath + hashCode + SAVED_SMS_SUFFIX;
            File smsDir = new File(smsFileDirPath);
            File smsFile = new File(smsFilePath);
            if (!smsDir.exists()){
                smsDir.mkdir();
                Gson gson = new Gson();
                String smsAsJson = gson.toJson(sms);
                out = new PrintWriter(new BufferedWriter(new FileWriter(smsFile, true)));
                out.println(smsAsJson);
                out.close();
            }
        } catch (Exception e){
            Utils.processException(e, SMSDaoImpl.class, "save");
        } finally {
            if (out != null){
                out.close();
            }
        }
    }

    @Override
    public SMS find(String id) {
        File smsFile = new File(getSMSDaoDirPath() + "/" + id + "/" + id + SAVED_SMS_SUFFIX);
        String smsAsJson = Utils.readFileAsString(smsFile);
        Gson gson = new Gson();
        return gson.fromJson(smsAsJson, SMS.class);
    }

    @Override
    public Set<SMS> findAll() {
        Set<SMS> result = new HashSet<SMS>();
        try {
            File smsDao = new File(getSMSDaoDirPath());
            for (File smsPath : smsDao.listFiles()){
                File[] filesInsideSmsPath = smsPath.listFiles(new AllSMSFilenameFilter());
                File smsFile = filesInsideSmsPath[0];
                String smsAsJson = Utils.readFileAsString(smsFile);
                Gson gson = new Gson();
                SMS sms = gson.fromJson(smsAsJson, SMS.class);
                result.add(sms);
            }
        } catch (Exception e){
            Utils.processException(e, SMSDaoImpl.class, "findAll");
        }
        return result;
    }

    @Override
    public Set<String> findAllIds() {
        String[] filePathsInSMSDaoDir = new File(getSMSDaoDirPath()).list();
        return new HashSet<String>(Arrays.asList(filePathsInSMSDaoDir));
    }

    @Override
    public boolean isSentToServer(String id) {
        String smsDir = getSMSDaoDirPath() + "/" + id + "/";
        File sentFileMarker = new File(smsDir + id + SAVED_SMS_SUFFIX + SENT_SMS_SUFFIX);
        return sentFileMarker.exists();
    }

    @Override
    public void delete(String id) throws IOException {
        String smsToDeletePath = getSMSDaoDirPath() + "/" + id;
        File smsToDelete = new File(smsToDeletePath);
        deleteRecursively(smsToDelete);
    }

    @Override
    public void markAsSent(SMS sms) throws IOException {
        int hashCode = sms.hashCode();
        String smsFileDirPath = getSMSDaoDirPath() + "/" + hashCode + "/";
        String markerFilePath = smsFileDirPath + hashCode + SAVED_SMS_SUFFIX + SENT_SMS_SUFFIX;
        new File(markerFilePath).createNewFile();
    }

    private static String getSMSDaoDirPath(){
        return Utils.getApplicationRootDir() + "/smsDao";
    }

    private static class AllSMSFilenameFilter implements FilenameFilter {
        @Override
        public boolean accept(File file, String s) {
            return s.contains(SAVED_SMS_SUFFIX);
        }
    }
}
