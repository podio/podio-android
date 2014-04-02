package com.podio.sdk.parser;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.test.AndroidTestCase;

import com.podio.sdk.parser.mock.CursorParserBooleanMockItem;
import com.podio.sdk.parser.mock.CursorParserDoubleMockItem;
import com.podio.sdk.parser.mock.CursorParserFloatMockItem;
import com.podio.sdk.parser.mock.CursorParserIntegerMockItem;
import com.podio.sdk.parser.mock.CursorParserLongMockItem;
import com.podio.sdk.parser.mock.CursorParserMixedMockItem;
import com.podio.sdk.parser.mock.CursorParserMockItem;
import com.podio.sdk.parser.mock.CursorParserShortMockItem;
import com.podio.sdk.parser.mock.CursorParserStringMockItem;

public class CursorParserTest extends AndroidTestCase {

    private static final String[] COLUMNS = { "existingPublicPlain", "existingPublicAnnotated", "existingPrivatePlain", "existingPrivateAnnotated" };

    private static void assertSuccess(String failMessage, Object[] expectedExistingFields, Object[] expectedNonExistingFields, Object target) {
        CursorParserMockItem<?> actual = (CursorParserMockItem<?>) target;

        assertEquals(failMessage, expectedExistingFields[0], actual.getExistingPublicPlain());
        assertEquals(failMessage, expectedExistingFields[1], actual.getExistingPublicAnnotated());
        assertEquals(failMessage, expectedExistingFields[2], actual.getExistingPrivatePlain());
        assertEquals(failMessage, expectedExistingFields[3], actual.getExistingPrivateAnnotated());

        assertEquals(failMessage, expectedNonExistingFields[0], actual.getNonExistingPublicPlain());
        assertEquals(failMessage, expectedNonExistingFields[1], actual.getNonExistingPublicAnnotated());
        assertEquals(failMessage, expectedNonExistingFields[2], actual.getNonExistingPrivatePlain());
        assertEquals(failMessage, expectedNonExistingFields[3], actual.getNonExistingPrivateAnnotated());
    }

    private Cursor prepareCursor(List<Object[]> rows) {
        MatrixCursor cursor = new MatrixCursor(COLUMNS);

        if (rows != null) {
            for (Object[] values : rows) {
                if (values != null && values.length == COLUMNS.length) {
                    cursor.addRow(values);
                }
            }
        }

        return cursor;
    }

    private Cursor prepareCursor(Object[] row) {
        List<Object[]> rows = null;

        if (row != null) {
            rows = new ArrayList<Object[]>();
            rows.add(row);
        }

        return prepareCursor(rows);
    }

    /**
     * Verifies that Cursor columns can be parsed into Item boolean fields.
     * Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock Cursor as defined in the {@link CursorParserBooleanMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify field content integrity.
     * 
     * </pre>
     */
    public void testParseBooleanMemberSuccess() {
        Boolean[] values = { true, false, false, true };
        Boolean[] defaults = { false, false, false, false };

        Cursor cursor = prepareCursor(values);
        CursorItemParser parser = new CursorItemParser();
        List<?> items = parser.parse(cursor, CursorParserBooleanMockItem.class);

        String failMessage = "Boolean test failed";
        int expectedSize = 1;
        int actualCursorSize = cursor.getCount();
        int actualItemsSize = items.size();

        assertEquals(failMessage, expectedSize, actualCursorSize);
        assertEquals(failMessage, expectedSize, actualItemsSize);
        assertSuccess(failMessage, values, defaults, items.get(0));
    }

    /**
     * Verifies that Cursor columns can be parsed into Item double fields.
     * Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock Cursor as defined in the {@link CursorParserDoubleMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify field content integrity.
     * 
     * </pre>
     */
    public void testParseDoubleMemberSuccess() {
        Double[] values = { 10.0d, 11.0d, 12.0d, 13.0d };
        Double[] defaults = { 0.0d, 0.0d, 0.0d, 0.0d };

        Cursor cursor = prepareCursor(values);
        CursorItemParser parser = new CursorItemParser();
        List<?> items = parser.parse(cursor, CursorParserDoubleMockItem.class);

        String failMessage = "Double test failed";
        int expectedSize = 1;
        int actualCursorSize = cursor.getCount();
        int actualItemsSize = items.size();

        assertEquals(failMessage, expectedSize, actualCursorSize);
        assertEquals(failMessage, expectedSize, actualItemsSize);
        assertSuccess(failMessage, values, defaults, items.get(0));
    }

    /**
     * Verifies that Cursor columns can be parsed into Item float fields.
     * Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock Cursor as defined in the {@link CursorParserFloatMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify field content integrity.
     * 
     * </pre>
     */
    public void testParseFloatMemberSuccess() {
        Float[] values = { 1.0f, 1.1f, 1.2f, 1.3f };
        Float[] defaults = { 0.0f, 0.0f, 0.0f, 0.0f };

        Cursor cursor = prepareCursor(values);
        CursorItemParser parser = new CursorItemParser();
        List<?> items = parser.parse(cursor, CursorParserFloatMockItem.class);

        String failMessage = "Float test failed";
        int expectedSize = 1;
        int actualCursorSize = cursor.getCount();
        int actualItemsSize = items.size();

        assertEquals(failMessage, expectedSize, actualCursorSize);
        assertEquals(failMessage, expectedSize, actualItemsSize);
        assertSuccess(failMessage, values, defaults, items.get(0));
    }

