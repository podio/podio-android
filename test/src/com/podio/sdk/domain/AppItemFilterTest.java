package com.podio.sdk.domain;

import android.net.Uri;
import android.test.AndroidTestCase;

public class AppItemFilterTest extends AndroidTestCase {

    /**
     * Verifies that {@link AppItemFilter} doesn't omit any properties when
     * setting them all at once.
     * 
     * <pre>
     * 
     * 1. Create a new AppItemFilter object.
     * 
     * 2. Add a value for all properties.
     * 
     * 3. Make it build the result Uri.
     * 
     * 4. Verify that all properties are reflected in the result Uri.
     * 
     * </pre>
     */
    public void testAllPropertiesIncludedInResultUri() {
        Uri reference = Uri.parse("content://test.uri/app/space/2" //
                + "?oauth_token=test_token" + //
                "&include_inactive=true");

        Uri result = new AppItemFilter() //
                .withSpaceId(2L) //
                .withToken("test_token") //
                .withInactivesIncluded(true) //
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that all methods of the {@link AppItemFilter} that set any
     * parameter returns the actual <code>AppItemFilter</code> object (and,
     * hence, enables a chaining design pattern).
     * 
     * <pre>
     * 
     * 1. Create a new AppItemFilter object.
     * 
     * 2. Call the class specific methods on the object.
     * 
     * 3. Verify that each method returns the object itself.
     * 
     * </pre>
     */
    public void testChainingPossible() {
        AppItemFilter target = new AppItemFilter();

        assertEquals(target, target.withInactivesIncluded(true));
        assertEquals(target, target.withSpaceId(1L));
        assertEquals(target, target.withToken("token"));
    }

    /**
     * Verifies that the {@link AppItemFilter} adds the given "include inactive"
     * flag as a query parameter in the final Uri.
     * 
     * <pre>
     * 
     * 1. Create a new AppItemFilter.
     * 
     * 2. Specify the "include inactive" flag for it.
     * 
     * 3. Make it build the result Uri.
     * 
     * 4. Verify that the flag is a query parameter.
     * 
     * </pre>
     */
    public void testIncludeInactiveFlagAddedAsQueryParameter() {
        Uri reference = Uri.parse("content://test.uri/app?include_inactive=false");
        Uri result = new AppItemFilter().withInactivesIncluded(false).buildUri("content",
                "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that the {@link AppItemFilter} adds the given "space id" as a
     * part of the path segments in the final Uri.
     * 
     * <pre>
     * 
     * 1. Create a new AppItemFilter.
     * 
     * 2. Specify a "space id" for it.
     * 
     * 3. Make it build the result Uri.
     * 
     * 4. Verify that the id is a part of the path segments.
     * 
     * </pre>
     */
    public void testSpaceIdAddedAsPathSegment() {
        Uri reference = Uri.parse("content://test.uri/app/space/1");
        Uri result = new AppItemFilter().withSpaceId(1L).buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that the {@link AppItemFilter} adds the given "token" as a query
     * parameter in the final Uri.
     * 
     * <pre>
     * 
     * 1. Create a new AppItemFilter.
     * 
     * 2. Specify a "access token" for it.
     * 
     * 3. Make it build the result Uri.
     * 
     * 4. Verify that the token is a query parameter.
     * 
     * </pre>
     */
    public void testTokenAddedAsQueryParameter() {
        Uri reference = Uri.parse("content://test.uri/app?oauth_token=token");
        Uri result = new AppItemFilter().withToken("token").buildUri("content", "test.uri");

        assertEquals(reference, result);
    }
}
