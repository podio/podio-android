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

import java.util.List;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class ItemRequestTest extends AndroidTestCase {

    /**
     * Verifies that the fields of the {@link ItemRequest} class can be
     * populated by the Gson JSON parser.
     * 
     * <pre>
     * 
     * 1. Describe a ItemRequest domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON with the Gson parser.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its fields.
     * 
     * </pre>
     */
    public void testItemRequestCanBePopulatedByGson() {
        String json = "{sort_by:'SORTBY', sort_desc:true, filters:{ key1: 1, key2: 'value'}, limit:12, offset:3, remember:false}";
        Gson gson = new Gson();
        Item.FilterData filterData = gson.fromJson(json, Item.FilterData.class);

        assertNotNull(filterData);
        assertEquals("SORTBY", filterData.getSortKey());
        assertEquals(true, filterData.doSortDescending());
        assertTrue(filterData.hasConstraint("key1"));
        assertTrue(filterData.hasConstraint("key2"));
        assertEquals(12, filterData.getLimit());
        assertEquals(3, filterData.getOffset());
        assertEquals(false, filterData.doRemember());
    }

    /**
     * Verifies that the fields of the {@link ItemRequest} class can be
     * populated from the class constructor and that no values are distorted.
     * 
     * <pre>
     * 
     * 1. Initialize an ItemRequest instance with the constructor.
     * 
     * 3. Verify that the created object has the expected values for its
     *      fields.
     * 
     * </pre>
     */
    public void testItemRequestCanBePopulatedFromConstructor() {
        Item.FilterData filterData = new Item.FilterData("SORTBY", true, 12, 3, false);
        filterData.addConstraint("key1", 1);
        filterData.addConstraint("key2", "value");

        assertNotNull(filterData);
        assertEquals("SORTBY", filterData.getSortKey());
        assertEquals(true, filterData.doSortDescending());
        assertTrue(filterData.hasConstraint("key1"));
        assertTrue(filterData.hasConstraint("key2"));
        assertEquals(12, filterData.getLimit());
        assertEquals(3, filterData.getOffset());
        assertEquals(false, filterData.doRemember());
    }

    /**
     * Verifies that the fields of the {@link ItemRequest.Result} class can be
     * populated by the Gson JSON parser.
     * 
     * <pre>
     * 
     * 1. Describe a ItemRequest Result domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON with the Gson parser.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its fields.
     * 
     * </pre>
     */
    public void testItemRequestResultCanBePopulatedByGson() {
        String json = "{total:100, filtered:2, items:[{external_id: 'one'}, {external_id: 'two'}]}";
        Gson gson = new Gson();
        Item.FilterResult result = gson.fromJson(json, Item.FilterResult.class);

        assertNotNull(result);
        assertEquals(100, result.getTotalCount());
        assertEquals(2, result.getFilteredCount());

        List<Item> items = result.getItems();
        assertNotNull(items);
        assertEquals(2, items.size());
    }
}
