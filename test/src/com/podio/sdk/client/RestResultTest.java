package com.podio.sdk.client;

import java.util.ArrayList;

import android.test.AndroidTestCase;

import com.podio.sdk.domain.Session;

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
        assertNull(target.session());
        assertEquals(isSuccess, target.isSuccess());
        assertEquals(message, target.message());
        assertEquals(item, target.item());
    }

    /**
     * Verifies that a {@link RestResult} created with a successful status, a
     * changed session object, a non-empty message and a non-empty list of
     * items, keeps the initial values and returns them correctly when
     * requested.
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
    public void testCreateRestResultSuccessSessionMessageItems() {
        boolean isSuccess = true;
        Session session = new Session("accessToken", "refreshToken", 1L);
        String message = "success";
        Object item = new Object();

        RestResult target = new RestResult(isSuccess, session, message, item);
        assertNotNull(target);
        assertNotNull(target.session());
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
        assertNull(target.session());
        assertEquals(isSuccess, target.isSuccess());
        assertEquals(message, target.message());
        assertEquals(items, target.item());
    }

}
