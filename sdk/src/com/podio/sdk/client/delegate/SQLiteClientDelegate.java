/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */

package com.podio.sdk.client.delegate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.internal.utils.Utils;

public class SQLiteClientDelegate implements RestClientDelegate {

    private final SQLiteHelper sqliteHelper;

    public SQLiteClientDelegate(Context context, String name, int version) {
        sqliteHelper = new SQLiteHelper(context, name, version);
    }

    @Override
    public RestResult authorize(Uri uri, PodioParser<?> parser) {
        throw new UnsupportedOperationException(
                "SQLiteDatabaseDelegate doesn't support authorization requests");
    }

    @Override
    public RestResult delete(Uri uri, PodioParser<?> parser) throws SQLiteException {
    	if (Utils.isEmpty(uri)) {
    		throw new IllegalArgumentException("uri cannot be empty");
    	}
    	
        SQLiteDatabase database = sqliteHelper.getWritableDatabase();
        String key = "uri=?";
        String[] value = { uri.toString() };
        int count = database.delete("content", key, value);

        boolean isSuccess = count != -1;
        
        return new RestResult(isSuccess, null, null);
    }

    @Override
    public RestResult get(Uri uri, PodioParser<?> parser) throws SQLiteException {
    	if (Utils.isEmpty(uri)) {
    		throw new IllegalArgumentException("uri cannot be empty");
    	}
    	if (parser == null) {
    		throw new NullPointerException("parser cannot be null");
    	}

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        String[] projection = { "json" };
        String key = "uri=?";
        String[] value = { uri.toString() };

        Cursor cursor = database.query("content", projection, key, value, null, null, null);

        if (cursor == null || !cursor.moveToFirst()) {
        	return new RestResult(false, null, null);
        }
        
        String json = cursor.getString(0);

        Object item = parser.parseToItem(json);
        
        return new RestResult(true, null, item);
    }

    @Override
    public RestResult post(Uri uri, Object item, PodioParser<?> parser) throws SQLiteException {
    	if (Utils.isEmpty(uri)) {
    		throw new IllegalArgumentException("uri cannot be empty");
    	}
    	if (parser == null) {
    		throw new NullPointerException("parser cannot be null");
    	}
    	
        SQLiteDatabase database = sqliteHelper.getWritableDatabase();
        String json = parser.parseToJson(item);

        ContentValues values = new ContentValues();
        values.put("uri", uri.toString());
        values.put("json", json);

        long id = database.insertWithOnConflict("content", null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
        
        boolean isSuccess = id != -1;
        
        return new RestResult(isSuccess, null, null);
    }

    @Override
    public RestResult put(Uri uri, Object item, PodioParser<?> parser) throws SQLiteException {
    	if (Utils.isEmpty(uri)) {
    		throw new IllegalArgumentException("uri cannot be empty");
    	}
    	if (parser == null) {
    		throw new NullPointerException("parser cannot be null");
    	}
    	
        SQLiteDatabase database = sqliteHelper.getWritableDatabase();
        String json = parser.parseToJson(item);

        String key = "uri=?";
        String[] value = { uri.toString() };
        ContentValues values = new ContentValues();
        values.put("json", json);

        int count = database.updateWithOnConflict("content", values, key, value,
                SQLiteDatabase.CONFLICT_IGNORE);

        boolean isSuccess = count != -1;
        
        return new RestResult(isSuccess, null, null);
    }

    public SQLiteDatabase getReadableDatabase() {
        return sqliteHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return sqliteHelper.getWritableDatabase();
    }
}
