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

package com.podio.sdk.filter;

import java.util.List;

import android.net.Uri;
import android.test.AndroidTestCase;

public class BasicPodioFilterTest extends AndroidTestCase {

    /**
     * Verifies that path segments can be added and are persisted in the same
     * order they were added.
     * 
     * <pre>
     * 
     * 1. Create a new ItemFilter object.
     * 
     * 2. Add path segments to it.
     * 
     * 3. Let the filter build the Uri.
     * 
     * 4. Verify that the Uri corresponds to the expectations.
     * 
     * </pre>
     */
    public void testAddPathSegments() {
        Uri reference = Uri.parse("scheme://authority/path1/path2");
        BasicPodioFilter filter = new BasicPodioFilter();

        try {
            filter.addPathSegment(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        filter.addPathSegment("path1");
        filter.addPathSegment("path2");

        Uri target = filter.buildUri("scheme", "authority");
        assertNotNull(target);
        assertEquals(reference, target);
    }

    /**
     * Verifies that a query parameter can be added and retrieved from the
     * ItemFilter.
     * 
     * <pre>
     * 
     * 1. Create a new ItemFilter object.
     * 
     * 2. Add a query parameter to it.
     * 
     * 3. Retrieve the query parameters.
     * 
     * 4. Verify that the previously added value is intact.
     * 
     * </pre>
     */
    public void testAddQueryParameter() {
        String key = "test-key";
        String value = "test-value";

        BasicPodioFilter filter = new BasicPodioFilter();

        try {
            filter.addQueryParameter(null, value);
            fail("should have thrown IllegalArgumentException");
        } catch (Exception e) {
        }

        try {
            filter.addQueryParameter("null-pointer-value", null);
        } catch (Exception e) {
            fail("null-pointer query values should be allowed");
        }

        filter.addQueryParameter(key, value);

        Uri uri = filter.buildUri("scheme", "authority");
        assertNotNull(uri);

        // +1 for the accepted null-pointer value.
        assertEquals(2, uri.getQueryParameterNames().size());

        assertEquals(value, uri.getQueryParameter(key));
    }

    /**
     * Verifies that the ItemFilter can build a valid URI.
     * 
     * <pre>
     * 
     * 1. Create a new ItemFilter object.
     * 
     * 2. Add a path segment to it.
     * 
     * 3. Add a query parameter to it.
     * 
     * 4. Try to build a URI from the ItemFilter with null pointer scheme/authority.
     * 
     * 5. Verify that exceptions are thrown.
     * 
     * 6. Try to build a URI from the ItemFilter with valid scheme/authority.
     * 
     * 7. Verify that an expected URI is built.
     * 
     * </pre>
     */
    public void testBuildUri() {
        Uri reference = Uri.parse("scheme://authority/path?key=value");
        BasicPodioFilter filter = new BasicPodioFilter();

        filter.addPathSegment("path");
        filter.addQueryParameter("key", "value");

        try {
            filter.buildUri(null, "authority");
            fail("null pointer scheme shouldn't be accepted");
        } catch (IllegalArgumentException e) {
        }

        try {
            filter.buildUri("scheme", null);
            fail("null pointer authority shouldn't be accepted");
        } catch (IllegalArgumentException e) {
        }

        Uri target = filter.buildUri("scheme", "authority");
        assertNotNull(target);
        assertEquals(reference, target);
    }

    /**
     * Verifies that multiple values with the same key don't overwrite each
     * other.
     * 
     * <pre>
     * 
     * 1. Create a new ItemFilter object.
     * 
     * 2. Add two query parameters, with the same key, to it.
     * 
     * 3. Retrieve the query parameters.
     * 
     * 4. Verify that the previously added values are both intact.
     * 
     * </pre>
     */
    public void testMultipleValuesWithSameKey() {
        String key = "test-key";
        String value1 = "test-value-1";
        String value2 = "test-value-2";

        BasicPodioFilter target = new BasicPodioFilter();
        target.addQueryParameter(key, value1);
        target.addQueryParameter(key, value2);

        Uri uri = target.buildUri("scheme", "authority");
        List<String> values = uri.getQueryParameters(key);

        assertNotNull(uri);
        assertEquals(1, uri.getQueryParameterNames().size());
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }
}
