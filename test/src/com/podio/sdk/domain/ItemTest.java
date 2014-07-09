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

package com.podio.sdk.domain;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.podio.sdk.domain.field.CategoryField;
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.field.TextField;
import com.podio.sdk.domain.field.value.CategoryValue;

public class ItemTest extends AndroidTestCase {

    /**
     * Verifies that an Object value can be added to a field that previously
     * didn't have any value and hence wasn't present in the item fields list.
     * 
     * <pre>
     * 
     * 1. Create a new Item object (since we didn't fetch it from the server
     *      it will have an empty fields list).
     * 
     * 2. Add a value to an arbitrary field.
     * 
     * 3. Verify that boolean true was returned.
     * 
     * 4. Verify that the value would get pushed to the server, would we try
     *      to save it.
     * 
     * </pre>
     */
    public void testAddUnverifiedValue() {
        Item item = new Item();
        item.addValue("FIELD-1", (Object) "VALUE");

        Gson gson = new Gson();
        String actualJson = gson.toJson(item.getPushData());
        String expectedJson = "{\"fields\":{\"FIELD-1\":[{\"value\":\"VALUE\"}]}}";

        assertEquals(expectedJson, actualJson);
    }

    /**
     * Verifies that a String value can be added to a field that previously
     * didn't have any value and hence wasn't present in the item fields list.
     * 
     * <pre>
     * 
     * 1. Create a new Item object (since we didn't fetch it from the server
     *      it will have an empty fields list).
     * 
     * 2. Add a value to an arbitrary field.
     * 
     * 3. Verify that the value would get pushed to the server, would we try
     *      to save it.
     * 
     * </pre>
     */
    public void testAddUnverifiedStringValue() {
        Item item = new Item();
        Gson gson = new Gson();
        String expectedStringJson = "{\"fields\":{\"FIELD-1\":[{\"value\":\"VALUE\"}]}}";
        String expectedEmptyJson = "{\"fields\":{\"FIELD-1\":[{\"value\":\"VALUE\"},{\"value\":\"\"}]}}";

        item.addValue("FIELD-1", "VALUE");
        String actualStringJson = gson.toJson(item.getPushData());
        assertEquals(expectedStringJson, actualStringJson);

        // Empty values are not added
        item.addValue("FIELD-1", "");
        String actualEmptyJson = gson.toJson(item.getPushData());
        assertEquals(expectedEmptyJson, actualEmptyJson);
    }

    /**
     * Verifies that the {@link Item#addValue(String, Object)} method throws an
     * exception when the field name is empty.
     * 
     * <pre>
     * 
     * 1. Create a new Item object.
     * 
     * 2. Try to add any type of value to an unnamed field.
     * 
     * 3. Verify that an exception is thrown.
     * 
     * </pre>
     */
    public void testAddValueToUnnamedField() {
        Item item = new Item();

        try {
            item.addValue(null, null);
            fail("Should have thrown exception");
        } catch (Exception e) {
        }

        try {
            item.addValue(null, new Object());
            fail("Should have thrown exception");
        } catch (Exception e) {
        }

        try {
            item.addValue("", null);
            fail("Should have thrown exception");
        } catch (Exception e) {
        }

        try {
            item.addValue("", new Object());
            fail("Should have thrown exception");
        } catch (Exception e) {
        }
    }

    /**
     * Verifies that a value can be added to field that already has a value.
     * 
     * <pre>
     * 
     * 1. Create a new Item object which already has a value for a known
     *      field.
     * 
     * 2. Add a new value to the field.
     * 
     * 3. Verify that both the pre-existing value and the new value are
     *      present.
     * 
     * </pre>
     */
    public void testAddVerifiedValue() {
        String json = new StringBuilder("{")
                .append("external_id:'EXTERNALID',")
                .append("fields:[{")
                .append("  external_id:'FIELD-1',")
                .append("  values: [{")
                .append("    value: {")
                .append("      id:2")
                .append("    }")
                .append("  }],")
                .append("  config: {")
                .append("    settings: {")
                .append("      options: [{")
                .append("        id:2")
                .append("      },{")
                .append("        id:3")
                .append("      }]")
                .append("    }")
                .append("  }")
                .append("}]")
                .append("}").toString();

        Gson gson = new GsonBuilder().registerTypeAdapter(Field.class, new JsonDeserializer<Field>() {
            @Override
            public Field deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
                return gsonContext.deserialize(element, CategoryField.class);
            }
        }).create();

