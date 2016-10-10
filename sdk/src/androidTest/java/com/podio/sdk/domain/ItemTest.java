package com.podio.sdk.domain;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.Suppress;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.podio.sdk.domain.field.CategoryField;
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.field.NumberField;
import com.podio.sdk.domain.field.TextField;

public class ItemTest extends AndroidTestCase {


    public void testAddUnverifiedValue() {
        Item item = new Item();
        item.addValue("FIELD-1", new TextField.Value("VALUE"));

        Gson gson = new Gson();
        String actualJson = gson.toJson(item.getCreateData(false));

        String expectedJson = "{\"fields\":{\"FIELD-1\":[{\"value\":\"VALUE\"}]}";

        assertTrue(actualJson.contains(expectedJson));
    }


    public void testAddUnverifiedStringValue() {
        Item item = new Item();
        Gson gson = new Gson();
        String expectedStringJson = "{\"fields\":{\"FIELD-1\":[{\"value\":\"VALUE\"}]}";

        item.addValue("FIELD-1", new TextField.Value("VALUE"));
        String actualStringJson = gson.toJson(item.getCreateData(false));
        assertTrue(actualStringJson.contains(expectedStringJson));

        // Empty values are not added
        item.addValue("FIELD-1", new TextField.Value(""));
        String actualEmptyJson = gson.toJson(item.getCreateData(false));
        assertTrue(actualEmptyJson.contains(expectedStringJson));
    }


    @Suppress()
    public void testAddValueToUnnamedField() {
        Item item = new Item();

        try {
            item.addValue(null, null);
            fail("Should have thrown exception");
        } catch (Exception e) {
        }

        try {
            item.addValue(null, new TextField.Value("VALUE"));
            fail("Should have thrown exception");
        } catch (Exception e) {
        }

        try {
            item.addValue("", null);
            fail("Should have thrown exception");
        } catch (Exception e) {
        }

        try {
            item.addValue("", new TextField.Value("VALUE"));
            fail("Should have thrown exception");
        } catch (Exception e) {
        }
    }


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
        item.addValue("FIELD-1", new CategoryField.Value(3));

        Object pushData = item.getCreateData(false);
        String actualPushJson = gson.toJson(pushData);
        String expectedPushJson = "{\"external_id\":\"EXTERNALID\",\"fields\":{\"FIELD-1\":[{\"value\":2},{\"value\":3}]}";

