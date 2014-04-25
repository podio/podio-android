package com.podio.sdk.domain.mock;

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
    public boolean enqueue(RestRequest request) {
        this.request = request;
        return true;
    }

    public RestRequest mock_getLastPushedRestRequest() {
        return request;
    }

    public void mock_processLastPushedRestRequest(boolean shouldBeSuccess, String withMockMessage,
            Object withMockItem) {

        if (request != null) {
            ResultListener listener = request.getResultListener();

            if (listener != null) {
                Object ticket = request.getTicket();

                if (shouldBeSuccess) {
                    listener.onSuccess(ticket, withMockItem);
                } else {
                    listener.onFailure(ticket, withMockMessage);
                }
            }
        }
    }

}
