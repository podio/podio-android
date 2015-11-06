package com.podio.sdk.provider;

import android.net.Uri;
import android.test.InstrumentationTestCase;

public class ItemFilterTest extends InstrumentationTestCase {

    public void testApplicationIdIsIncludedInResultUri() {
        Uri reference = Uri.parse("content://test.uri/item/app/12");

        Uri result = new ItemProvider.Path()
                .withApplicationId(12)
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    public void testFilterPropertyIsIncludedInResultUri() {
        Uri reference = Uri.parse("content://test.uri/item/app/12/filter");

        Uri result = new ItemProvider.Path()
                .withApplicationIdFilter(12)
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    public void testItemIdIsIncludedInResultUri() {
        Uri reference = Uri.parse("content://test.uri/item/12");

        Uri result = new ItemProvider.Path()
                .withItemId(12)
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

}
