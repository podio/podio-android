package com.podio.test;

import com.podio.sdk.domain.Session;
import com.podio.sdk.internal.request.ResultListener;

public class ThreadCaptureResultListener implements ResultListener<Object> {
	
	private String threadName;

	public String getThreadName() {
		return threadName;
	}

	@Override
	public void onFailure(Object ticket, String message) {
	}

	@Override
	public void onSessionChange(Object ticket, Session session) {
	}

	@Override
	public void onSuccess(Object ticket, Object content) {
		this.threadName = Thread.currentThread().getName();
	}

}
