package com.podio.sdk.internal.request;

import com.podio.sdk.Session;

public interface ResultListener {

    public void onFailure(Object ticket, String message);

    public void onSessionChange(Object ticket, Session session);

    public void onSuccess(Object ticket, Object content);

}
