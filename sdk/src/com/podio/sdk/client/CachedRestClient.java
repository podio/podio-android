package com.podio.sdk.client;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.Filter;
import com.podio.sdk.RestClient;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.delegate.SQLiteClientDelegate;
import com.podio.sdk.internal.request.RestOperation;

/**
 * A RestClient that, when requesting data, returns content from a local
 * database first, before passing the request on to the parent,
 * {@link HttpRestClient}, implementation. For any other operation (basically,
 * pushing or deleting data) the request is relayed directly to the parent
 * implementation.
 * 
 * @author László Urszuly
 */
public class CachedRestClient extends HttpRestClient {
    private static final String DATABASE_NAME = "podio.db";
    private static final int DATABASE_VERSION = 1;

    private final String contentScheme;

    private RestClientDelegate databaseDelegate;
    private ArrayList<RestRequest> delegatedRequests;

    /**
     * Creates a new <code>CachedRestClient</code> with a default pending
     * requests capacity of 10.
     * 
     * @param context
     *            The context in which to operate on the database and network
     *            files.
     * @param authority
     *            The content authority, this authority will apply to both the
     *            database and the network Uri.
     */
    public CachedRestClient(Context context, String authority) {
        this(context, authority, 10);
    }

    /**
     * Creates a new <code>CachedRestClient</code> with the given pending
     * requests capacity.
     * 
     * @param context
     *            The context in which to operate on the database and network
     *            files.
     * @param authority
     *            The content authority, this authority will apply to both the
     *            database and the network Uri.
     * @param queueCapacity
     *            The number of pending request this {@link RestClient} will
     *            keep in its queue.
     */
    public CachedRestClient(Context context, String authority, int queueCapacity) {
        super(context, authority, queueCapacity);
        contentScheme = "content";
        delegatedRequests = new ArrayList<RestRequest>();
        databaseDelegate = new SQLiteClientDelegate(context, DATABASE_NAME, DATABASE_VERSION);
    }

    /**
     * Performs a custom rest request flow, by - generally speaking - allowing
     * all requests to be handled by the super network client implementation
     * first. When the super implementation delivers a result, that result is
     * stored by this implementation in a local database. The stored data is
     * then requested immediately after and returned to the caller.
     * 
     * One exception from the above flow is the GET rest requests, which
     * actually return the cached content first and then re-posts the same
     * request to be handled by the network client as well according to the
     * above pattern.
     * 
     * @see com.podio.sdk.client.HttpRestClient#handleRequest(com.podio.sdk.client.RestRequest)
     */
    @Override
    protected RestResult handleRequest(RestRequest restRequest) {
        RestResult result = null;

        if (restRequest != null) {
            RestOperation operation = restRequest.getOperation();
            Filter filter = restRequest.getFilter();
            Object item = restRequest.getContent();
            Class<?> itemType = restRequest.getItemType();

            Uri uri = filter.buildUri(contentScheme, authority);

            if (uri != null && itemType != null) {
                if (operation == RestOperation.GET && !delegatedRequests.contains(restRequest)) {

                    // Query the locally cached data first...
                    delegate(operation, uri, item, itemType);

                    // ...and then queue the request once again for the super
                    // implementation to act upon.
                    delegatedRequests.add(restRequest);
                    super.perform(restRequest);
                } else {
                    // Let the super implementation act upon the request.
                    delegatedRequests.remove(restRequest);
                    result = super.handleRequest(restRequest);

                    // The super implementation has delivered successfully,
                    // now also update the local cache accordingly.
                    if (result.isSuccess() && operation != RestOperation.GET) {
                        result = delegate(operation, uri, item, itemType);
                    }

                    // The cache update succeeded. Get the new cached content
                    // and return it to the caller.
                    if (result.isSuccess()) {
                        result = delegate(RestOperation.GET, uri, item, itemType);
                    }
                }
            }
        }

        return result != null ? result : new RestResult(false, null, null);
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
     * Lets the assigned {@link RestClientDelegate} implementation act upon the
     * underlying content as requested per operation and Uri.
     * 
     * @param operation
     *            The operation to perform.
     * @param uri
     *            The key used to identify the content.
     * @param item
     *            The description of the new content.
     * @param itemType
     *            The definition of the new content type.
     * @return The result description of the requested operation.
     */
    private RestResult delegate(RestOperation operation, Uri uri, Object item, Class<?> itemType) {
        RestResult result;

        switch (operation) {
        case DELETE:
            result = databaseDelegate.delete(uri);
            break;
        case GET:
            result = databaseDelegate.get(uri, itemType);
            break;
        case POST:
            result = databaseDelegate.post(uri, item, itemType);
            break;
        case PUT:
            result = databaseDelegate.put(uri, item, itemType);
            break;
        default:
            result = new RestResult(false, null, null);
            break;
        }

        return result;
    }
}
