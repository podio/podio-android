package com.podio.sdk.internal.request;


public interface ResultListener {

    public void onFailure(Object ticket, String message);

    public void onSuccess(Object ticket, Object content);

}
