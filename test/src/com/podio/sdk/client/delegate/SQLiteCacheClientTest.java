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

import java.util.ArrayList;
import java.util.List;

import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.client.RestResult;
import com.podio.sdk.client.cache.SQLiteCacheClient;
import com.podio.sdk.client.cache.SQLiteHelper;

public class SQLiteCacheClientTest extends InstrumentationTestCase {

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Delete any previously created database.
        Instrumentation instrumentation = getInstrumentation();
        Context context = instrumentation.getTargetContext();
        context.deleteDatabase(DATABASE_NAME);
    }

    /**
     * Verifies that the content table is reset when the database is downgraded
     * to an older version.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteCacheClient} object.
     * 
     * 2. Add some data to the content table.
     * 
     * 3. Create a new {@link SQLiteCacheClient} object, with a lower version.
     * 
     * 4. Verify that the delegate object with lower version has properly reset
     *      the tables.
     * 
     * </pre>
     */
    public void testContentTableResetOnVersionDowngrade() {
        // Insert test data.
        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        SQLiteDatabase sqliteDatabase = cacheClient.getWritableDatabase();
        
        String sql = "INSERT INTO %s (%s, %s) VALUES ('a://b', '{c:d}')";
        sqliteDatabase.execSQL(String.format(sql, SQLiteHelper.TABLE, SQLiteHelper.KEY_COLUMN, SQLiteHelper.DATA_COLUMN));

        Cursor cursor = sqliteDatabase.rawQuery(String.format("SELECT COUNT(*) FROM %s", SQLiteHelper.TABLE), null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());

        cursor.moveToFirst();
        assertEquals(1, cursor.getInt(0));

        // Verify the database is reset properly.
        SQLiteCacheClient cacheClient2 = getCacheClient(DATABASE_VERSION - 1);
        SQLiteDatabase sqliteDatabase2 = cacheClient2.getReadableDatabase();

        Cursor cursor2 = sqliteDatabase2.rawQuery(String.format("SELECT COUNT(*) FROM %s", SQLiteHelper.TABLE), null);
        assertNotNull(cursor2);
        assertEquals(1, cursor2.getCount());

        cursor2.moveToFirst();
        assertEquals(0, cursor2.getInt(0));
    }

    /**
     * Verifies that the content table is reset when the database is upgraded to
     * a newer version.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteCacheClient} object.
     * 
     * 2. Add some data to the content table.
     * 
     * 3. Create a new {@link SQLiteCacheClient} object, with a higher version.
     * 
     * 4. Verify that the delegate object with higher version has properly reset
     *      the tables.
     * 
     * </pre>
     */
    public void testContentTableResetOnVersionUpgrade() {
        // Insert test data.
        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        SQLiteDatabase sqliteDatabase = cacheClient.getWritableDatabase();
        
        String sql = "INSERT INTO %s (%s, %s) VALUES ('a://b', '{c:d}')";
        sqliteDatabase.execSQL(String.format(sql, SQLiteHelper.TABLE, SQLiteHelper.KEY_COLUMN, SQLiteHelper.DATA_COLUMN));

        Cursor cursor = sqliteDatabase.rawQuery(String.format("SELECT COUNT(*) FROM %s", SQLiteHelper.TABLE), null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());

        cursor.moveToFirst();
        assertEquals(1, cursor.getInt(0));

        // Verify the database is reset properly.
        SQLiteCacheClient cacheClient2 = getCacheClient(DATABASE_VERSION + 1);
        SQLiteDatabase sqliteDatabase2 = cacheClient2.getReadableDatabase();

        Cursor cursor2 = sqliteDatabase2.rawQuery(String.format("SELECT COUNT(*) FROM %s", SQLiteHelper.TABLE), null);
        assertNotNull(cursor2);
        assertEquals(1, cursor2.getCount());

        cursor2.moveToFirst();
        assertEquals(0, cursor2.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteCacheClient} returns a valid result to
     * a delete request even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty Uri to the database helpers delete method.
     * 
     * 2. Verify that a {@link RestResult} is returned.
     * 
     * 3. Verify that the result has a no-success flag.
     * 
     * </pre>
     */
    public void testDeleteHandlesEmptyUriCorrectly() {
        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        
        try {
        	cacheClient.delete(Uri.EMPTY.toString());
        	fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verifies that the {@link SQLiteCacheClient} returns a valid result to
     * a delete request even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on a null pointer Uri to the database helpers delete method.
     * 
     * 2. Verify that a {@link RestResult} is returned.
     * 
     * 3. Verify that the result has a no-success flag.
     * 
     * </pre>
     */
    public void testDeleteHandlesNullUriCorrectly() {
        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        
        try {
        	cacheClient.delete(null);
        	fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verifies that the {@link SQLiteCacheClient} returns the correct status
     * when a row was successfully deleted as a result of a valid URI pointing
     * to an existing row.
     * 
     * <pre>
     * 
     * 1. Insert a test row into a table.
     * 
     * 2. Create a valid Uri that points to the test row.
     * 
     * 3. Call the delete method of the database helper with the test URI.
     * 
     * 4. Verify that the result has a success flag.
     * 
     * </pre>
     */
    public void testDeleteReturnsCorrectStatusWhenRowDeleted() {
        Uri validUri = Uri.parse("content://test/uri");

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_COLUMN, validUri.toString());
        values.put(SQLiteHelper.DATA_COLUMN, "{text: 'test'}");

        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        SQLiteDatabase sqliteDatabase = cacheClient.getWritableDatabase();
        sqliteDatabase.insert("content", null, values);

        boolean deleted = cacheClient.delete(validUri.toString());
        assertTrue(deleted);
    }

    /**
     * Verifies that the {@link SQLiteCacheClient} returns the correct status
     * when there was no row to be deleted as a result of a valid URI not
     * pointing to an existing row.
     * 
     * <pre>
     * 
     * 2. Create a valid URI that doesn't point to any existing row in the table.
     * 
     * 3. Call the delete method of the database helper with the test URI.
     * 
     * 4. Verify that the result has a success flag.
     * 
     * </pre>
     */
    public void testDeleteReturnsCorrectStatusWhenNoRowDeleted() {
        Uri validUri = Uri.parse("content://test/uri");

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_COLUMN, validUri.toString() + "/not-this-one");
        values.put(SQLiteHelper.DATA_COLUMN, "{text: 'test'}");

        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        SQLiteDatabase sqliteDatabase = cacheClient.getWritableDatabase();
        sqliteDatabase.insert("content", null, values);

        boolean deleted = cacheClient.delete(validUri.toString());
        assertFalse(deleted);
    }

    /**
     * Verifies that the {@link SQLiteCacheClient} returns a valid result to
     * a get request even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty Uri to the database helpers get method.
     * 
     * 2. Verify that a {@link RestResult} is returned.
     * 
     * 3. Verify that the result has a no-success flag.
     * 
     * </pre>
     */
    public void testLoadHandlesEmptyUriCorrectly() {
        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        
        try {
        	cacheClient.load(Uri.EMPTY.toString());
        	fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verifies that the {@link SQLiteCacheClient} returns a valid result to
     * a get request even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty Uri to the database helpers get method.
     * 
     * 2. Verify that a {@link RestResult} is returned.
     * 
     * 3. Verify that the result has a no-success flag.
     * 
     * </pre>
     */
    public void testLoadHandlesNullUriCorrectly() {
        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        
        try {
        	cacheClient.load(null);
        	fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verifies that the {@link SQLiteCacheClient} returns the correct result
     * to a get request when an existing row is requested.
     * 
     * <pre>
     * 
     * 1. Insert test rows to the table.
     * 
     * 2. Request a known test row with the database helper object.
     * 
     * 3. Verify that the result has a success flag.
     * 
     * 4. Verify that the result contains the correct content.
     * 
     * </pre>
     */
    public void testLoadReturnsCorrectItem() {
        ContentValues[] values = { new ContentValues(), new ContentValues() };
        values[0].put(SQLiteHelper.KEY_COLUMN, "test://uri/app/0");
        values[0].put(SQLiteHelper.DATA_COLUMN, "{text:'test 0'}".getBytes());
        values[1].put(SQLiteHelper.KEY_COLUMN, "test://uri/app/1");
        values[1].put(SQLiteHelper.DATA_COLUMN, "{text:'test 1'}".getBytes());

        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        SQLiteDatabase sqliteDatabase = cacheClient.getWritableDatabase();
        sqliteDatabase.insert("content", null, values[0]);
        sqliteDatabase.insert("content", null, values[1]);

        byte[] result = cacheClient.load("test://uri/app/0");

        assertNotNull(result);
        assertEquals("{text:'test 0'}", new String(result));
    }

    /**
     * Verifies that the {@link SQLiteCacheClient} returns a valid result to
     * a post request even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty Uri to the database helpers post method.
     * 
     * 2. Verify that a {@link RestResult} is returned.
     * 
     * 3. Verify that the result has a no-success flag.
     * 
     * </pre>
     */
    public void testSaveHandlesEmptyUriCorrectly() {
        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        
        try {
        	cacheClient.save(Uri.EMPTY.toString(), null);
        	fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verifies that the {@link SQLiteCacheClient} returns a valid result to
     * a post request even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty Uri to the database helpers post method.
     * 
     * 2. Verify that a {@link RestResult} is returned.
     * 
     * 3. Verify that the result has a no-success flag.
     * 
     * </pre>
     */
    public void testSaveHandlesNullUriCorrectly() {
        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        
        try {
        	cacheClient.save(null, null);
        	fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verifies that the {@link SQLiteCacheClient} returns the correct status
     * when a row was successfully inserted.
     * 
     * <pre>
     * 
     * 1. Insert a test row with the database helper.
     * 
     * 2. Verify that the result has a success flag.
     * 
     * </pre>
     */
    public void testSaveReturnsCorrectStatusWhenRowAdded() {
        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        cacheClient.save("test://uri/app/", "{text: 'test'}".getBytes());
    }

    /**
     * Verifies that the database is initialized with the expected tables when
     * created.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteCacheClient} object.
     * 
     * 2. Verify that the expected tables have been created automatically.
     * 
     * </pre>
     */
    public void testTablesCreated() {
        // Query all non-system tables from the database and verify that there
        // is five of them.
        String query = "SELECT name FROM sqlite_master WHERE type = ? AND name != ? AND name != ?";
        String[] arguments = { "table", "sqlite_master", "android_metadata" };
        SQLiteCacheClient cacheClient = getCacheClient(DATABASE_VERSION);
        SQLiteDatabase sqliteDatabase = cacheClient.getWritableDatabase();
        Cursor cursor = sqliteDatabase.rawQuery(query, arguments);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());

        // Collect all table names in a list.
        List<String> tables = new ArrayList<String>();
        cursor.moveToFirst();

        do {
            String table = cursor.getString(0);
            tables.add(table);
        } while (cursor.moveToNext());

        // Verify that the list has all expected table names.
        assertTrue(tables.contains("content"));
    }

    /**
     * Creates a new {@link SQLiteCacheClient} object with the given version
     * number.
     * 
     * @param version
     *            The desired version number of the database helper.
     * @return The newly created database helper object.
     */
    private SQLiteCacheClient getCacheClient(int version) {
        return new SQLiteCacheClient(getInstrumentation().getTargetContext(),
                DATABASE_NAME, version);
    }

}
