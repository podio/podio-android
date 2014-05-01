package com.podio.sdk.client;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.Filter;
import com.podio.sdk.RestClient;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.delegate.HttpClientDelegate;
import com.podio.sdk.client.delegate.JsonClientDelegate;
import com.podio.sdk.domain.Session;
import com.podio.sdk.internal.request.RestOperation;

/**
 * This class manages the communication between the client application and the
 * Podio servers.
 * 
 * @author László Urszuly
 */
public class HttpRestClient extends QueuedRestClient {

    private static final String SCHEME = "https";

    protected final JsonClientDelegate networkDelegate;

    /**
     * @param context
     *            The context to execute the database operations in.
     * @param authority
     *            The authority to use in URIs by this client.
     * @param authToken
     *            The initial auth token to use when communicating with the
     *            Podio servers.
     * @param networkDelegate
     *            The {@link RestClientDelegate} implementation that will
     *            perform the actual HTTP request.
     * @param queueCapacity
     *            The custom request queue capacity.
     * 
     * @see QueuedRestClient
     * @see RestClient
     */
    public HttpRestClient(Context context, String authority, JsonClientDelegate networkDelegate,
            int queueCapacity) {

        super(SCHEME, authority, queueCapacity);

        if (networkDelegate == null) {
            throw new IllegalArgumentException("The JsonClientDelegate mustn't be null");
        } else {
            this.networkDelegate = networkDelegate;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected RestResult handleRequest(RestRequest restRequest) {
        RestResult result = null;

        if (restRequest != null) {
            Filter filter = restRequest.getFilter();

            if (filter != null) {
                RestOperation operation = restRequest.getOperation();
                Object item = restRequest.getContent();
                Uri uri = filter.buildUri(scheme, authority);

                result = queryNetwork(operation, uri, item);
            }
        }

        return result != null ? result : new RestResult(false, null, null);
    }

    public void revokeSession(String refreshPath, Session session) {
        if (networkDelegate instanceof HttpClientDelegate) {
            Uri sessionRefreshUri = new Uri.Builder() //
                    .scheme(SCHEME) //
                    .authority(authority) //
                    .appendEncodedPath(refreshPath) //
                    .build();
            String url = sessionRefreshUri.toString();
            ((HttpClientDelegate) networkDelegate).revokeSession(url, session);
        }
    }

    private RestResult queryNetwork(RestOperation operation, Uri uri, Object item) {

        switch (operation) {
        case AUTHORIZE:
            return networkDelegate.authorize(uri);
        case DELETE:
            return networkDelegate.delete(uri);
        case GET:
            return networkDelegate.get(uri);
        case POST:
            return networkDelegate.post(uri, item);
        case PUT:
            return networkDelegate.put(uri, item);
        default:
            // This should never happen under normal conditions.
            String message = "Unknown operation: " + operation.name();
            return new RestResult(false, message, null);
        }
    }
}