    /**
     * Verifies that Cursor columns can be parsed into Item int fields. Verifies
     * both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock Cursor as defined in the {@link CursorParserIntegerMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify field content integrity.
     * 
     * </pre>
     */
    public void testParseIntegerMemberSuccess() {
        Integer[] values = { 10, 11, 12, 13 };
        Integer[] defaults = { 0, 0, 0, 0 };

        Cursor cursor = prepareCursor(values);
        CursorItemParser parser = new CursorItemParser();
        List<?> items = parser.parse(cursor, CursorParserIntegerMockItem.class);

        String failMessage = "Integer test failed";
        int expectedSize = 1;
        int actualCursorSize = cursor.getCount();
        int actualItemsSize = items.size();

        assertEquals(failMessage, expectedSize, actualCursorSize);
        assertEquals(failMessage, expectedSize, actualItemsSize);
        assertSuccess(failMessage, values, defaults, items.get(0));
    }

    /**
     * Verifies that Cursor columns can be parsed into Item long fields.
     * Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock Cursor as defined in the {@link CursorParserLongMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify field content integrity.
     * 
     * </pre>
     */
    public void testParseLongMemberSuccess() {
        Long[] values = { 100L, 101L, 102L, 103L };
        Long[] defaults = { 0L, 0L, 0L, 0L };

        Cursor cursor = prepareCursor(values);
        CursorItemParser parser = new CursorItemParser();
        List<?> items = parser.parse(cursor, CursorParserLongMockItem.class);

        String failMessage = "Long test failed";
        int expectedSize = 1;
        int actualCursorSize = cursor.getCount();
        int actualItemsSize = items.size();

        assertEquals(failMessage, expectedSize, actualCursorSize);
        assertEquals(failMessage, expectedSize, actualItemsSize);
        assertSuccess(failMessage, values, defaults, items.get(0));
    }

    /**
     * Verifies that Cursor columns can be parsed into Item mixed type fields.
     * Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock Cursor as defined in the {@link CursorParserMixedMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify field content integrity.
     * 
     * </pre>
     */
    public void testParseMixedMemberSuccess() {
        Object[] values = { "test1", 101L, 1.2f, true };
        Object[] defaults = { null, 0L, 0.0f, false };

        Cursor cursor = prepareCursor(values);
        CursorItemParser parser = new CursorItemParser();
        List<?> items = parser.parse(cursor, CursorParserMixedMockItem.class);

        String failMessage = "Mixed test failed";
        int expectedSize = 1;
        int actualCursorSize = cursor.getCount();
        int actualItemsSize = items.size();

        assertEquals(failMessage, expectedSize, actualCursorSize);
        assertEquals(failMessage, expectedSize, actualItemsSize);
        assertSuccess(failMessage, values, defaults, items.get(0));
    }

    /**
     * Verifies that all rows in Cursor are parsed to items. Verifies both plain
     * fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock Cursor with multiple rows as defined in the
     *      {@link CursorParserMixedMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify field content integrity.
     * 
     * </pre>
     */
    public void testParseMultiRowCursorSuccess() {
        List<Object[]> values = new ArrayList<Object[]>();
        values.add(new Object[] { "test1", 101L, 1.1f, true });
        values.add(new Object[] { "test2", 102L, 1.2f, true });
        values.add(new Object[] { "test3", 103L, 1.3f, true });
        Object[] defaults = { null, 0L, 0.0f, false };

        Cursor cursor = prepareCursor(values);
        CursorItemParser parser = new CursorItemParser();
        List<?> items = parser.parse(cursor, CursorParserMixedMockItem.class);

        String failMessage = "Multirow Mixed test failed";
        int expectedSize = 3;
        int actualCursorSize = cursor.getCount();
        int actualItemsSize = items.size();

        assertEquals(failMessage, expectedSize, actualCursorSize);
        assertEquals(failMessage, expectedSize, actualItemsSize);

        for (int i = 0; i < actualItemsSize; i++) {
            assertSuccess(failMessage, values.get(i), defaults, items.get(i));
        }

    }

    /**
     * Verifies that Cursor columns can be parsed into Item short fields.
     * Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock Cursor as defined in the {@link CursorParserShortMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify field content integrity.
     * 
     * </pre>
     */
    public void testParseShortMemberSuccess() {
        Short[] values = { 0, 1, 2, 3 };
        Short[] defaults = { 0, 0, 0, 0 };

        Cursor cursor = prepareCursor(values);
        CursorItemParser parser = new CursorItemParser();
        List<?> items = parser.parse(cursor, CursorParserShortMockItem.class);

        String failMessage = "Integer test failed";
        int expectedSize = 1;
        int actualCursorSize = cursor.getCount();
        int actualItemsSize = items.size();

        assertEquals(failMessage, expectedSize, actualCursorSize);
        assertEquals(failMessage, expectedSize, actualItemsSize);
        assertSuccess(failMessage, values, defaults, items.get(0));
    }

    /**
     * Verifies that Cursor columns can be parsed into Item String fields.
     * Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     *  1. Parse the predefined mock Cursor as defined in the {@link CursorParserStringMockItem} class.
     *  
     *  2. Verify parsed list size integrity.
     *  
     *  3. Verify field content integrity.
     * 
     * </pre>
     */
    public void testParseStringMemberSuccess() {
        String[] values = { "test10", "test11", "test12", "test13" };
        String[] defaults = { null, null, null, null };

        Cursor cursor = prepareCursor(values);
        CursorItemParser parser = new CursorItemParser();
        List<?> items = parser.parse(cursor, CursorParserStringMockItem.class);

        String failMessage = "String test failed";
        int expectedSize = 1;
        int actualCursorSize = cursor.getCount();
        int actualItemsSize = items.size();

        assertEquals(failMessage, expectedSize, actualCursorSize);
        assertEquals(failMessage, expectedSize, actualItemsSize);
        assertSuccess(failMessage, values, defaults, items.get(0));
    }
}
