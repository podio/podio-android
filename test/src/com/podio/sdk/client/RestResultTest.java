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

        RestResult<Object> target = new RestResult<Object>(isSuccess, message, item);
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

        RestResult<Object> target = new RestResult<Object>(isSuccess, session, message, item);
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

        RestResult<Object> target = new RestResult<Object>(isSuccess, message, items);
        assertNotNull(target);
        assertNull(target.session());
        assertEquals(isSuccess, target.isSuccess());
        assertEquals(message, target.message());
        assertEquals(items, target.item());
    }

}
