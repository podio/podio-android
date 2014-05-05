package com.podio.sdk.domain;

import java.util.List;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.podio.sdk.Filter;
import com.podio.sdk.provider.PodioFilter;

public class PodioFilterTest extends AndroidTestCase {

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

        Filter filter = new PodioFilter();
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

        Filter target = new PodioFilter();
        target.addQueryParameter(key, value);

        Uri uri = target.buildUri("scheme", "authority");
        assertNotNull(uri);
        assertEquals(1, uri.getQueryParameterNames().size());
        assertEquals(value, uri.getQueryParameter(key));
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

        Filter target = new PodioFilter();
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
