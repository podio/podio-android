package com.podio.sdk.domain;

import java.util.List;
import java.util.Map;

import android.test.AndroidTestCase;

import com.podio.sdk.Filter;
import com.podio.sdk.domain.mock.MockItemFilter;

public class ItemFilterTest extends AndroidTestCase {

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

        Filter target = new MockItemFilter();
        target.addQueryParameter(key, value);

        Map<String, List<String>> params = target.getQueryParameters();
        assertNotNull(params);
        assertEquals(1, params.size());

        List<String> values = params.get(key);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
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

        Filter target = new MockItemFilter();
        target.addQueryParameter(key, value1);
        target.addQueryParameter(key, value2);

        Map<String, List<String>> params = target.getQueryParameters();
        assertNotNull(params);
        assertEquals(1, params.size());

        List<String> values = params.get(key);
        assertNotNull(values);
        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }
}
