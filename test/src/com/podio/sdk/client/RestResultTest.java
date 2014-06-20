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

import android.test.AndroidTestCase;

import com.podio.sdk.PodioException;
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
    public void testCreateSuccessRestResultWithItem() {
        Object item = new Object();

        RestResult<Object> target = RestResult.success(item);
        assertNotNull(target);
        assertFalse(target.hasSession());
        assertFalse(target.hasAuthorizedSession());
        assertFalse(target.hasException());
        assertNull(target.getSession());
        assertNull(target.getException());
        assertEquals(item, target.getItem());
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
    public void testCreateSuccessRestResultWithSessionAndItem() {
        Session session = new Session("accessToken", "refreshToken", 1L);
        Object item = new Object();

        RestResult<Object> target = RestResult.success(item, session);
        assertNotNull(target);
        assertTrue(target.hasSession());
        assertTrue(target.hasAuthorizedSession());
        assertFalse(target.hasException());
        assertNotNull(target.getSession());
        assertEquals(item, target.getItem());
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
    public void testCreateFailureRestResultWithNoSessionAndNoItem() {
        PodioException exception = new PodioException("Ohno");

        RestResult<Object> target = RestResult.failure(exception);
        assertNotNull(target);
        assertFalse(target.hasSession());
        assertFalse(target.hasAuthorizedSession());
        assertNull(target.getSession());
        assertTrue(target.hasException());
        assertEquals(exception, target.getException());
        assertNull(target.getItem());
    }

}
