package com.podio.sdk.client;

import java.util.ArrayList;

import android.test.AndroidTestCase;

public class RestResultTest extends AndroidTestCase {

    /**
     * Verifies that a {@link RestResult} created with a successful status, a
     * non-empty message and a non-empty list of items, keeps the initial values
     * and returns them correctly when requested.
     * 
     * <pre>
     * 
     * 1. Create a new RestResult item with known, positive values.
     * 
     * 2. Verify that the values don't get distorted until they are
     *      requested again.
     * 
     * </pre>
     */
    public void testCreateRestResultSuccessMessageItems() {
        boolean isSuccess = true;
        String message = "success";
        Object item = new Object();

        RestResult target = new RestResult(isSuccess, message, item);
        assertNotNull(target);
        assertEquals(isSuccess, target.isSuccess());
        assertEquals(message, target.message());
        assertEquals(item, target.item());
    }

    /**
     * Verifies that a {@link RestResult} created with a unsuccessful status, an
     * empty message and an empty list of items, keeps the initial values and
     * returns them correctly when requested.
     * 
     * <pre>
     * 
     * 1. Create a new RestResult item with known, negative values.
     * 
     * 2. Verify that the values don't get distorted until they are
     *      requested again.
     * 
     * </pre>
     */
    public void testCreateRestResultFailureNoMessageNoItems() {
        boolean isSuccess = false;
        String message = null;
        ArrayList<Object> items = null;

        RestResult target = new RestResult(isSuccess, message, items);
        assertNotNull(target);
        assertEquals(isSuccess, target.isSuccess());
        assertEquals(message, target.message());
        assertEquals(items, target.item());
    }

}
