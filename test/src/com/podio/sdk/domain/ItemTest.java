// @formatter:off

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

// @formatter:on

package com.podio.sdk.domain;

import java.lang.reflect.Type;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.podio.sdk.domain.field.CategoryField;
import com.podio.sdk.domain.field.Field;

public class ItemTest extends AndroidTestCase {

    /**
     * Verifies that the fields of the {@link Item} class can be populated by
     * the Gson JSON parser.
     * 
     * <pre>
     * 
     * 1. Describe an Item domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON with the Gson parser.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its fields.
     * 
     * </pre>
     */
    public void testItemCanBePopulatedByGson() {
        String json = new StringBuilder("{")
                .append("app:{},")
                .append("created_by:{},")
                .append("created_on:'CREATEDON',")
                .append("excerpt:{")
                .append(" label:'LABEL',")
                .append(" text:'TEXT'")
                .append("},")
                .append("external_id:'EXTERNALID',")
                .append("fields:[],")
                .append("grant:[],")
                .append("grant_count:1,")
                .append("item_id:1,")
                .append("link:'LINK',")
                .append("pinned:true,")
                .append("priority:1,")
                .append("revision:1,")
                .append("rights:[],")
                .append("space:{},")
                .append("subscribed:true,")
                .append("subscribed_count:1,")
                .append("tags:[],")
                .append("title:'TITLE'")
                .append("}").toString();
        Gson gson = new Gson();
        Item item = gson.fromJson(json, Item.class);

        assertNotNull(item.app);
        assertNotNull(item.created_by);
        assertEquals("CREATEDON", item.created_on);
        assertNotNull(item.excerpt);
        assertEquals("LABEL", item.excerpt.label);
        assertEquals("TEXT", item.excerpt.text);
        assertEquals("EXTERNALID", item.external_id);
        assertNotNull(item.fields);
        assertEquals(0, item.fields.size());
        assertNotNull(item.grant);
        assertEquals(0, item.grant.length);
        assertEquals(Integer.valueOf(1), item.grant_count);
        assertEquals(Long.valueOf(1), item.item_id);
        assertEquals("LINK", item.link);
        assertEquals(Boolean.TRUE, item.pinned);
        assertEquals(Integer.valueOf(1), item.priority);
        assertEquals(Integer.valueOf(1), item.revision);
        assertNotNull(item.rights);
        assertEquals(0, item.rights.length);
        assertNotNull(item.space);
        assertEquals(Boolean.TRUE, item.subscribed);
        assertEquals(Integer.valueOf(1), item.subscribed_count);
        assertNotNull(item.tags);
        assertEquals(0, item.tags.length);
        assertEquals("TITLE", item.title);
    }

    /**
     * Verifies that the {@link Item} class can generate an object that
     * describes the changed values it contains.
     * 
     * <pre>
     * 
     * 1. Describe a simple Item domain object as a JSON string. Make
     *      sure it has some Field items.
     * 
     * 2. Create an instance of it from the JSON with the Gson parser.
     * 
     * 3. Request the push data from the item and convert it to a
     *      JSON string.
     * 
     * 3. Verify that the JSON string corresponds to expectations.
     * 
     * </pre>
     */
    public void testItemCanGeneratePushData() {
        String json = new StringBuilder("{")
                .append("external_id:'EXTERNALID',")
                .append("fields:[{")
                .append("  field_id:1,")
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
                .append("},{")
                .append("  field_id:4,")
                .append("  values: [{")
                .append("    value: {")
                .append("      id:5")
                .append("    }")
                .append("  }],")
                .append("  config: {")
                .append("    settings: {")
                .append("      options: [{")
                .append("        id:5")
                .append("      },{")
                .append("        id:6")
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

        Object pushData = item.getPushData();
        String actualPushJson = gson.toJson(pushData);
        String expectedPushJson = "{\"external_id\":\"EXTERNALID\",\"fields\":{\"1\":[{\"value\":2}],\"4\":[{\"value\":5}]}}";

        assertEquals(expectedPushJson, actualPushJson);
    }

    /**
     * Verifies that the fields of the {@link Item.PushResult} class can be
     * populated by the Gson JSON parser.
     * 
     * <pre>
     * 
     * 1. Describe a PushResult domain object as a JSON string.
     * 
     * 2. Create an instance of it from the JSON with the Gson parser.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its fields.
     * 
     * </pre>
     */
    public void testPushResultCanBePopulatedByGson() {
        String json = "{revision:1,item_id:1,title:'TITLE'}";
        Gson gson = new Gson();
        Item.PushResult result = gson.fromJson(json, Item.PushResult.class);

        assertEquals(Integer.valueOf(1), result.revision);
        assertEquals(Long.valueOf(1L), result.item_id);
        assertEquals("TITLE", result.title);
    }
}
