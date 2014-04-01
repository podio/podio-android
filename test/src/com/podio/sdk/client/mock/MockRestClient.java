package com.podio.sdk.client.mock;

import com.podio.sdk.client.QueuedRestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.client.RestResult;

public class MockRestClient extends QueuedRestClient {

    public MockRestClient(String scheme, String authority) {
        super(scheme, authority);
    }

    public MockRestClient(String scheme, String authority, int capacity) {
        super(scheme, authority, capacity);
    }

    @Override
    protected RestResult handleRequest(RestRequest restRequest) {
        return null;
    }

}
