package com.podio.sdk.client.database;

import java.util.ArrayList;
import java.util.List;

import com.podio.sdk.client.database.SQLiteClientDelegate;

import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.InstrumentationTestCase;

public class SQLiteClientDelegateTest extends InstrumentationTestCase {

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
     * Verifies that the {@link SQLiteClientDelegate} returns a valid result cursor
     * even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the database helpers "delete" method.
     * 
     * 2. Verify that a cursor is returned.
     * 
     * 3. Verify that the cursor has a "count" column.
     * 
     * 4. Verify that the cursor has a single row.
     * 
     * 5. Verify that the cursor "count" value is zero.
     * 
     * </pre>
     */
    public void testDeleteHandlesEmptyUriCorrectly() {
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor result = databaseHelper.delete(Uri.EMPTY);
        assertNotNull(result);
        assertEquals(0, result.getColumnIndex("count"));
        assertEquals(1, result.getCount());
        assertTrue(result.moveToFirst());
        assertEquals(0, result.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns a valid result cursor
     * even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the database helpers "delete" method.
     * 
     * 2. Verify that a cursor is returned.
     * 
     * 3. Verify that the cursor has a "count" column.
     * 
     * 4. Verify that the cursor has a single row.
     * 
     * 5. Verify that the cursor "count" value is zero.
     * 
     * </pre>
     */
    public void testDeleteHandlesNullUriCorrectly() {
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor result = databaseHelper.delete(null);
        assertNotNull(result);
        assertEquals(0, result.getColumnIndex("count"));
        assertEquals(1, result.getCount());
        assertTrue(result.moveToFirst());
        assertEquals(0, result.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns the correct count in the
     * result cursor when a row was successfully deleted as a result of a valid
     * URI pointing to an existing row.
     * 
     * <pre>
     * 
     * 1. Insert a test row into the task table.
     * 
     * 2. Create a valid URI that points to the test row.
     * 
     * 3. Call the delete method of the database helper with the test URI.
     * 
     * 4. Verify that the result cursor contains a "count" value equal to 1.
     * 
     * </pre>
     */
    public void testDeleteReturnsCorrectCountWhenRowDeleted() {
        ContentValues values = new ContentValues();
        values.put("text", "test_text");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        SQLiteDatabase sqliteDatabase = databaseHelper.getWritableDatabase();
        long id = sqliteDatabase.insert("task", null, values);

        Uri validUri = Uri.parse("test://uri/task/?_id=" + id);
        Cursor result = databaseHelper.delete(validUri);
        assertNotNull(result);
        assertEquals(0, result.getColumnIndex("count"));
        assertTrue(result.moveToFirst());
        assertEquals(1, result.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns the correct count in the
     * result cursor when a row couldn't be deleted as a result of a valid URI
     * not pointing to an existing row.
     * 
     * <pre>
     * 
     * 2. Create a valid URI that doesn't point to any existing row in the table.
     * 
     * 3. Call the delete method of the database helper with the test URI.
     * 
     * 4. Verify that the result cursor contains a "count" value equal to 0.
     * 
     * </pre>
     */
    public void testDeleteReturnsCorrectCountWhenNoRowDeleted() {
        Uri invalidUri = Uri.parse("test://uri/task/?_id=-1");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor result = databaseHelper.delete(invalidUri);
        assertNotNull(result);
        assertEquals(0, result.getColumnIndex("count"));
        assertTrue(result.moveToFirst());
        assertEquals(0, result.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns a valid result cursor
     * even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the database helpers "insert" method.
     * 
     * 2. Verify that a cursor is returned.
     * 
     * 3. Verify that the cursor has an "id" column.
     * 
     * 4. Verify that the cursor has a single row.
     * 
     * 5. Verify that the cursor "id" value is zero.
     * 
     * </pre>
     */
    public void testInsertHandlesEmptyUriCorrectly() {
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor result = databaseHelper.insert(Uri.EMPTY, null);
        assertNotNull(result);
        assertEquals(0, result.getColumnIndex("id"));
        assertEquals(1, result.getCount());
        assertTrue(result.moveToFirst());
        assertEquals(0, result.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns a valid result cursor
     * even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the database helpers "insert" method.
     * 
     * 2. Verify that a cursor is returned.
     * 
     * 3. Verify that the cursor has an "id" column.
     * 
     * 4. Verify that the cursor has a single row.
     * 
     * 5. Verify that the cursor "id" value is zero.
     * 
     * </pre>
     */
    public void testInsertHandlesNullUriCorrectly() {
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor result = databaseHelper.insert(null, null);
        assertNotNull(result);
        assertEquals(0, result.getColumnIndex("id"));
        assertEquals(1, result.getCount());
        assertTrue(result.moveToFirst());
        assertEquals(0, result.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns the correct id in the
     * result cursor when a row was successfully inserted.
     * 
     * <pre>
     * 
     * 1. Insert a test row with the database helper.
     * 
     * 2. Verify that a result cursor is returned.
     * 
     * 3. Verify that the cursor has an "id" column.
     * 
     * 4. Verify that the cursor has a single row.
     * 
     * 5. Verify that the cursor "id" value is valid.
     * 
     * </pre>
     */
    public void testInsertReturnsCorrectIdWhenRowAdded() {
        ContentValues values = new ContentValues();
        values.put("text", "test_text");
        Uri uri = Uri.parse("test://uri/task/");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor result = databaseHelper.insert(uri, values);
        assertNotNull(result);
        assertEquals(0, result.getColumnIndex("id"));
        assertTrue(result.moveToFirst());

        String query = "SELECT _id FROM task WHERE text=?";
        String[] arguments = { "test_text" };
        SQLiteDatabase sqliteDatabase = databaseHelper.getReadableDatabase();
        Cursor reference = sqliteDatabase.rawQuery(query, arguments);
        reference.moveToFirst();

        assertEquals(reference.getInt(0), result.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns a valid result cursor
     * even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the database helpers "query" method.
     * 
     * 2. Verify that a cursor is returned.
     * 
     * 3. Verify that the cursor has no columns.
     * 
     * 4. Verify that the cursor has no rows.
     * 
     * </pre>
     */
    public void testQueryHandlesEmptyUriCorrectly() {
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor result = databaseHelper.query(Uri.EMPTY);
        assertNotNull(result);
        assertEquals(0, result.getColumnCount());
        assertEquals(0, result.getCount());
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns a valid result cursor
     * even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the database helpers "query" method.
     * 
     * 2. Verify that a cursor is returned.
     * 
     * 3. Verify that the cursor has no columns.
     * 
     * 4. Verify that the cursor has no rows.
     * 
     * </pre>
     */
    public void testQueryHandlesNullUriCorrectly() {
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor result = databaseHelper.query(null);
        assertNotNull(result);
        assertEquals(0, result.getColumnCount());
        assertEquals(0, result.getCount());
    }

    /**
     * Verifies that a query parameter with an equals operator is respected when
     * querying for content.
     * 
     * <pre>
     * 
     * 1. Insert two test rows with "_id" = 1 and 2 in the task table.
     * 
     * 2. Query for all rows with "_id" == 1.
     * 
     * 3. Verify that the expected test row is returned.
     * 
     * </pre>
     */
    public void testQueryParameterEqualsOperatorIsRespected() {
        ContentValues values1 = new ContentValues();
        values1.put("_id", 1L);

        ContentValues values2 = new ContentValues();
        values2.put("_id", 2L);

        Uri insertUri = Uri.parse("test://uri/task/");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        databaseHelper.insert(insertUri, values1);
        databaseHelper.insert(insertUri, values2);

        Uri uri = Uri.parse("test://uri/task/?_id==1");
        Cursor cursor = databaseHelper.query(uri);
        cursor.moveToFirst();
        assertEquals(1, cursor.getCount());
        assertEquals(1L, cursor.getLong(cursor.getColumnIndex("_id")));
    }

    /**
     * Verifies that a query parameter with a less than operator is respected
     * when querying for content.
     * 
     * <pre>
     * 
     * 1. Insert two test rows with "_id" = 1 and 2 in the task table.
     * 
     * 2. Query for all rows with "_id" < 2.
     * 
     * 3. Verify that the expected test row is returned.
     * 
     * </pre>
     */
    public void testQueryParameterEscapeOperatorIsRespected() {
        ContentValues values1 = new ContentValues();
        values1.put("text", "=sign");
        values1.put("_id", 1L);

        ContentValues values2 = new ContentValues();
        values2.put("text", "sign");
        values2.put("_id", 2L);

        Uri insertUri = Uri.parse("test://uri/task/");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        databaseHelper.insert(insertUri, values1);
        databaseHelper.insert(insertUri, values2);

        Uri uri = Uri.parse("test://uri/task/?text=\\=sign");
        Cursor cursor = databaseHelper.query(uri);
        cursor.moveToFirst();
        assertEquals(1, cursor.getCount());
        assertEquals(1L, cursor.getLong(cursor.getColumnIndex("_id")));
    }

    /**
     * Verifies that a query parameter with a greater than operator is respected
     * when querying for content.
     * 
     * <pre>
     * 
     * 1. Insert two test rows with "_id" = 1 and 2 in the task table.
     * 
     * 2. Query for all rows with "_id" > 1.
     * 
     * 3. Verify that the expected test row is returned.
     * 
     * </pre>
     */
    public void testQueryParameterGreaterThanOperatorIsRespected() {
        ContentValues values1 = new ContentValues();
        values1.put("_id", 1L);

        ContentValues values2 = new ContentValues();
        values2.put("_id", 2L);

        Uri insertUri = Uri.parse("test://uri/task/");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        databaseHelper.insert(insertUri, values1);
        databaseHelper.insert(insertUri, values2);

        Uri uri = Uri.parse("test://uri/task/?_id=>1");
        Cursor cursor = databaseHelper.query(uri);
        cursor.moveToFirst();
        assertEquals(1, cursor.getCount());
        assertEquals(2L, cursor.getLong(cursor.getColumnIndex("_id")));
    }

    /**
     * Verifies that a query parameter with a less than operator is respected
     * when querying for content.
     * 
     * <pre>
     * 
     * 1. Insert two test rows with "_id" = 1 and 2 in the task table.
     * 
     * 2. Query for all rows with "_id" < 2.
     * 
     * 3. Verify that the expected test row is returned.
     * 
     * </pre>
     */
    public void testQueryParameterLessThanOperatorIsRespected() {
        ContentValues values1 = new ContentValues();
        values1.put("_id", 1L);

        ContentValues values2 = new ContentValues();
        values2.put("_id", 2L);

        Uri insertUri = Uri.parse("test://uri/task/");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        databaseHelper.insert(insertUri, values1);
        databaseHelper.insert(insertUri, values2);

        Uri uri = Uri.parse("test://uri/task/?_id=<2");
        Cursor cursor = databaseHelper.query(uri);
        cursor.moveToFirst();
        assertEquals(1, cursor.getCount());
        assertEquals(1L, cursor.getLong(cursor.getColumnIndex("_id")));
    }

    /**
     * Verifies that multiple instances of the same query parameters are
     * composed as a boolean OR chain.
     * 
     * <pre>
     * 
     * 1. Insert three test rows with "_id" = 1, 2 and 3 in the task table.
     * 
     * 2. Build a URI which has two definitions for the "_id" query parameter,
     *      like ?_id=1&_id=3.
     * 
     * 3. Verify that the expected test rows are returned.
     * </pre>
     */
    public void testQueryParameterOrChainedOnMultipleDefinitions() {
        ContentValues values1 = new ContentValues();
        values1.put("_id", 1L);

        ContentValues values2 = new ContentValues();
        values2.put("_id", 2L);

        ContentValues values3 = new ContentValues();
        values3.put("_id", 3L);

        Uri insertUri = Uri.parse("test://uri/task/");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        databaseHelper.insert(insertUri, values1);
        databaseHelper.insert(insertUri, values2);
        databaseHelper.insert(insertUri, values3);

        Uri uri = Uri.parse("test://uri/task/?_id=1&_id=3");
        Cursor cursor = databaseHelper.query(uri);
        cursor.moveToFirst();
        assertEquals(2, cursor.getCount());
        assertEquals(1L, cursor.getLong(cursor.getColumnIndex("_id")));
        cursor.moveToNext();
        assertEquals(3L, cursor.getLong(cursor.getColumnIndex("_id")));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns the correct item when an
     * existing row is requested.
     * 
     * <pre>
     * 
     * 1. Insert a test row to the task table.
     * 
     * 2. Request the test row with the database helper object.
     * 
     * 3. Verify that the cursor has an "id" column with the correct id.
     * 
     * 4. Verify that the cursor has a "text" column with the correct text.
     * 
     * </pre>
     */
    public void testQueryReturnsCorrectItem() {
        ContentValues values = new ContentValues();
        values.put("text", "test_text");
        Uri insertUri = Uri.parse("test://uri/task/");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor insertResult = databaseHelper.insert(insertUri, values);
        insertResult.moveToFirst();
        long id = insertResult.getLong(0);

        Uri uri = Uri.parse("test://uri/task/?_id=" + id);
        Cursor result = databaseHelper.query(uri);

        assertNotNull(result);
        result.moveToFirst();

        assertEquals(id, result.getLong(result.getColumnIndex("_id")));
        assertEquals("test_text", result.getString(result.getColumnIndex("text")));
    }

    /**
     * Verifies that the database is initialized with the expected tables when
     * created.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteClientDelegate} object.
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
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        SQLiteDatabase sqliteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqliteDatabase.rawQuery(query, arguments);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertEquals(true, cursor.moveToFirst());

        // Collect all table names in a list.
        List<String> tables = new ArrayList<String>();
        do {
            String table = cursor.getString(0);
            tables.add(table);
        } while (cursor.moveToNext());

        // Verify that the list has all expected table names.
        assertTrue(tables.contains("task"));
    }

    /**
     * Verifies that the task table is reset when the database is downgraded to
     * a newer version.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteClientDelegate} object.
     * 
     * 2. Add some data to the table.
     * 
     * 3. Create a new {@link SQLiteClientDelegate} object, with a lower version.
     * 
     * 4. Verify that table is properly reset.
     * 
     * </pre>
     */
    public void testTaskTableResetOnVersionDowngrade() {
        // Insert test data.
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        SQLiteDatabase sqliteDatabase = databaseHelper.getWritableDatabase();
        sqliteDatabase.execSQL("INSERT INTO task (text) VALUES ('test_task')");
        Cursor cursor = sqliteDatabase.rawQuery("SELECT COUNT(task.text) FROM task", null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1, cursor.getInt(0));

        // Verify the database is reset properly.
        SQLiteClientDelegate databaseHelper2 = getDatabaseHelper(DATABASE_VERSION - 1);
        SQLiteDatabase sqliteDatabase2 = databaseHelper2.getReadableDatabase();
        Cursor cursor2 = sqliteDatabase2.rawQuery("SELECT COUNT(task.text) FROM task", null);
        assertNotNull(cursor2);
        assertEquals(1, cursor2.getCount());
        assertTrue(cursor2.moveToFirst());
        assertEquals(0, cursor2.getInt(0));
    }

    /**
     * Verifies that the task table is reset when the database is upgraded to a
     * newer version.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SQLiteClientDelegate} object.
     * 
     * 2. Add some data to the table.
     * 
     * 3. Create a new {@link SQLiteClientDelegate} object, with a higher version.
     * 
     * 4. Verify that table is properly reset.
     * 
     * </pre>
     */
    public void testTaskTableResetOnVersionUpgrade() {
        // Insert test data.
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        SQLiteDatabase sqliteDatabase = databaseHelper.getWritableDatabase();
        sqliteDatabase.execSQL("INSERT INTO task (text) VALUES ('test_task')");
        Cursor cursor = sqliteDatabase.rawQuery("SELECT COUNT(task.text) FROM task", null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1, cursor.getInt(0));

        // Verify the database is reset properly.
        SQLiteClientDelegate databaseHelper2 = getDatabaseHelper(DATABASE_VERSION + 1);
        SQLiteDatabase sqliteDatabase2 = databaseHelper2.getReadableDatabase();
        Cursor cursor2 = sqliteDatabase2.rawQuery("SELECT COUNT(task.text) FROM task", null);
        assertNotNull(cursor2);
        assertEquals(1, cursor2.getCount());
        assertTrue(cursor2.moveToFirst());
        assertEquals(0, cursor2.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns a valid result cursor
     * even if the caller provides an empty URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the database helpers "update" method.
     * 
     * 2. Verify that a cursor is returned.
     * 
     * 3. Verify that the cursor has a "count" column.
     * 
     * 4. Verify that the cursor has a single row.
     * 
     * 5. Verify that the cursor "count" value is zero.
     * 
     * </pre>
     */
    public void testUpdateHandlesEmptyUriCorrectly() {
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor result = databaseHelper.update(Uri.EMPTY, null);
        assertNotNull(result);
        assertEquals(0, result.getColumnIndex("count"));
        assertEquals(1, result.getCount());
        assertTrue(result.moveToFirst());
        assertEquals(0, result.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns a valid result cursor
     * even if the caller provides a null-pointer URI.
     * 
     * <pre>
     * 
     * 1. Pass on an empty URI to the database helpers "update" method.
     * 
     * 2. Verify that a cursor is returned.
     * 
     * 3. Verify that the cursor has a "count" column.
     * 
     * 4. Verify that the cursor has a single row.
     * 
     * 5. Verify that the cursor "count" value is zero.
     * 
     * </pre>
     */
    public void testUpdateHandlesNullUriCorrectly() {
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor result = databaseHelper.update(null, null);
        assertNotNull(result);
        assertEquals(0, result.getColumnIndex("count"));
        assertEquals(1, result.getCount());
        assertTrue(result.moveToFirst());
        assertEquals(0, result.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns the correct count in the
     * result cursor when a row was successfully updated.
     * 
     * <pre>
     * 
     * 1. Insert a test row into the task table.
     * 
     * 2. Update the test row with a new value.
     * 
     * 3. Verify that the correct "count" value is reported.
     * 
     * </pre>
     */
    public void testUpdateReturnsCorrectCountWhenRowUpdated() {
        ContentValues insertValues = new ContentValues();
        insertValues.put("text", "test_text");
        Uri insertUri = Uri.parse("test://uri/task/");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor insertResult = databaseHelper.insert(insertUri, insertValues);
        insertResult.moveToFirst();
        long id = insertResult.getLong(0);

        Uri uri = Uri.parse("test://uri/task/?_id=" + id);
        ContentValues values = new ContentValues();
        values.put("text", "text_test");
        Cursor result = databaseHelper.update(uri, values);
        assertNotNull(result);
        assertEquals(0, result.getColumnIndex("count"));
        assertEquals(1, result.getCount());
        assertTrue(result.moveToFirst());
        assertEquals(1, result.getInt(0));
    }

    /**
     * Verifies that the {@link SQLiteClientDelegate} returns the updated values when
     * a row was successfully updated.
     * 
     * <pre>
     * 
     * 1. Insert a test row into the task table.
     * 
     * 2. Update the test row with a new value.
     * 
     * 3. Verify that the updated values are returned once requested.
     * 
     * </pre>
     */
    public void testUpdateReturnsCorrectValuesAfterRowUpdated() {
        ContentValues insertValues = new ContentValues();
        insertValues.put("text", "test_text");
        Uri insertUri = Uri.parse("test://uri/task/");
        SQLiteClientDelegate databaseHelper = getDatabaseHelper(DATABASE_VERSION);
        Cursor insertResult = databaseHelper.insert(insertUri, insertValues);
        insertResult.moveToFirst();
        long id = insertResult.getLong(0);

        Uri uri = Uri.parse("test://uri/task/?_id=" + id);
        ContentValues values = new ContentValues();
        values.put("text", "text_test");
        databaseHelper.update(uri, values);

        String query = "SELECT text FROM task WHERE _id=?";
        String[] arguments = { Long.toString(id, 10) };
        SQLiteDatabase sqliteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.rawQuery(query, arguments);
        assertNotNull(cursor);
        assertEquals(0, cursor.getColumnIndex("text"));
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(values.getAsString("text"), cursor.getString(0));
    }

    /**
     * Creates a new {@link SQLiteClientDelegate} object with the given version
     * number.
     * 
     * @param version
     *            The desired version number of the database helper.
     * @return The newly created database helper object.
     */
    private SQLiteClientDelegate getDatabaseHelper(int version) {
        Instrumentation instrumentation = getInstrumentation();
        Context context = instrumentation.getTargetContext();
        return new SQLiteClientDelegate(context, DATABASE_NAME, version);
    }
}
