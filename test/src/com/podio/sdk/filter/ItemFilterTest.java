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

public class ItemFilterTest extends AndroidTestCase {

    /**
     * Verifies that the {@link ItemFilter} builds with the correct path and
     * properties when requesting all items for a single Application.
     * 
     * <pre>
     * 
     * 1. Create a new {@link ItemFilter} object.
     * 
     * 2. Add the application id to it.
     * 
     * 3. Make it build a default Uri.
     * 
     * 4. Verify that all expected properties are reflected in the result Uri.
     * 
     * </pre>
     */
    public void testApplicationIdIsIncludedInResultUri() {
        Uri reference = Uri.parse("content://test.uri/item/app/12");

        Uri result = new ItemFilter()
                .withApplicationId(12)
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that the {@link ItemFilter} builds with the correct path and
     * properties when requesting a filtered set of items for a single
     * Application.
     * 
     * <pre>
     * 
     * 1. Create a new {@link ItemFilter} object.
     * 
     * 2. Add the application id to it.
     * 
     * 3. Make it build a default Uri.
     * 
     * 4. Verify that all expected properties are reflected in the result Uri.
     * 
     * </pre>
     */
    public void testFilterPropertyIsIncludedInResultUri() {
        Uri reference = Uri.parse("content://test.uri/item/app/12/filter");

        Uri result = new ItemFilter()
                .withApplicationIdFilter(12)
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that the {@link ItemFilter} builds with the correct path and
     * properties when requesting a single Item.
     * 
     * <pre>
     * 
     * 1. Create a new {@link ItemFilter} object.
     * 
     * 2. Add the item id to it.
     * 
     * 3. Make it build a default Uri.
     * 
     * 4. Verify that all expected properties are reflected in the result Uri.
     * 
     * </pre>
     */
    public void testItemIdIsIncludedInResultUri() {
        Uri reference = Uri.parse("content://test.uri/item/12");

        Uri result = new ItemFilter()
                .withItemId(12)
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

}
