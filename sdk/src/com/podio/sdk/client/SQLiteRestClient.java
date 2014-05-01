package com.podio.sdk.client;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.Filter;
import com.podio.sdk.RestClient;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.delegate.JsonClientDelegate;
import com.podio.sdk.internal.request.RestOperation;

/**
 * This class manages the communication between the client application and the
 * local Podio database cache.
 * 
 * @author László Urszuly
 */
public final class SQLiteRestClient extends QueuedRestClient {

    protected final JsonClientDelegate databaseDelegate;

    /**
     * @param context
     *            The context to execute the database operations in.
     * @param authority
     *            The authority to use in URIs by this client.
     * @param databaseDelegate
     *            The {@link RestClientDelegate} implementation that will
     *            perform the SQLite queries.
     * @param queueCapacity
     *            The custom request queue capacity.
     * 
     * @see QueuedRestClient
     * @see RestClient
     */
    public SQLiteRestClient(Context context, String authority, JsonClientDelegate databaseDelegate,
            int queueCapacity) {

        super("content", authority, queueCapacity);

        if (databaseDelegate == null) {
            throw new IllegalArgumentException("The JsonClientDelegate mustn't be null");
        } else {
            this.databaseDelegate = databaseDelegate;
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

                result = queryDatabase(operation, uri, item);
            }
        }

        return result;
    }

    /**
     * Delegates the requested operation to the {@link DatabaseClientDelegate}
     * to execute.
     * 
     * @param operation
     *            The type of rest operation to perform.
     * @param uri
     *            The URI that defines the details of the operation.
     * @param content
     *            Any additional data that the operation refers to.
     * @return An object representation of the result of the operation.
     */
    private RestResult queryDatabase(RestOperation operation, Uri uri, Object content) {

        switch (operation) {
        case AUTHORIZE:
            return databaseDelegate.authorize(uri);
        case DELETE:
            return databaseDelegate.delete(uri);
        case GET:
            return databaseDelegate.get(uri);
        case POST:
            return databaseDelegate.post(uri, content);
        case PUT:
            return databaseDelegate.put(uri, content);
        default:
            // This should never happen under normal conditions.
            String message = "Unknown operation: " + operation.name();
            return new RestResult(false, message, null);
        }
    }

}
