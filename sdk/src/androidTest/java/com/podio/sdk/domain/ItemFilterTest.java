
package com.podio.sdk.domain;

import java.util.List;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class ItemFilterTest extends AndroidTestCase {


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


    public void testCanNotAddNullConstraint() {
        Item.FilterData filterData = new Item.FilterData();
        filterData.addConstraint("TEST", null);

        assertEquals(false, filterData.hasConstraint("TEST"));
        assertEquals(null, filterData.getConstraint("TEST"));

        filterData.addConstraint(null, null);
        assertEquals(false, filterData.hasConstraint(null));
        assertEquals(null, filterData.getConstraint(null));
    }


    public void testCanRemoveConstraint() {
        Item.FilterData filterData = new Item.FilterData();
        filterData.addConstraint("KEY", "VALUE");

        assertEquals(true, filterData.hasConstraint("KEY"));
        assertEquals("VALUE", filterData.getConstraint("KEY"));

        filterData.removeConstraint("KEY");

        assertEquals(false, filterData.hasConstraint("KEY"));
        assertEquals(null, filterData.getConstraint("KEY"));
    }


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


    public void testItemFilterResultItemsNeverNull() {
        String json = "{total:100, filtered:0}";
        Gson gson = new Gson();
        Item.FilterResult result = gson.fromJson(json, Item.FilterResult.class);
        List<Item> items = result.getItems();
        assertNotNull(items);
        assertEquals(0, items.size());
    }
}
