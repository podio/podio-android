package com.podio.sdk.domain;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.podio.sdk.domain.field.TextField;

public class ItemPushTest extends AndroidTestCase {


    public void testItemPushDataIsProducedFromItem() {
        Gson gson = new Gson();
        Item item = new Item();
        item.addValue("FIELD-1", new TextField.Value("VALUE"));
        String pushJson = gson.toJson(item.getCreateData());
        String reference = "{\"fields\":{\"FIELD-1\":[{\"value\":\"VALUE\"}]}";
        assertTrue(pushJson.contains(reference));
    }

    public void testItemPushResultCanBeInitializedFromJson() {
        String json = "{item_id:1, revision:1, title:'TITLE'}";
        Gson gson = new Gson();
        Item.CreateResult pushResult = gson.fromJson(json, Item.CreateResult.class);

        assertEquals(1L, pushResult.getItemId());
        assertEquals(1L, pushResult.getRevisionId());
        assertEquals("TITLE", pushResult.getTitle());
    }


    public void testItemPushResultDefaults() {
        Item.CreateResult pushResult = new Item.CreateResult();

        assertEquals(-1L, pushResult.getItemId());
        assertEquals(-1L, pushResult.getRevisionId());
        assertEquals(null, pushResult.getTitle());
    }
}
