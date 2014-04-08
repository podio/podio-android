package com.podio.sdk.parser;

import java.util.List;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import com.podio.sdk.parser.mock.ItemParserBooleanMockItem;
import com.podio.sdk.parser.mock.ItemParserDoubleMockItem;
import com.podio.sdk.parser.mock.ItemParserFloatMockItem;
import com.podio.sdk.parser.mock.ItemParserIntegerMockItem;
import com.podio.sdk.parser.mock.ItemParserLongMockItem;
import com.podio.sdk.parser.mock.ItemParserMixedMockItem;
import com.podio.sdk.parser.mock.ItemParserShortMockItem;
import com.podio.sdk.parser.mock.ItemParserStringMockItem;

public class ItemParserTest extends AndroidTestCase {

    /**
     * Verifies that class members can be parsed into ContentValues boolean
     * fields. Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock item as defined in the {@link ItemParserBooleanMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify ContentValues content integrity.
     * 
     * </pre>
     */
    public void testParseBooleanMemberSuccess() {
        ItemParserBooleanMockItem item = new ItemParserBooleanMockItem();
        ItemToContentValuesParser parser = new ItemToContentValuesParser();
        List<ContentValues> valuesList = parser.parse(item, ItemParserBooleanMockItem.class);

        assertNotNull(valuesList);
        assertEquals(1, valuesList.size());

        ContentValues values = valuesList.get(0);
        assertEquals(4, values.size());

        assertTrue(values.containsKey("real1"));
        assertTrue(values.containsKey("real2"));
        assertTrue(values.containsKey("real4"));
        assertTrue(values.containsKey("real5"));

        assertFalse(values.containsKey("phoney1"));
        assertFalse(values.containsKey("phoney4"));
        assertFalse(values.containsKey("real3"));

        assertEquals(item.phoney1, values.getAsBoolean("real1"));
        assertEquals(item.real2, values.getAsBoolean("real2"));
        assertEquals(item.phoney4, values.getAsBoolean("real4").booleanValue());
        assertEquals(item.real5, values.getAsBoolean("real5").booleanValue());
    }

    /**
     * Verifies that class members can be parsed into ContentValues double
     * fields. Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock item as defined in the {@link ItemParserDoubleMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify ContentValues content integrity.
     * 
     * </pre>
     */
    public void testParseDoubleMemberSuccess() {
        ItemParserDoubleMockItem item = new ItemParserDoubleMockItem();
        ItemToContentValuesParser parser = new ItemToContentValuesParser();
        List<ContentValues> valuesList = parser.parse(item, ItemParserDoubleMockItem.class);

        assertNotNull(valuesList);
        assertEquals(1, valuesList.size());

        ContentValues values = valuesList.get(0);
        assertEquals(4, values.size());

        assertTrue(values.containsKey("real1"));
        assertTrue(values.containsKey("real2"));
        assertTrue(values.containsKey("real4"));
        assertTrue(values.containsKey("real5"));

        assertFalse(values.containsKey("phoney1"));
        assertFalse(values.containsKey("phoney4"));
        assertFalse(values.containsKey("real3"));

        assertEquals(item.phoney1, values.getAsDouble("real1"));
        assertEquals(item.real2, values.getAsDouble("real2"));
        assertEquals(item.phoney4, values.getAsDouble("real4").doubleValue());
        assertEquals(item.real5, values.getAsDouble("real5").doubleValue());
    }

    /**
     * Verifies that class members can be parsed into ContentValues float
     * fields. Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock item as defined in the {@link ItemParserFloatMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify ContentValues content integrity.
     * 
     * </pre>
     */
    public void testParseFloatMemberSuccess() {
        ItemParserFloatMockItem item = new ItemParserFloatMockItem();
        ItemToContentValuesParser parser = new ItemToContentValuesParser();
        List<ContentValues> valuesList = parser.parse(item, ItemParserFloatMockItem.class);

        assertNotNull(valuesList);
        assertEquals(1, valuesList.size());

        ContentValues values = valuesList.get(0);
        assertEquals(4, values.size());

        assertTrue(values.containsKey("real1"));
        assertTrue(values.containsKey("real2"));
        assertTrue(values.containsKey("real4"));
        assertTrue(values.containsKey("real5"));

        assertFalse(values.containsKey("phoney1"));
        assertFalse(values.containsKey("phoney4"));
        assertFalse(values.containsKey("real3"));

        assertEquals(item.phoney1, values.getAsFloat("real1"));
        assertEquals(item.real2, values.getAsFloat("real2"));
        assertEquals(item.phoney4, values.getAsFloat("real4").floatValue());
        assertEquals(item.real5, values.getAsFloat("real5").floatValue());
    }

    /**
     * Verifies that class members can be parsed into ContentValues integer
     * fields. Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock item as defined in the {@link ItemParserIntegerMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify ContentValues content integrity.
     * 
     * </pre>
     */
    public void testParseIntegerMemberSuccess() {
        ItemParserIntegerMockItem item = new ItemParserIntegerMockItem();
        ItemToContentValuesParser parser = new ItemToContentValuesParser();
        List<ContentValues> valuesList = parser.parse(item, ItemParserIntegerMockItem.class);

        assertNotNull(valuesList);
        assertEquals(1, valuesList.size());

        ContentValues values = valuesList.get(0);
        assertEquals(4, values.size());

        assertTrue(values.containsKey("real1"));
        assertTrue(values.containsKey("real2"));
        assertTrue(values.containsKey("real4"));
        assertTrue(values.containsKey("real5"));

        assertFalse(values.containsKey("phoney1"));
        assertFalse(values.containsKey("phoney4"));
        assertFalse(values.containsKey("real3"));

        assertEquals(item.phoney1, values.getAsInteger("real1"));
        assertEquals(item.real2, values.getAsInteger("real2"));
        assertEquals(item.phoney4, values.getAsInteger("real4").intValue());
        assertEquals(item.real5, values.getAsInteger("real5").intValue());
    }

