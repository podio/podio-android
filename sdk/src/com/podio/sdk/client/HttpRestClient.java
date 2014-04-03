package com.podio.sdk.client;

import java.util.List;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.RestClient;
import com.podio.sdk.client.authentication.AuthenticationDelegate;
import com.podio.sdk.client.authentication.PodioAuthenticationDelegate;
import com.podio.sdk.client.network.HttpClientDelegate;
import com.podio.sdk.client.network.NetworkClientDelegate;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.utils.Utils;
import com.podio.sdk.parser.ItemJsonParser;
import com.podio.sdk.parser.JsonItemParser;

/**
 * This class manages the communication between the client application and the
 * Podio servers.
 * 
 * @author László Urszuly
 */
public class HttpRestClient extends QueuedRestClient {
    private AuthenticationDelegate authenticationDelegate;
    private NetworkClientDelegate networkDelegate;
    private JsonItemParser resultParser;
    private ItemJsonParser contentParser;

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
        resultParser = new JsonItemParser();
        contentParser = new ItemJsonParser();
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
            Uri uri = restRequest.buildUri(scheme, authority);

            RestOperation operation = restRequest.getOperation();
            Class<?> itemType = restRequest.getItemType();
            Object item = restRequest.getContent();

            String json = queryNetwork(operation, uri, item, itemType);
            List<?> items = buildItems(json, itemType);

            if (items != null) {
                result = new RestResult(true, null, items);
            } else {
                result = new RestResult(false, "Ohno", null);
            }
        }

        return result;
    }

    public void setAuthenticationDelegate(AuthenticationDelegate authenticationDelegate) {
        if (authenticationDelegate != null) {
            this.authenticationDelegate = authenticationDelegate;
        }
    }

    /**
     * Sets the parser used for parsing content items when performing an insert
     * or update operation. The parser will take the content item object and
     * parse data from it and populate a new ContentValues object with the data.
     * 
     * @param parser
     *            The parser to use for extracting item data.
     */
    public void setContentParser(ItemJsonParser parser) {
        if (parser != null) {
            this.contentParser = parser;
        }
    }

    /**
     * Sets the database helper class which will manage the actual database
     * access operations.
     * 
     * @param databaseHelper
     *            The helper implementation.
     */
    public void setNetworkDelegate(NetworkClientDelegate networkDelegate) {
        if (networkDelegate != null) {
            this.networkDelegate = networkDelegate;
        }
    }

    /**
     * Sets the parser used for parsing the database cursor when performing a
     * query operation. The parser will take the cursor and parse data from its
     * columns and populate new content item objects with the data.
     * 
     * @param parser
     *            The parser to use for extracting cursor data.
     */
    public void setResultParser(JsonItemParser parser) {
        if (parser != null) {
            this.resultParser = parser;
        }
    }

    /**
     * Creates a ContentValues object, populated with data from the provided
     * content item object.
     * 
     * @param item
     *            The item to parse.
     * @param classOfItem
     *            The class definition of the item.
     * @return A ContentValues object with keys and values parsed from the given
     *         item.
     */
    private String buildJson(Object item, Class<?> classOfItem) {
        List<?> values = contentParser.parse(item, classOfItem);
        Object result = Utils.notEmpty(values) ? values.get(0) : null;
        return result != null ? result.toString() : "";
    }

    /**
     * Creates a list of content item objects, created from the provided JSON
     * string.
     * 
     * @param json
     *            The JSON string to parse
     * @param classOfItem
     *            The class definition of the resulting item.
     * @return A list of content item objects.
     */
    private List<?> buildItems(String json, Class<?> classOfItem) {
        List<?> result = resultParser.parse(json, classOfItem);
        return result;
    }

    private String queryNetwork(RestOperation operation, Uri uri, Object item, Class<?> classOfItem) {
        String json = null;
        String values = null;
        String token = authenticationDelegate.getAuthToken();

        switch (operation) {
        case DELETE:
            json = networkDelegate.delete(uri);
            break;
        case GET:
            json = networkDelegate.get(uri);
            break;
        case POST:
            values = buildJson(item, classOfItem);
            json = networkDelegate.post(uri, values);
            break;
        case PUT:
            values = buildJson(item, classOfItem);
            json = networkDelegate.put(uri, values);
            break;
        default:
            // This should never happen under normal conditions.
            json = null;
            break;
        }

        return json;
    }
}
