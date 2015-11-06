package com.podio.sdk;

import java.util.List;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.Suppress;

public class BasicPodioFilterTest extends AndroidTestCase {

    static class EmptyProvider extends Provider {

        static class Path extends Filter {
            Path() {
                super("");
            }
        }
    }

    @Suppress()
    public void testAddPathSegments() {
        Uri reference = Uri.parse("scheme://authority/path1/path2");
        Filter filter = new EmptyProvider.Path();

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

    @Suppress
    public void testAddQueryParameter() {
        String key = "test-key";
        String value = "test-value";

        Filter filter = new EmptyProvider.Path();
        try {
            filter.addQueryParameter(null, value);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            filter.addQueryParameter("null-pointer-value", null);
        } catch (IllegalArgumentException e) {
            fail("null-pointer query values should be allowed");
        }

        filter.addQueryParameter(key, value);

        Uri uri = filter.buildUri("scheme", "authority");
        assertNotNull(uri);

        // +1 for the accepted null-pointer value.
        assertEquals(2, uri.getQueryParameterNames().size());

        assertEquals(value, uri.getQueryParameter(key));
    }

    @Suppress
    public void testBuildUri() {
        Uri reference = Uri.parse("scheme://authority/path?key=value");
        Filter filter = new EmptyProvider.Path();
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

    public void testMultipleValuesWithSameKey() {
        String key = "test-key";
        String value1 = "test-value-1";
        String value2 = "test-value-2";

        Filter filter = new EmptyProvider.Path();
        filter.addQueryParameter(key, value1);
        filter.addQueryParameter(key, value2);

        Uri uri = filter.buildUri("scheme", "authority");
        List<String> values = uri.getQueryParameters(key);

        assertNotNull(uri);
        assertEquals(1, uri.getQueryParameterNames().size());
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }
}
