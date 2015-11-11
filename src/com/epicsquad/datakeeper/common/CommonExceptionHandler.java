package com.epicsquad.datakeeper.common;

public class CommonExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Utils.processException(throwable, CommonExceptionHandler.class, "Thread: " + thread.getName());
    }
}
