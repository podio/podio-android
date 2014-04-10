package com.podio.sdk.client;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.Filter;
import com.podio.sdk.RestClient;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.authentication.AuthenticationDelegate;
import com.podio.sdk.client.authentication.PodioAuthenticationDelegate;
import com.podio.sdk.client.delegate.HttpClientDelegate;
import com.podio.sdk.internal.request.RestOperation;

/**
 * This class manages the communication between the client application and the
 * Podio servers.
 * 
 * @author László Urszuly
 */
public class HttpRestClient extends QueuedRestClient {
    private AuthenticationDelegate authenticationDelegate;
    private RestClientDelegate networkDelegate;

    /**
     * Creates a new SQLiteRestClient with a request queue capacity of 10
     * requests.
     * 
     * @param context
     *            The context to execute the database operations in.
     * @param authority
     *            The authority to use in URIs by this client.
     * 
     * @see QueuedRestClient
     * @see RestClient
     */
    public HttpRestClient(Context context, String authority) {
        this(context, authority, 10);
    }

    /**
     * @param context
     *            The context to execute the database operations in.
     * @param authority
     *            The authority to use in URIs by this client.
     * @param queueCapacity
     *            The custom request queue capacity.
     * 
     * @see QueuedRestClient
     * @see RestClient
     */
    public HttpRestClient(Context context, String authority, int queueCapacity) {
        super("https", authority, queueCapacity);
        networkDelegate = new HttpClientDelegate(context);
        authenticationDelegate = new PodioAuthenticationDelegate();
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
                Class<?> itemType = restRequest.getItemType();
                Object item = restRequest.getContent();
                Uri uri = buildUri(filter);

                result = queryNetwork(operation, uri, item, itemType);
            }
        }

        return result != null ? result : new RestResult(false, null, null);
    }

    public void setAuthenticationDelegate(AuthenticationDelegate authenticationDelegate) {
        if (authenticationDelegate != null) {
            this.authenticationDelegate = authenticationDelegate;
        }
    }

    /**
     * Sets the database helper class which will manage the actual database
     * access operations.
     * 
     * @param databaseHelper
     *            The helper implementation.
     */
    public void setNetworkDelegate(RestClientDelegate networkDelegate) {
        if (networkDelegate != null) {
            this.networkDelegate = networkDelegate;
        }
    }

    private Uri buildUri(Filter filter) {
        String token = authenticationDelegate.getAuthToken();

        Uri authUri = filter //
                .buildUri(scheme, authority) //
                .buildUpon() //
                .appendQueryParameter("oauth_token", token) //
                .build();

        return authUri;
    }

    private RestResult queryNetwork(RestOperation operation, Uri uri, Object item,
            Class<?> classOfItem) {

        switch (operation) {
        case DELETE:
            return networkDelegate.delete(uri);
        case GET:
            return networkDelegate.get(uri, classOfItem);
        case POST:
            return networkDelegate.post(uri, item, classOfItem);
        case PUT:
            return networkDelegate.put(uri, item, classOfItem);
        default:
            // This should never happen under normal conditions.
            return new RestResult(false, "Huh?", null);
        }
    }
}
