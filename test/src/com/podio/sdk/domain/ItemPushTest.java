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

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class ItemPushTest extends AndroidTestCase {

    /**
     * Verifies that a {@link Item.PushData} class is produced by the
     * {@link Item} class.
     * 
     * <pre>
     * 
     * 1. Create an Item object.
     * 
     * 2. Add a value to it.
     * 
     * 3. Have the Item build a PushData object for you.
     * 
     * 4. Parse the object to a JSON string and verify its integrity
     *      (Since the PushData object doesn't expose any of its
     *      members, we have to parse it to JSON in order to be able
     *      to analyze its key inertia).
     * 
     * </pre>
     */
    public void testItemPushDataIsProducedFromItem() {
        Gson gson = new Gson();
        Item item = new Item();
        item.addValue("FIELD-1", "VALUE");
        String pushJson = gson.toJson(item.getPushData());
        String reference = "{\"fields\":{\"FIELD-1\":[{\"value\":\"VALUE\"}]}}";
        assertEquals(reference, pushJson);
    }

    /**
     * Verifies that the members of an {@link Item.PushResult} object can be
     * initialized from a JSON string.
     * 
     * <pre>
     * 
     * 1. Create an Item.PushResult object from JSON.
     * 
     * 2. Verify that the members are initialized as expected.
     * 
     * </pre>
     */
    public void testItemPushResultCanBeInitializedFromJson() {
        String json = "{item_id:1, revision:1, title:'TITLE'}";
        Gson gson = new Gson();
        Item.PushResult pushResult = gson.fromJson(json, Item.PushResult.class);

        assertEquals(1, pushResult.getItemId());
        assertEquals(1, pushResult.getRevisionId());
        assertEquals("TITLE", pushResult.getTitle());
    }

    /**
     * Verifies that the members of an {@link Item.PushResult} object defaults
     * to expected values.
     * 
     * <pre>
     * 
     * 1. Instantiate a new Item.PushResult item from its constructor.
     * 
     * 2. Verify the default values for its members.
     * 
     * </pre>
     */
    public void testItemPushResultDefaults() {
        Item.PushResult pushResult = new Item.PushResult();

        assertEquals(-1, pushResult.getItemId());
        assertEquals(-1, pushResult.getRevisionId());
        assertEquals(null, pushResult.getTitle());
    }
}
