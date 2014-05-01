package com.podio.sdk.domain;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.ProviderListener;
import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;

public class PodioProvider implements Provider {

    private final ResultListener resultListener = new ResultListener() {
        @Override
        public void onFailure(Object ticket, String message) {
            if (providerListener != null) {
                providerListener.onRequestFailure(ticket, message);
            }
        }

        @Override
        public void onSessionChange(Object ticket, Session session) {
            if (providerListener != null) {
                providerListener.onSessionChange(ticket, session);
            }
        }

        @Override
        public void onSuccess(Object ticket, Object content) {
            if (providerListener != null) {
                providerListener.onRequestComplete(ticket, content);
            }
        }
    };

    private ProviderListener providerListener;
    protected RestClient client;

    @Override
    public Object changeRequest(Filter filter, Object item) {
        Object ticket = null;

        if (client != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.PUT, filter, item);

            if (client.enqueue(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    @Override
    public Object deleteRequest(Filter filter) {
        Object ticket = null;

        if (client != null && filter != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.DELETE, filter, null);

            if (client.enqueue(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    @Override
    public Object fetchRequest(Filter filter) {
        Object ticket = null;

        if (client != null && filter != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.GET, filter, null);

            if (client.enqueue(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    @Override
    public Object pushRequest(Filter filter, Object item) {
        Object ticket = null;

        if (client != null && filter != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.POST, filter, item);

            if (client.enqueue(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    /**
     * Sets the callback interface used to report the result through. If this
     * callback is not given, then the rest operations can still be executed
     * silently. Note, though, that the GET operation, even though technically
     * possible, wouldn't make any sense without this callback.
     * 
     * @param providerListener
     *            The callback implementation. Null is valid.
     */
    public void setProviderListener(ProviderListener providerListener) {
        this.providerListener = providerListener;
    }

    /**
     * Sets the rest client that will perform the rest operation.
     * 
     * @param client
     *            The target {@link RestClient}.
     */
    public void setRestClient(RestClient client) {
        this.client = client;
    }

    protected RestRequest buildRestRequest(RestOperation operation, Filter filter, Object content) {
        RestRequest request = new RestRequest() //
                .setContent(content) //
                .setOperation(operation) //
                .setResultListener(resultListener) //
                .setFilter(filter) //
                .setTicket(filter);

        return request;
    }
}