        Item item = gson.fromJson(json, Item.class);
        item.addValue("FIELD-1", 3);

        Object pushData = item.getPushData();
        String actualPushJson = gson.toJson(pushData);
        String expectedPushJson = "{\"external_id\":\"EXTERNALID\",\"fields\":{\"FIELD-1\":[{\"value\":2},{\"value\":3}]}}";

        assertEquals(expectedPushJson, actualPushJson);
    }

    /**
     * Verifies that the {@link Item#getFields()} doesn't return null, not even
     * if the describing JSON doesn't have a 'fields' attribute.
     * 
     * <pre>
     * 
     * 1. Create a new Item object.
     * 
     * 2. Call the getFields() method.
     * 
     * 3. Verify the result is not null.
     * 
     * </pre>
     */
    public void testFieldsNeverNull() {
        String json = "{external_id:'EXTERNAL_ID', fields:null}";
        Gson gson = new Gson();
        Item item;
        List<Field> fields;

        item = gson.fromJson(json, Item.class);
        fields = item.getFields();
        assertNotNull(fields);
        assertEquals(0, fields.size());

        item = new Item();
        fields = item.getFields();
        assertNotNull(fields);
        assertEquals(0, fields.size());
    }

    /**
     * Verifies that the SDK doesn't crash but returns null when asking for a
     * value of an unnamed field.
     * 
     * <pre>
     * 
     * 1. Create a new Item object.
     * 
     * 2. Add a value to it.
     * 
     * 3. Verify that null is returned when asking for a value of an unnamed
     *      field.
     * 
     * </pre>
     */
    public void testGetUnnamedValue() {
        Item item = new Item();
        item.addValue("FIELD-1", "VALUE-1");
        assertEquals(null, item.getValue("", 0));
        assertEquals(null, item.getValue(null, 0));
    }

    /**
     * Verifies that the value with the given index can be fetched for a known
     * field which already has values.
     * 
     * <pre>
     * 
     * 1. Create a new Item object which already has values for a known
     *      field.
     * 
     * 2. Verify that you can get each of the existing values and that they
     *      are as expected.
     * 
     * </pre>
     */
    public void testGetUnverifiedValue() {
        Item item = new Item();
        item.addValue("FIELD-1", "VALUE-1");
        item.addValue("FIELD-1", "VALUE-2");
        item.addValue("FIELD-1", "VALUE-3");

        assertEquals("VALUE-1", item.getValue("FIELD-1", 0).toString());
        assertEquals("VALUE-2", item.getValue("FIELD-1", 1).toString());
        assertEquals("VALUE-3", item.getValue("FIELD-1", 2).toString());
    }

    /**
     * Verifies that the value with the given index can be fetched for a known
     * field.
     * 
     * <pre>
     * 
     * 1. Create a new Item object which already has values for a known
     *      field.
     * 
     * 3. Verify that you can get each of the values back and that they are as
     *      expected.
     * 
     * </pre>
     */
    public void testGetVerifiedValue() {
        String json = new StringBuilder("{")
                .append("external_id:'EXTERNALID',")
                .append("fields:[{")
                .append("  external_id:'FIELD-1',")
                .append("  values: [{")
                .append("    value: {")
                .append("      id:2")
                .append("    }")
                .append("  },{")
                .append("    value: {")
                .append("      id:3")
                .append("    }")
                .append("  },{")
                .append("    value: {")
                .append("      id:4")
                .append("    }")
                .append("  }],")
                .append("  config: {")
                .append("    settings: {")
                .append("      options: [{")
                .append("        id:1")
                .append("      },{")
                .append("        id:2")
                .append("      },{")
                .append("        id:3")
                .append("      },{")
                .append("        id:4")
                .append("      }]")
                .append("    }")
                .append("  }")
                .append("}]")
                .append("}").toString();

        Gson gson = new GsonBuilder().registerTypeAdapter(Field.class, new JsonDeserializer<Field>() {
            @Override
            public Field deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
                return gsonContext.deserialize(element, CategoryField.class);
            }
        }).create();

        Item item = gson.fromJson(json, Item.class);
        CategoryValue value;

        // Verify 'getValue()'
        value = (CategoryValue) item.getValue("FIELD-1", 0);
        assertEquals(2, value.getId());

        value = (CategoryValue) item.getValue("FIELD-1", 1);
        assertEquals(3, value.getId());

        value = (CategoryValue) item.getValue("FIELD-1", 2);
        assertEquals(4, value.getId());

        // Verify 'getVerifiedValue()'
        value = (CategoryValue) item.getVerifiedValue("FIELD-1", 0);
        assertEquals(2, value.getId());

        value = (CategoryValue) item.getVerifiedValue("FIELD-1", 1);
        assertEquals(3, value.getId());

        value = (CategoryValue) item.getVerifiedValue("FIELD-1", 2);
        assertEquals(4, value.getId());
    }

    /**
     * Verifies that the {@link Item#getVerifiedValue(String, int)} method
     * doesn't return values for unverified fields.
     * 
     * <pre>
     * 
     * 1. Create a new Item object (since we didn't fetch it from the server
     *      it will have an empty fields list).
     * 
     * 2. Add a value to an arbitrary field.
     * 
     * 3. Verify that the getVerifiedValue method returns null.
     * 
     * </pre>
     */
    public void testGetVerifiedValueDoesNotReturnValuesForUnverifiedFields() {
        Item item = new Item();
        item.addValue("FIELD-1", "VALUE");
        assertEquals(null, item.getVerifiedValue("FIELD-1", 0));
    }

    /**
     * Verifies that the members of the {@link Item} class can be initialized by
     * parsing JSON.
     * 
     * <pre>
     * 
     * 1. Describe an Item domain object as a JSON string.
     * 
     * 2. Create an Item instance object from the JSON with the Gson parser.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testItemCanBeCreatedFromJson() {
        // private final String last_event_on = null;
        String json = new StringBuilder("{")
                .append("app:{},")
                .append("created_by:{},")
                .append("created_on:'2014-07-09 10:37:14',")
                .append("excerpt:{")
                .append(" label:'LABEL',")
                .append(" text:'TEXT'")
                .append("},")
                .append("external_id:'EXTERNALID',")
                .append("fields:[],")
                .append("item_id:1,")
                .append("link:'LINK',")
                .append("pinned:true,")
                .append("priority:1,")
                .append("revision:1,")
                .append("rights:['view'],")
                .append("space:{},")
                .append("subscribed:true,")
                .append("subscribed_count:1,")
                .append("tags:[],")
                .append("title:'TITLE',")
                .append("last_event_on:'2014-07-09 10:42:30'")
                .append("}").toString();
        Gson gson = new Gson();
        Item item = gson.fromJson(json, Item.class);

        assertNotNull(item);
        assertNotNull(item.getApplication());
        assertNotNull(item.getCreatedByUser());
        assertEquals("2014-07-09 10:37:14", item.getCreatedDateString());

        Date createdDate = item.getCreatedDate();
        Calendar createdCalendar = Calendar.getInstance();
        createdCalendar.setTime(createdDate);
        assertNotNull(createdDate);
        assertEquals(2014, createdCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, createdCalendar.get(Calendar.MONTH));
        assertEquals(9, createdCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(10, createdCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(37, createdCalendar.get(Calendar.MINUTE));
        assertEquals(14, createdCalendar.get(Calendar.SECOND));

        Item.Excerpt excerpt = item.getExcerpt();
        assertNotNull(excerpt);
        assertEquals("LABEL", excerpt.getLabel());
        assertEquals("TEXT", excerpt.getText());

        assertEquals("EXTERNALID", item.getExternalId());

        List<Field> fields = item.getFields();
        assertNotNull(fields);
        assertEquals(0, fields.size());

        assertEquals(1, item.getId());
        assertEquals("LINK", item.getLink());
        assertEquals(true, item.isPinned());
        assertEquals(1, item.getPriority());
        assertEquals(1, item.getRevisionId());
        assertTrue(item.hasRights(Right.view));
        assertFalse(item.hasRights(Right.add_file));
        assertNotNull(item.getWorkspace());
        assertEquals(true, item.isSubscribed());
        assertEquals(1, item.getNumberOfSubscriptions());

        List<String> tags = item.getTags();
        assertNotNull(tags);
        assertEquals(0, tags.size());

        assertEquals("TITLE", item.getTitle());
        assertEquals("2014-07-09 10:42:30", item.getLastEventDateString());

        Date eventDate = item.getLastEventDate();
        Calendar eventCalendar = Calendar.getInstance();
        eventCalendar.setTime(eventDate);
        assertNotNull(eventDate);
        assertEquals(2014, eventCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, eventCalendar.get(Calendar.MONTH));
        assertEquals(9, eventCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(10, eventCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(42, eventCalendar.get(Calendar.MINUTE));
        assertEquals(30, eventCalendar.get(Calendar.SECOND));
    }

    /**
     * Verifies that the members of the {@link Item} class can be initialized to
     * default by instantiation.
     * 
     * <pre>
     * 
     * 1. Create a new Item object instance.
     * 
     * 3. Verify that the members have the default values.
     * 
     * </pre>
     */
    public void testItemCanBeCreatedFromInstantiation() {
        Item item = new Item();

        assertNotNull(item);
        assertEquals(null, item.getApplication());
        assertEquals(null, item.getCreatedByUser());
        assertEquals(null, item.getCreatedDateString());
        assertEquals(null, item.getCreatedDate());
        assertEquals(null, item.getExcerpt());
        assertEquals(null, item.getExternalId());

        List<Field> fields = item.getFields();
        assertNotNull(fields);
        assertEquals(0, fields.size());

        assertEquals(-1, item.getId());
        assertEquals(null, item.getLink());
        assertEquals(false, item.isPinned());
        assertEquals(0, item.getPriority());
        assertEquals(-1, item.getRevisionId());
        assertEquals(false, item.hasRights(Right.view));
        assertEquals(null, item.getWorkspace());
        assertEquals(false, item.isSubscribed());
        assertEquals(0, item.getNumberOfSubscriptions());

        List<String> tags = item.getTags();
        assertNotNull(tags);
        assertEquals(0, tags.size());

        assertEquals(null, item.getTitle());
        assertEquals(null, item.getLastEventDateString());
    }

    /**
     * Verifies that the members of the {@link Item} class can be initialized to
     * defaults, with fields from a given Podio app, by instantiation.
     * 
     * <pre>
     * 
     * 1. Create a new Item object instance with an App template.
     * 
     * 3. Verify that the list of fields are the same as defined in the App.
     * 
     * </pre>
     */
    public void testItemCanBeCreatedFromInstantiationWithFieldsFromApplication() {
        String json = new StringBuilder("{")
                .append("fields:[{")
                .append("  external_id:'FIELD-1'")
                .append(" },{")
                .append("  external_id:'FIELD-2'")
                .append(" }]")
                .append("}")
                .toString();

        Gson gson = new GsonBuilder().registerTypeAdapter(Field.class, new JsonDeserializer<Field>() {
            @Override
            public Field deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
                return gsonContext.deserialize(element, TextField.class);
            }
        }).create();

        Application application = gson.fromJson(json, Application.class);
        List<Field> appFields = application.getFields();
        assertNotNull(appFields);
        assertEquals(2, appFields.size());

        Item item = new Item(application);
        List<Field> itemFields = item.getFields();
        assertNotNull(itemFields);
        assertEquals(2, itemFields.size());

        assertEquals("FIELD-1", itemFields.get(0).getExternalId());
        assertEquals("FIELD-2", itemFields.get(1).getExternalId());
    }

    /**
     * Verifies that the {@link Item} class can generate an object that
     * describes the changed values it contains.
     * 
     * <pre>
     * 
     * 1. Describe a simple Item domain object as a JSON string. Make
     *      sure it has some {@link Field} items.
     * 
     * 2. Create an instance of it from the JSON string.
     * 
     * 3. Add a value to a field that previously didn't have a value.
     *      Basically a field that wasn't described in the JSON string.
     * 
     * 4. Request the push data from the item and convert it to a
     *      JSON string.
     * 
     * 5. Verify that the JSON string corresponds to expectations.
     * 
     * </pre>
     */
    public void testItemCanGeneratePushData() {
        String json = new StringBuilder("{")
                .append("external_id:'EXTERNALID',")
                .append("fields:[{")
                .append("  external_id:'FIELD-1',")
                .append("  values: [{")
                .append("    value: {")
                .append("      id:2")
                .append("    }")
                .append("  }],")
                .append("  config: {")
                .append("    settings: {")
                .append("      options: [{")
                .append("        id:2")
                .append("      },{")
                .append("        id:3")
                .append("      }]")
                .append("    }")
                .append("  }")
                .append("}]")
                .append("}").toString();

        Gson gson = new GsonBuilder().registerTypeAdapter(Field.class, new JsonDeserializer<Field>() {
            @Override
            public Field deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
                return gsonContext.deserialize(element, CategoryField.class);
            }
        }).create();

        Item item = gson.fromJson(json, Item.class);
        item.addValue("FIELD-4", 5);

        Object pushData = item.getPushData();
        String actualPushJson = gson.toJson(pushData);
        String expectedPushJson = "{\"external_id\":\"EXTERNALID\",\"fields\":{\"FIELD-4\":[{\"value\":5}],\"FIELD-1\":[{\"value\":2}]}}";

        assertEquals(expectedPushJson, actualPushJson);
    }

    /**
     * Verifies that a unsaved value can be removed from the item.
     * 
     * <pre>
     * 
     * 1. Create a new Item object (since we didn't fetch it from the server
     *      it will have an empty fields list).
     * 
     * 2. Add some values to a known field.
     * 
     * 3. Verify that the values would be present if the item were to be saved.
     * 
     * 4. Remove a value.
     * 
     * 3. Verify that the removed value is no longer present in the push data.
     * 
     * </pre>
     */
    public void testRemoveUnverifiedValue() {
        String value = "VALUE";

        Gson gson = new Gson();
        Item item = new Item();
        item.addValue("FIELD-1", value);

        String actualPreRemoveJson = gson.toJson(item.getPushData());
        String expectedPreRemoveJson = "{\"fields\":{\"FIELD-1\":[{\"value\":\"VALUE\"}]}}";
        assertEquals(expectedPreRemoveJson, actualPreRemoveJson);

        item.removeValue("FIELD-1", value);

        String expectedPostRemoveJson = "{\"fields\":{\"FIELD-1\":[]}}";
        String actualPostRemoveJson = gson.toJson(item.getPushData());
        assertEquals(expectedPostRemoveJson, actualPostRemoveJson);
    }

    /**
     * Verifies that a previously added and saved value can be removed from the
     * item.
     * 
     * <pre>
     * 
     * 1. Create an item which already has values for a field.
     * 
     * 2. Remove a value.
     * 
     * 3. Verify that the value is no longer present in the push data.
     * 
     * </pre>
     */
    public void testRemoveVerifiedValue() {
        String json = new StringBuilder("{")
                .append("fields:[{")
                .append("  external_id:'FIELD-1',")
                .append("  values: [{")
                .append("    value: {")
                .append("      id:2")
                .append("    }")
                .append("  },{")
                .append("    value: {")
                .append("      id:3")
                .append("    }")
                .append("  }],")
                .append("  config: {")
                .append("    settings: {")
                .append("      options: [{")
                .append("        id:2")
                .append("      },{")
                .append("        id:3")
                .append("      }]")
                .append("    }")
                .append("  }")
                .append("}]")
                .append("}").toString();

        Gson gson = new GsonBuilder().registerTypeAdapter(Field.class, new JsonDeserializer<Field>() {
            @Override
            public Field deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
                return gsonContext.deserialize(element, CategoryField.class);
            }
        }).create();

        Item item = gson.fromJson(json, Item.class);
        Object preRemoveData = item.getPushData();
        String expectedPreRemoveJson = "{\"fields\":{\"FIELD-1\":[{\"value\":2},{\"value\":3}]}}";
        assertEquals(expectedPreRemoveJson, gson.toJson(preRemoveData));

        CategoryValue value = new CategoryValue(2);
        item.removeValue("FIELD-1", value);
        Object postRemoveData = item.getPushData();
        String expectedPostRemoveJson = "{\"fields\":{\"FIELD-1\":[{\"value\":3}]}}";
        assertEquals(expectedPostRemoveJson, gson.toJson(postRemoveData));
    }
}
