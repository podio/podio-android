package com.podio.sdk.client;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.podio.sdk.Filter;
import com.podio.sdk.RestClient;
import com.podio.sdk.client.database.DatabaseClientDelegate;
import com.podio.sdk.client.database.SQLiteClientDelegate;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.utils.Utils;
import com.podio.sdk.parser.CursorToItemParser;
import com.podio.sdk.parser.ItemToContentValuesParser;

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
    private CursorToItemParser cursorToItemParser;
    private ItemToContentValuesParser itemToContentValuesParser;

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
        cursorToItemParser = new CursorToItemParser();
        itemToContentValuesParser = new ItemToContentValuesParser();
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

                Cursor cursor = queryDatabase(operation, uri, item, itemType);
                List<?> items = buildItems(cursor, itemType);

                if (items != null) {
                    result = new RestResult(true, null, items);
                } else {
                    result = new RestResult(false, "Ohno", null);
                }
            }
        }

        return result;
    }

    /**
     * Sets the parser used for parsing the database cursor objects. The parser
     * will take the cursor, parse data from its columns and create new item
     * objects from it.
     * 
     * @param cursorToItemParser
     *            The parser to use for extracting cursor data.
     */
    public void setCursorToItemParser(CursorToItemParser cursorToItemParser) {
        if (cursorToItemParser != null) {
            this.cursorToItemParser = cursorToItemParser;
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
     * Sets the parser used for parsing item objects when performing a SQL
     * INSERT or UPDATE operation. The parser will take the item object, parse
     * data from its fields and create a new ContentValues object from it.
     * 
     * @param itemToContentValuesParser
     *            The parser to use for extracting item data.
     */
    public void setItemToContentValuesParser(ItemToContentValuesParser itemToContentValuesParser) {
        if (itemToContentValuesParser != null) {
            this.itemToContentValuesParser = itemToContentValuesParser;
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
        List<ContentValues> values = itemToContentValuesParser.parse(item, classOfItem);
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
        List<?> result = cursorToItemParser.parse(cursor, classOfItem);
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
