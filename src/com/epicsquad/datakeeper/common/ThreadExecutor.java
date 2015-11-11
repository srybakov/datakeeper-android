package com.epicsquad.datakeeper.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor {

    private ThreadExecutor(){
        Thread.setDefaultUncaughtExceptionHandler(new CommonExceptionHandler());
    }

    private static final int corePoolSize = 4;
    private static final int maximumPoolSize = 8;
    private static final int keepAliveTime = 10;

    private static final ThreadExecutor instance = new ThreadExecutor();
    private static final ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public static ThreadExecutor getInstance(){
        return instance;
    }

    public void runTask(Runnable runnable){
        executorService.execute(runnable);
    }

}
