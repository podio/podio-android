package com.podio.sdk.client.delegate;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.internal.utils.Utils;

public class SQLiteClientDelegate extends JsonClientDelegate implements RestClientDelegate {

    private final SQLiteHelper sqliteHelper;

    public SQLiteClientDelegate(Context context, String name, int version) {
        sqliteHelper = new SQLiteHelper(context, name, version);
    }

    @Override
    public RestResult authorize(Uri uri) {
        throw new UnsupportedOperationException(
                "SQLiteDatabaseDelegate doesn't support authorization requests");
    }

    @Override
    public RestResult delete(Uri uri) throws SQLiteException {
        int count = -1;

        if (Utils.notEmpty(uri)) {
            SQLiteDatabase database = sqliteHelper.getWritableDatabase();
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
    public RestResult get(Uri uri) throws SQLiteException, InvalidParserException {
        String json = null;

        if (Utils.notEmpty(uri)) {
            SQLiteDatabase database = sqliteHelper.getReadableDatabase();
            String[] projection = { "json" };
            String key = "uri=?";
            String[] value = { uri.toString() };

            Cursor cursor = database.query("content", projection, key, value, null, null, null);

            if (cursor != null) {
                json = cursor.moveToFirst() ? cursor.getString(0) : "";
            }
        }

        boolean isSuccess = json != null;
        String message = null;
        Object item = parseJson(json);
        RestResult result = new RestResult(isSuccess, message, item);

        return result;
    }

    @Override
    public RestResult post(Uri uri, Object item) throws SQLiteException, InvalidParserException {
        long id = -1L;

        if (Utils.notEmpty(uri)) {
            SQLiteDatabase database = sqliteHelper.getWritableDatabase();
            String json = parseItem(item);

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
    public RestResult put(Uri uri, Object item) throws SQLiteException, InvalidParserException {
        int count = -1;

        if (Utils.notEmpty(uri)) {
            SQLiteDatabase database = sqliteHelper.getWritableDatabase();
            String json = parseItem(item);

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

    public SQLiteDatabase getReadableDatabase() {
        return sqliteHelper != null ? sqliteHelper.getReadableDatabase() : null;
    }

    public SQLiteDatabase getWritableDatabase() {
        return sqliteHelper != null ? sqliteHelper.getWritableDatabase() : null;
    }
}