        assertTrue(actualPushJson.contains(expectedPushJson));
    }


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


    public void testGetUnnamedValue() {
        Item item = new Item();
        item.addValue("FIELD-1", new TextField.Value("VALUE"));
        assertEquals(null, item.getValue("", 0));
        assertEquals(null, item.getValue(null, 0));
    }


    public void testGetUnverifiedValue() {
        Item item = new Item();
        item.addValue("FIELD-1", new TextField.Value("VALUE-1"));
        item.addValue("FIELD-1", new TextField.Value("VALUE-2"));
        item.addValue("FIELD-1", new TextField.Value("VALUE-3"));

        assertEquals("VALUE-1", ((TextField.Value) item.getValue("FIELD-1", 0)).getValue());
        assertEquals("VALUE-2", ((TextField.Value) item.getValue("FIELD-1", 1)).getValue());
        assertEquals("VALUE-3", ((TextField.Value) item.getValue("FIELD-1", 2)).getValue());
    }


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
        CategoryField.Value value;

        // Verify 'getValue()'
        value = (CategoryField.Value) item.getValue("FIELD-1", 0);
        assertEquals(2, value.getId());

        value = (CategoryField.Value) item.getValue("FIELD-1", 1);
        assertEquals(3, value.getId());

        value = (CategoryField.Value) item.getValue("FIELD-1", 2);
        assertEquals(4, value.getId());

        // Verify 'getVerifiedValue()'
        value = (CategoryField.Value) item.getVerifiedValue("FIELD-1", 0);
        assertEquals(2, value.getId());

        value = (CategoryField.Value) item.getVerifiedValue("FIELD-1", 1);
        assertEquals(3, value.getId());

        value = (CategoryField.Value) item.getVerifiedValue("FIELD-1", 2);
        assertEquals(4, value.getId());
    }


    public void testGetVerifiedValueDoesNotReturnValuesForUnverifiedFields() {
        Item item = new Item();
        item.addValue("FIELD-1", new TextField.Value("VALUE"));
        assertEquals(null, item.getVerifiedValue("FIELD-1", 0));
    }


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
        assertNotNull(item.getCreatedBy());
        assertEquals("2014-07-09 10:37:14", item.getCreatedDateString());

        Calendar createdCalendar = Calendar.getInstance();
        createdCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        createdCalendar.setTime(item.getCreatedDate());

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

        assertEquals(1L, item.getId());
        assertEquals("LINK", item.getLink());
        assertEquals(true, item.isPinned());
        assertEquals(1.0, item.getPriority());
        assertEquals(1L, item.getRevisionId());
        assertTrue(item.hasAllRights(Right.view));
        assertNotNull(item.getSpace());
        assertEquals(true, item.isSubscribed());
        assertEquals(1, item.getSubscribedCount());

        List<String> tags = item.getTags();
        assertNotNull(tags);
        assertEquals(0, tags.size());

        assertEquals("TITLE", item.getTitle());
        assertEquals("2014-07-09 10:42:30", item.getLastEventDateString());

        Calendar eventCalendar = Calendar.getInstance();
        eventCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        eventCalendar.setTime(item.getLastEventDate());

        assertEquals(2014, eventCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, eventCalendar.get(Calendar.MONTH));
        assertEquals(9, eventCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(10, eventCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(42, eventCalendar.get(Calendar.MINUTE));
        assertEquals(30, eventCalendar.get(Calendar.SECOND));
    }


    public void testItemCanBeCreatedFromInstantiation() {
        Item item = new Item();

        assertNotNull(item);
        assertEquals(null, item.getApplication());
        assertEquals(null, item.getCreatedBy());
        assertEquals(null, item.getCreatedDateString());
        assertEquals(null, item.getCreatedDate());
        assertEquals(null, item.getExcerpt());
        assertEquals(null, item.getExternalId());

        List<Field> fields = item.getFields();
        assertNotNull(fields);
        assertEquals(0, fields.size());

        assertEquals(-1L, item.getId());
        assertEquals(null, item.getLink());
        assertEquals(false, item.isPinned());
        assertEquals(0.0, item.getPriority());
        assertEquals(-1L, item.getRevisionId());
        assertEquals(false, item.hasAllRights(Right.view));
        assertEquals(null, item.getSpace());
        assertEquals(false, item.isSubscribed());
        assertEquals(0, item.getSubscribedCount());

        List<String> tags = item.getTags();
        assertNotNull(tags);
        assertEquals(0, tags.size());

        assertEquals(null, item.getTitle());
        assertEquals(null, item.getLastEventDateString());
    }


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
        Field[] appFields = application.getTemplate();
        assertNotNull(appFields);
        assertEquals(2, appFields.length);

        Item item = new Item(application);
        List<Field> itemFields = item.getFields();
        assertNotNull(itemFields);
        assertEquals(2, itemFields.size());

        assertEquals("FIELD-1", itemFields.get(0).getExternalId());
        assertEquals("FIELD-2", itemFields.get(1).getExternalId());
    }


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
        item.addValue("FIELD-4", new NumberField.Value(5d));

        Object pushData = item.getCreateData(false);
        String actualPushJson = gson.toJson(pushData);
        String expectedPushJson = "{\"external_id\":\"EXTERNALID\",\"fields\":{\"FIELD-4\":[{\"value\":\"5.0\"}],\"FIELD-1\":[{\"value\":2}]}";

        assertTrue(actualPushJson.contains(expectedPushJson));
    }


    public void testRemoveUnverifiedValue() {
        TextField.Value value = new TextField.Value("VALUE");

        Gson gson = new Gson();
        Item item = new Item();
        item.addValue("FIELD-1", value);

        String actualPreRemoveJson = gson.toJson(item.getCreateData(false));
        String expectedPreRemoveJson = "{\"fields\":{\"FIELD-1\":[{\"value\":\"VALUE\"}]}";
        assertTrue(actualPreRemoveJson.contains(expectedPreRemoveJson));

        item.removeValue("FIELD-1", value);

        String expectedPostRemoveJson = "{\"fields\":{\"FIELD-1\":[]}";
        String actualPostRemoveJson = gson.toJson(item.getCreateData(false));
        assertTrue(actualPostRemoveJson.contains(expectedPostRemoveJson));
    }


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
        Object preRemoveData = item.getCreateData(false);
        String expectedPreRemoveJson = "{\"fields\":{\"FIELD-1\":[{\"value\":2},{\"value\":3}]}";
        assertTrue(gson.toJson(preRemoveData).contains(expectedPreRemoveJson));

        CategoryField.Value value = new CategoryField.Value(2);
        item.removeValue("FIELD-1", value);
        Object postRemoveData = item.getCreateData(false);
        String expectedPostRemoveJson = "{\"fields\":{\"FIELD-1\":[{\"value\":3}]}";
        assertTrue(gson.toJson(postRemoveData).contains(expectedPostRemoveJson));
    }
}
