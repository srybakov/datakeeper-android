package com.epicsquad.datakeeper.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.epicsquad.datakeeper.receiver.CaptureNewDataReceiver;
import com.epicsquad.datakeeper.receiver.CleaningReceiver;
import com.epicsquad.datakeeper.receiver.SendDataReceiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public final class Utils {

    public static final String SMS_URI_PATH = "content://sms";
    public static final String CALL_LOG_URI_PATH = "content://call_log/calls";

    private static final boolean IS_DEBUG_ENABLED = true;
    private static final String LOG_FILE_NAME = "applicationLog.txt";
    private static String ROOT_DIR = null;

    static {
        Thread.setDefaultUncaughtExceptionHandler(new CommonExceptionHandler());
    }

    public static void setupApplication(Context context){
        Utils.initRootDir(context);
        CleaningReceiver.setAlarm(context);
        CaptureNewDataReceiver.setAlarm(context);
        SendDataReceiver.setAlarm(context);
    }

    public static void processException(Throwable throwable, Class clazz, String message){
        Utils.log(new Date() + ":" + clazz.getName() + "; Message: " + message + "; Exception message:"
                + throwable.getMessage());
    }

    public static void log(String message){
        try {
            if (IS_DEBUG_ENABLED){
                final String applicationLogFilePath = getApplicationRootDir() + "/" + LOG_FILE_NAME;
                final File applicationLogFile = new File(applicationLogFilePath);
                if (!applicationLogFile.exists()){
                    applicationLogFile.createNewFile();
                }
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(applicationLogFile, true)));
                out.println(message);
                out.close();
            }
        } catch (Exception e){
            //TODO: cannot do anything here...
        }
    }

    public static String getApplicationRootDir(){
        return ROOT_DIR;
    }

    public static String readFileAsString(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (IOException e){
            processException(e, Utils.class, "readFileAsString");
        }
        return null;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void initRootDir(final Context context) {
        try {
            ROOT_DIR = context.getExternalFilesDir(null).getCanonicalPath() + "/";
            if (!ROOT_DIR.isEmpty()) {
                makeDirs(ROOT_DIR);
            }
        } catch (final Exception e) {
            Utils.processException(e, Utils.class, "initRootDir");
        }
    }

    public static void deleteRecursively(File file) throws IOException {
        if(file.isDirectory()){
            if(file.list().length==0){
                file.delete();
            } else {
                String files[] = file.list();
                for (String temp : files) {
                    File fileDelete = new File(file, temp);
                    deleteRecursively(fileDelete);
                }
                if(file.list().length==0){
                    file.delete();
                }
            }
        } else {
            file.delete();
        }
    }

    private static boolean makeDirs(final String path) {
        final File f = new File(path);
        return f.mkdirs();
    }
}
