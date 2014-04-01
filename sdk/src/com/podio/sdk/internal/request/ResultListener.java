package com.podio.sdk.internal.request;

import java.util.List;

public interface ResultListener {

    public void onFailure(Object ticket, String message);

    public void onSuccess(Object ticket, List<?> items);

}
