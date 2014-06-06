package com.podio.sdk.internal.request;

import com.podio.sdk.domain.Session;

public abstract class ResultListenerAdapter<T> implements ResultListener<T> {

	@Override
	public void onFailure(Object ticket, String message) {
	}

	@Override
	public void onSessionChange(Object ticket, Session session) {
	}

	@Override
	public void onSuccess(Object ticket, T content) {
	}

}
