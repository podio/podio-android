package com.podio.sdk;

public class ThreadCaptureResultListener implements Request.ResultListener<Object> {

    private String threadName;

    public String getThreadName() {
        return threadName;
    }

    @Override
    public boolean onRequestPerformed(Object content) {
        this.threadName = Thread.currentThread().getName();
        return false;
    }
}