    /**
     * Verifies that class members can be parsed into ContentValues long fields.
     * Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock item as defined in the {@link ItemParserLongMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify ContentValues content integrity.
     * 
     * </pre>
     */
    public void testParseLongMemberSuccess() {
        ItemParserLongMockItem item = new ItemParserLongMockItem();
        ItemToContentValuesParser parser = new ItemToContentValuesParser();
        List<ContentValues> valuesList = parser.parse(item, ItemParserLongMockItem.class);

        assertNotNull(valuesList);
        assertEquals(1, valuesList.size());

        ContentValues values = valuesList.get(0);
        assertEquals(4, values.size());

        assertTrue(values.containsKey("real1"));
        assertTrue(values.containsKey("real2"));
        assertTrue(values.containsKey("real4"));
        assertTrue(values.containsKey("real5"));

        assertFalse(values.containsKey("phoney1"));
        assertFalse(values.containsKey("phoney4"));
        assertFalse(values.containsKey("real3"));

        assertEquals(item.phoney1, values.getAsLong("real1"));
        assertEquals(item.real2, values.getAsLong("real2"));
        assertEquals(item.phoney4, values.getAsLong("real4").longValue());
        assertEquals(item.real5, values.getAsLong("real5").longValue());
    }

    /**
     * Verifies that class members can be parsed into ContentValues mixed typed
     * fields. Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock item as defined in the {@link ItemParserMixedMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify ContentValues content integrity.
     * 
     * </pre>
     */
    public void testParseMixedMemberSuccess() {
        ItemParserMixedMockItem item = new ItemParserMixedMockItem();
        ItemToContentValuesParser parser = new ItemToContentValuesParser();
        List<ContentValues> valuesList = parser.parse(item, ItemParserMixedMockItem.class);

        assertNotNull(valuesList);
        assertEquals(1, valuesList.size());

        ContentValues values = valuesList.get(0);
        assertEquals(4, values.size());

        assertTrue(values.containsKey("real1"));
        assertTrue(values.containsKey("real2"));
        assertTrue(values.containsKey("real3"));
        assertTrue(values.containsKey("real4"));

        assertFalse(values.containsKey("phoney1"));
        assertFalse(values.containsKey("phoney4"));
        assertFalse(values.containsKey("real5"));

        assertEquals(item.phoney1, values.getAsBoolean("real1"));
        assertEquals(item.real2, values.getAsFloat("real2"));
        assertEquals(item.real3, values.getAsLong("real3"));
        assertEquals(item.phoney4, values.getAsString("real4"));
    }

    /**
     * Verifies that class members can be parsed into ContentValues short
     * fields. Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock item as defined in the {@link ItemParserShortMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify ContentValues content integrity.
     * 
     * </pre>
     */
    public void testParseShortMemberSuccess() {
        ItemParserShortMockItem item = new ItemParserShortMockItem();
        ItemToContentValuesParser parser = new ItemToContentValuesParser();
        List<ContentValues> valuesList = parser.parse(item, ItemParserShortMockItem.class);

        assertNotNull(valuesList);
        assertEquals(1, valuesList.size());

        ContentValues values = valuesList.get(0);
        assertEquals(4, values.size());

        assertTrue(values.containsKey("real1"));
        assertTrue(values.containsKey("real2"));
        assertTrue(values.containsKey("real4"));
        assertTrue(values.containsKey("real5"));

        assertFalse(values.containsKey("phoney1"));
        assertFalse(values.containsKey("phoney4"));
        assertFalse(values.containsKey("real3"));

        assertEquals(item.phoney1, values.getAsShort("real1"));
        assertEquals(item.real2, values.getAsShort("real2"));
        assertEquals(item.phoney4, values.getAsShort("real4").shortValue());
        assertEquals(item.real5, values.getAsShort("real5").shortValue());
    }

    /**
     * Verifies that class members can be parsed into ContentValues string
     * fields. Verifies both plain fields and annotated fields.
     * 
     * <pre>
     * 
     * 1. Parse the predefined mock item as defined in the {@link ItemParserStringMockItem} class.
     * 
     * 2. Verify parsed list size integrity.
     * 
     * 3. Verify ContentValues content integrity.
     * 
     * </pre>
     */
    public void testParseStringMemberSuccess() {
        ItemParserStringMockItem item = new ItemParserStringMockItem();
        ItemToContentValuesParser parser = new ItemToContentValuesParser();
        List<ContentValues> valuesList = parser.parse(item, ItemParserStringMockItem.class);

        assertNotNull(valuesList);
        assertEquals(1, valuesList.size());

        ContentValues values = valuesList.get(0);
        assertEquals(2, values.size());

        assertTrue(values.containsKey("real1"));
        assertTrue(values.containsKey("real2"));
        assertFalse(values.containsKey("phoney1"));
        assertFalse(values.containsKey("real3"));

        assertEquals(item.phoney1, values.getAsString("real1"));
        assertEquals(item.real2, values.getAsString("real2"));
    }

}
