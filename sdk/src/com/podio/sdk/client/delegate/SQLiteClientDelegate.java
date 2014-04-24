package com.podio.sdk.client.delegate;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.internal.utils.Utils;
import com.podio.sdk.parser.ItemToJsonParser;
import com.podio.sdk.parser.JsonToItemParser;

public class SQLiteClientDelegate extends SQLiteOpenHelper implements RestClientDelegate {

    private ItemToJsonParser itemToJsonParser;
    private JsonToItemParser jsonToItemParser;

    public SQLiteClientDelegate(Context context, String name, int version) {
        super(context, name, null, version);
        itemToJsonParser = new ItemToJsonParser();
        jsonToItemParser = new JsonToItemParser();
    }

    @Override
    public RestResult delete(Uri uri) {
        int count = -1;
        SQLiteDatabase database = openDatabase(uri);

        if (database != null) {
            String key = "uri=?";
            String[] value = { uri.toString() };
            count = database.delete("content", key, value);
        }

        boolean isSuccess = count != -1;
        String message = null;
        List<?> items = null;
        RestResult result = new RestResult(isSuccess, message, items);

        return result;
    }

    @Override
    public RestResult get(Uri uri, Class<?> classOfResult) {
        String json = null;
        SQLiteDatabase database = openDatabase(uri);

        if (database != null) {
            String[] projection = { "json" };
            String key = "uri=?";
            String[] value = { uri.toString() };

            Cursor cursor = database.query("content", projection, key, value, null, null, null);

            if (cursor != null) {
                json = cursor.moveToFirst() ? cursor.getString(0) : "{}";
            }
        }

        boolean isSuccess = json != null;
        String message = null;
        Object item = jsonToItemParser.parse(json, classOfResult);
        RestResult result = new RestResult(isSuccess, message, item);

        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        clearDatabase(database);
        setupDatabase(database);
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        clearDatabase(database);
        setupDatabase(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        clearDatabase(database);
        setupDatabase(database);
    }

    @Override
    public RestResult post(Uri uri, Object item, Class<?> classOfItem) {
        long id = -1L;
        SQLiteDatabase database = openDatabase(uri);

        if (database != null) {
            String json = itemToJsonParser.parse(item, classOfItem);

            ContentValues values = new ContentValues();
            values.put("uri", uri.toString());
            values.put("json", json);

            id = database.insertWithOnConflict("content", null, values,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }

        boolean isSuccess = id != -1L;
        String message = null;
        List<?> items = null;
        RestResult result = new RestResult(isSuccess, message, items);

        return result;
    }

    @Override
    public RestResult put(Uri uri, Object item, Class<?> classOfItem) {
        int count = -1;
        SQLiteDatabase database = openDatabase(uri);

        if (database != null) {
            String json = itemToJsonParser.parse(item, classOfItem);

            String key = "uri=?";
            String[] value = { uri.toString() };
            ContentValues values = new ContentValues();
            values.put("json", json);

            count = database.updateWithOnConflict("content", values, key, value,
                    SQLiteDatabase.CONFLICT_IGNORE);

        }

        boolean isSuccess = count != -1;
        String message = null;
        List<?> items = null;
        RestResult result = new RestResult(isSuccess, message, items);

        return result;
    }

    /**
     * Sets the parser used for parsing content items when performing an HTTP
     * POST or PUT request. The parser will take the item object, parse data
     * from its fields and create a new JSON string from it.
     * 
     * @param itemToJsonParser
     *            The parser to use for extracting item data.
     */
    public void setItemToJsonParser(ItemToJsonParser itemToJsonParser) {
        if (itemToJsonParser != null) {
            this.itemToJsonParser = itemToJsonParser;
        }
    }

    /**
     * Sets the parser used for parsing the response json when performing an
     * HTTP GET request. The parser will take the json string, parse its
     * attributes and create new corresponding item objects from it.
     * 
     * @param jsonToItemParser
     *            The parser to use for extracting cursor data.
     */
    public void setJsonToItemParser(JsonToItemParser jsonToItemParser) {
        if (jsonToItemParser != null) {
            this.jsonToItemParser = jsonToItemParser;
        }
    }

    private void clearDatabase(SQLiteDatabase database) {
        if (database != null) {
            Cursor cursor = null;

            try {
                // Drop all tables, views and triggers in the database.
                database.beginTransaction();
                String query = "SELECT type, name FROM sqlite_master WHERE type IN (?, ?, ?)";
                String[] arguments = { "table", "view", "trigger" };
                cursor = database.rawQuery(query, arguments);

                if (cursor != null && cursor.moveToFirst()) {
                    int typeColumn = cursor.getColumnIndex("type");
                    int nameColumn = cursor.getColumnIndex("name");

                    do {
                        String type = cursor.getString(typeColumn);
                        String name = cursor.getString(nameColumn);
                        database.execSQL("DROP " + type + " IF EXISTS " + name);
                    } while (cursor.moveToNext());
                }

                database.setTransactionSuccessful();
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                database.endTransaction();
            }
        }
    }

    private SQLiteDatabase openDatabase(Uri uri) {
        SQLiteDatabase database = null;

        if (Utils.notEmpty(uri)) {
            try {
                database = getWritableDatabase();
            } catch (SQLiteException e) {
                database = null;
            }
        }

        return database;
    }

    private void setupDatabase(SQLiteDatabase database) {
        if (database != null) {
            try {
                database.beginTransaction();
                database.execSQL("CREATE TABLE content (" + //
                        " uri TEXT PRIMARY KEY," + //
                        " json TEXT NOT NULL DEFAULT ('{}'))");
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
        }
    }
}
