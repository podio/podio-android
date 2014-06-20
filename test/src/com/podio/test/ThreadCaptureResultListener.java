package com.podio.test;

import com.podio.sdk.internal.request.ResultListener;

public class ThreadCaptureResultListener implements ResultListener<Object> {

    private String threadName;

    public String getThreadName() {
        return threadName;
    }

    @Override
    public void onRequestPerformed(Object content) {
        this.threadName = Thread.currentThread().getName();
    }

}
