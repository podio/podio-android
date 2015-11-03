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

import android.net.Uri;
import android.test.AndroidTestCase;

public class ApplicationFilterTest extends AndroidTestCase {

    /**
     * Verifies that {@link ApplicationFilter} doesn't omit any properties when
     * setting them all at once.
     * 
     * <pre>
     * 
     * 1. Create a new ApplicationFilter object.
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
     * Verifies that the {@link ApplicationFilter} adds the given application id
     * as a part of the path segments in the final Uri.
     * 
     * <pre>
     * 
     * 1. Create a new ApplicationFilter.
     * 
     * 2. Specify an application id for it.
     * 
     * 3. Make it build the result Uri.
     * 
     * 4. Verify that the id is a part of the path segments.
     * 
     * </pre>
     */
    public void testApplicationIdAddedAsPathSegment() {
        Uri reference = Uri.parse("content://test.uri/app/1");
        Uri result = new ApplicationFilter().withApplicationId(1L).buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that all methods of the {@link ApplicationFilter} that set any
     * parameter returns the actual <code>AppItemFilter</code> object (and,
     * hence, enables a chaining design pattern).
     * 
     * <pre>
     * 
     * 1. Create a new ApplicationFilter object.
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
     * Verifies that the {@link ApplicationFilter} adds the given
     * "include inactive" flag as a query parameter in the final Uri.
     * 
     * <pre>
     * 
     * 1. Create a new ApplicationFilter.
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
     * Verifies that the {@link ApplicationFilter} adds the given "space id" as
     * a part of the path segments in the final Uri.
     * 
     * <pre>
     * 
     * 1. Create a new ApplicationFilter.
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

    /**
     * Verifies that the {@link ApplicationFilter} adds the given "type" flag as
     * a query parameter in the final Uri.
     * 
     * <pre>
     * 
     * 1. Create a new ApplicationFilter.
     * 
     * 2. Specify the "type" flag for it.
     * 
     * 3. Make it build the result Uri.
     * 
     * 4. Verify that the flag is a query parameter.
     * 
     * </pre>
     */
    public void testTypeFlagAddedAsQueryParameter() {
        Uri reference = Uri.parse("content://test.uri/app?type=test");
        Uri result = new ApplicationFilter().withType("test").buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

}
