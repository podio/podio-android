package com.podio.sdk.client;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.Filter;
import com.podio.sdk.RestClient;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.utils.Utils;

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
    private final String contentScheme;
    private final RestClientDelegate databaseDelegate;
    private final ArrayList<RestRequest> delegatedRequests;

    /**
     * Creates a new <code>CachedRestClient</code>. This implementation will
     * return any cached content that matches the request criteria immediately
     * and after that do a corresponding network operation. When the network
     * call is finished, the cache is updated and the new result is also
     * returned (through the same callback).
     * 
     * @param context
     *            The context in which to operate on the database and network
     *            files.
     * @param authority
     *            The content authority, this authority will apply to both the
     *            database and the network Uri.
     * @param networkDelegate
     *            The {@link RestClientDelegate} implementation that will
     *            perform the HTTP requests.
     * @param cacheDelegate
     *            The {@link RestClientDelegate} implementation that will
     *            perform the SQLite queries.
     * @param queueCapacity
     *            The number of pending request this {@link RestClient} will
     *            keep in its queue.
     */
    public CachedRestClient(Context context, String authority, RestClientDelegate networkDelegate,
            RestClientDelegate cacheDelegate, int queueCapacity) {

        super(context, authority, networkDelegate, queueCapacity);

        if (cacheDelegate == null) {
            throw new IllegalArgumentException("The RestClientDelegates mustn't be null");
        } else {
            this.contentScheme = "content";
            this.delegatedRequests = new ArrayList<RestRequest>();
            this.databaseDelegate = cacheDelegate;
        }
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

            Uri uri = filter.buildUri(contentScheme, authority);

            if (Utils.notEmpty(uri)) {
                if (operation == RestOperation.GET && !delegatedRequests.contains(restRequest)) {
                    // Query the locally cached data first...
                    result = delegate(operation, uri, item);

                    // ...and then queue the request once again for the super
                    // implementation to act upon.
                    delegatedRequests.add(restRequest);
                    super.enqueue(restRequest);
                } else {
                    // Let the super implementation act upon the request.
                    delegatedRequests.remove(restRequest);
                    result = super.handleRequest(restRequest);

                    // The super implementation has delivered successfully,
                    // now also update the local cache accordingly.
                    if (result.isSuccess() && operation != RestOperation.AUTHORIZE) {
                        if (operation == RestOperation.GET) {
                            result = delegate(RestOperation.POST, uri, result.item());
                        } else {
                            result = delegate(operation, uri, result.item());
                        }
                    }

                    // The cache update succeeded. Get the new cached content
                    // and return it to the caller.
                    if (result.isSuccess() && operation != RestOperation.AUTHORIZE) {
                        result = delegate(RestOperation.GET, uri, null);
                    }
                }
            }
        }

        return result != null ? result : new RestResult(false, null, null);
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
     * @return The result description of the requested operation.
     */
    private RestResult delegate(RestOperation operation, Uri uri, Object item) {
        switch (operation) {
        case DELETE:
            return databaseDelegate.delete(uri);
        case GET:
            return databaseDelegate.get(uri);
        case POST:
            return databaseDelegate.post(uri, item);
        case PUT:
            return databaseDelegate.put(uri, item);
        default:
            String message = "Unknown operation: " + operation.name();
            return new RestResult(false, message, null);
        }
    }
}
