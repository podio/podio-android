package com.podio.sdk.domain.mock;

import java.util.List;

import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.internal.request.ResultListener;

public final class MockRestClient implements RestClient {

    private RestRequest request;

    @Override
    public String getAuthority() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public boolean perform(RestRequest request) {
        this.request = request;
        return true;
    }

    public RestRequest mock_getLastPushedRestRequest() {
        return request;
    }

    public void mock_processLastPushedRestRequest(boolean isSuccess, Object ticket, String message, List<?> items) {
        if (request != null) {
            ResultListener listener = request.getResultListener();

            if (listener != null) {
                if (isSuccess) {
                    listener.onSuccess(ticket, items);
                } else {
                    listener.onFailure(ticket, message);
                }
            }
        }
    }
}
