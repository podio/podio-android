package com.podio.sdk.client;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.Filter;
import com.podio.sdk.RestClient;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.delegate.SQLiteClientDelegate;
import com.podio.sdk.internal.request.RestOperation;

/**
 * This class manages the communication between the client application and the
 * local Podio database cache.
 * 
 * @author László Urszuly
 */
public final class SQLiteRestClient extends QueuedRestClient {
    private static final String DATABASE_NAME = "podio.db";
    private static final int DATABASE_VERSION = 1;

    private RestClientDelegate databaseDelegate;

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
    public SQLiteRestClient(Context context, String authority) {
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
    public SQLiteRestClient(Context context, String authority, int queueCapacity) {
        super("content", authority, queueCapacity);
        databaseDelegate = new SQLiteClientDelegate(context, DATABASE_NAME, DATABASE_VERSION);
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
                Uri uri = filter.buildUri(scheme, authority);

                result = queryDatabase(operation, uri, item, itemType);
            }
        }

        return result;
    }

    /**
     * Sets the database helper class which will manage the actual database
     * access operations.
     * 
     * @param databaseHelper
     *            The helper implementation.
     */
    public void setDatabaseDelegate(RestClientDelegate databaseDelegate) {
        if (databaseDelegate != null) {
            this.databaseDelegate = databaseDelegate;
        }
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
     * @param classOfContent
     *            The class definition of the additional data.
     * @return An object representation of the result of the operation.
     */
    private RestResult queryDatabase(RestOperation operation, Uri uri, Object content,
            Class<?> classOfContent) {

        switch (operation) {
        case DELETE:
            return databaseDelegate.delete(uri);
        case GET:
            return databaseDelegate.get(uri, classOfContent);
        case POST:
            return databaseDelegate.post(uri, content, classOfContent);
        case PUT:
            return databaseDelegate.put(uri, content, classOfContent);
        default:
            // This should never happen under normal conditions.
            return new RestResult(false, "Huh?", null);
        }
    }

}
