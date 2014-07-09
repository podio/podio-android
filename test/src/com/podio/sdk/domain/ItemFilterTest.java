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

public class ItemFilterTest extends AndroidTestCase {

    /**
     * Verifies that the members of the {@link Item.FilterData} class can be
     * initialized by the Gson JSON parser.
     * 
     * <pre>
     * 
     * 1. Describe a Item.FilterData domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON string.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testItemFilterDataCanBeInitializedFromJson() {
        String json = "{sort_by:'SORTBY', sort_desc:true, filters:{ key1: 1, key2: 'value'}, limit:12, offset:3, remember:false}";
        Gson gson = new Gson();
        Item.FilterData filterData = gson.fromJson(json, Item.FilterData.class);

        assertNotNull(filterData);
        assertEquals("SORTBY", filterData.getSortKey());
        assertEquals(true, filterData.getDoSortDescending());
        assertTrue(filterData.hasConstraint("key1"));
        assertTrue(filterData.hasConstraint("key2"));
        assertEquals(12, filterData.getLimit());
        assertEquals(3, filterData.getOffset());
        assertEquals(false, filterData.getDoRemember());
    }

    /**
     * Verifies that the members of the {@link Item.FilterData} class can be
     * initialized from the class constructor.
     * 
     * <pre>
     * 
     * 1. Initialize an Item.FilterData instance with the constructor.
     * 
     * 2. Verify that the created object has the expected values for its
     *      members.
     * 
     * </pre>
     */
    public void testItemFilterDataCanBeInitializedFromConstructor() {
        Item.FilterData filterData = new Item.FilterData()
                .setOrderByField("SORTBY", true)
                .setLimit(12)
                .setOffset(3)
                .setDoRemember(false);
        filterData.addConstraint("key1", 1);
        filterData.addConstraint("key2", "value");

        assertNotNull(filterData);
        assertEquals("SORTBY", filterData.getSortKey());
        assertEquals(true, filterData.getDoSortDescending());
        assertTrue(filterData.hasConstraint("key1"));
        assertTrue(filterData.hasConstraint("key2"));
        assertEquals(12, filterData.getLimit());
        assertEquals(3, filterData.getOffset());
        assertEquals(false, filterData.getDoRemember());
    }

    /**
     * Verifies that null pointer keys and values are ignored when trying to add
     * them as constraints. And that the SDK doesn't crash or throw exceptions
     * if still trying.
     * 
     * <pre>
     * 
     * 1. Create an Item.FilterData object.
     * 
     * 2. Try to add null pointer constraint keys and values to it.
     * 
     * 3. Verify that no null pointer constraints are present in the filter.
     * 
     * </pre>
     */
    public void testCanNotAddNullConstraint() {
        Item.FilterData filterData = new Item.FilterData();
        filterData.addConstraint("TEST", null);

        assertEquals(false, filterData.hasConstraint("TEST"));
        assertEquals(null, filterData.getConstraint("TEST"));

        filterData.addConstraint(null, null);
        assertEquals(false, filterData.hasConstraint(null));
        assertEquals(null, filterData.getConstraint(null));
    }

    /**
     * Verifies that a previously added constraint also can be removed.
     * 
     * <pre>
     * 
     * 1. Create a new Item.FilterData object.
     * 
     * 2. Add some constraints to it.
     * 
     * 3. Remove a constraint.
     * 
     * 4. Verify that the filter doesn't hold the constraint any more.
     * 
     * </pre>
     */
    public void testCanRemoveConstraint() {
        Item.FilterData filterData = new Item.FilterData();
        filterData.addConstraint("KEY", "VALUE");

        assertEquals(true, filterData.hasConstraint("KEY"));
        assertEquals("VALUE", filterData.getConstraint("KEY"));

        filterData.removeConstraint("KEY");

        assertEquals(false, filterData.hasConstraint("KEY"));
        assertEquals(null, filterData.getConstraint("KEY"));
    }

    /**
     * Verifies that the members of the {@link Item.FilterResult} class can be
     * instantiated from a JSON string.
     * 
     * <pre>
     * 
     * 1. Describe a Item.FilterResult domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON string.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testItemFilterResultCanBePopulatedByGson() {
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

    /**
     * Verifies that the Item.FilterResult never returns null when asking for
     * the items, not even if the result JSON doesn't define the 'items'
     * attribute.
     * 
     * <pre>
     * 
     * 1. Create an Item.FilterResult object from a JSON string. Make sure the
     *      'items' attribute is omitted.
     * 
     * 2. Try to read the items from the result object.
     * 
     * 3. Verify that an empty list is returned. Not null.
     * 
     * </pre>
     */
    public void testItemFilterResultItemsNeverNull() {
        String json = "{total:100, filtered:0}";
        Gson gson = new Gson();
        Item.FilterResult result = gson.fromJson(json, Item.FilterResult.class);
        List<Item> items = result.getItems();
        assertNotNull(items);
        assertEquals(0, items.size());
    }
}
