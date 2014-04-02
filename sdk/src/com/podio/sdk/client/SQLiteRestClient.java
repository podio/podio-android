package com.podio.sdk.client;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.podio.sdk.RestClient;
import com.podio.sdk.client.database.DatabaseClientDelegate;
import com.podio.sdk.client.database.SQLiteClientDelegate;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.utils.Utils;
import com.podio.sdk.parser.CursorItemParser;
import com.podio.sdk.parser.ItemContentValuesParser;

/**
 * This class manages the communication between the client application and the
 * local Podio database cache.
 * 
 * @author László Urszuly
 */
public final class SQLiteRestClient extends QueuedRestClient {
    private static final String DATABASE_NAME = "podio.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseClientDelegate databaseDelegate;
    private CursorItemParser resultParser;
    private ItemContentValuesParser contentParser;

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
        resultParser = new CursorItemParser();
        contentParser = new ItemContentValuesParser();
        databaseDelegate = new SQLiteClientDelegate(context, DATABASE_NAME, DATABASE_VERSION);
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

            Cursor cursor = queryDatabase(operation, uri, item, itemType);
            List<?> items = buildItems(cursor, itemType);

            if (items != null) {
                result = new RestResult(true, null, items);
            } else {
                result = new RestResult(false, "Ohno", null);
            }
        }

        return result;
    }

    /**
     * Sets the parser used for parsing content items when performing an insert
     * or update operation. The parser will take the content item object and
     * parse data from it and populate a new ContentValues object with the data.
     * 
     * @param parser
     *            The parser to use for extracting item data.
     */
    public void setContentParser(ItemContentValuesParser parser) {
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
    public void setDatabaseDelegate(DatabaseClientDelegate databaseDelegate) {
        if (databaseDelegate != null) {
            this.databaseDelegate = databaseDelegate;
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
    public void setResultParser(CursorItemParser parser) {
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
    private ContentValues buildContentValues(Object item, Class<?> classOfItem) {
        List<ContentValues> values = contentParser.parse(item, classOfItem);
        ContentValues result = Utils.notEmpty(values) ? values.get(0) : null;

        return result;
    }

    /**
     * Creates a list of content item objects, created from the rows of the
     * provided cursor.
     * 
     * @param cursor
     *            The cursor to parse
     * @param classOfItem
     *            The class definition of the resulting item.
     * @return A list of content item objects.
     */
    private List<?> buildItems(Cursor cursor, Class<?> classOfItem) {
        List<?> result = resultParser.parse(cursor, classOfItem);
        return result;
    }

    /**
     * Delegates the requested operation to the {@link DatabaseClientDelegate} to
     * execute.
     * 
     * @param operation
     *            The type of rest operation to perform.
     * @param uri
     *            The URI that defines the details of the operation.
     * @param content
     *            Any additional data that is affected by the operation.
     * @param itemType
     *            The class definition of the additional data.
     * @return A cursor with the result of the requested operation.
     */
    private Cursor queryDatabase(RestOperation operation, Uri uri, Object content, Class<?> itemType) {
        Cursor cursor = null;
        ContentValues values = null;

        switch (operation) {
        case GET:
            cursor = databaseDelegate.query(uri);
            break;
        case POST:
            values = buildContentValues(content, itemType);
            cursor = databaseDelegate.insert(uri, values);
            break;
        case DELETE:
            cursor = databaseDelegate.delete(uri);
            break;
        case PUT:
            values = buildContentValues(content, itemType);
            cursor = databaseDelegate.update(uri, values);
            break;
        default:
            // This should never happen under normal conditions.
            cursor = null;
            break;
        }

        return cursor;
    }

}
