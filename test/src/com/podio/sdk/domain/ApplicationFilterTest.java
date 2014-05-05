package com.podio.sdk.domain;

import com.podio.sdk.provider.ApplicationFilter;

import android.net.Uri;
import android.test.AndroidTestCase;

public class ApplicationFilterTest extends AndroidTestCase {

    /**
     * Verifies that {@link ApplicationFilter} doesn't omit any properties when
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
                + "?include_inactive=true");

        Uri result = new ApplicationFilter() //
                .withSpaceId(2L) //
                .withInactivesIncluded(true) //
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that all methods of the {@link ApplicationFilter} that set any
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
        ApplicationFilter target = new ApplicationFilter();

        assertEquals(target, target.withInactivesIncluded(true));
        assertEquals(target, target.withSpaceId(1L));
    }

    /**
     * Verifies that the {@link ApplicationFilter} adds the given "include inactive"
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
        Uri result = new ApplicationFilter().withInactivesIncluded(false).buildUri("content",
                "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that the {@link ApplicationFilter} adds the given "space id" as a
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
        Uri result = new ApplicationFilter().withSpaceId(1L).buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

}
