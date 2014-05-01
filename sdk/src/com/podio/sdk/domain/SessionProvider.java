package com.podio.sdk.domain;

import com.podio.sdk.Filter;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.internal.request.RestOperation;

public class SessionProvider extends PodioProvider {

    public Object authenticateWithUserCredentials(String clientId, String clientSecret,
            String username, String password) {

        Filter filter = new SessionFilter() //
                .withClientCredentials(clientId, clientSecret) //
                .withUserCredentials(username, password);

        return authorize(filter);
    }

    public Object authenticateWithAppCredentials(String clientId, String clientSecret,
            String appId, String appToken) {

        Filter filter = new SessionFilter() //
                .withClientCredentials(clientId, clientSecret) //
                .withAppCredentials(appId, appToken);

        return authorize(filter);
    }

    private Object authorize(Filter filter) {
        Object ticket = null;

        if (client != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.AUTHORIZE, filter, null);

            if (client.enqueue(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }
}
