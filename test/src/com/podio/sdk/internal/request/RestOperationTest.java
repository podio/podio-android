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

package com.podio.sdk.internal.request;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

public class RestOperationTest extends AndroidTestCase {

    /**
     * Verifies that the {@link RestOperation} enumeration parses the "DELETE"
     * string into the correct enumeration value.
     * 
     * <pre>
     * 
     * 1. Call the <code>valueOf()</code> method on the {@link RestOperation}
     *      enumeration with the "DELETE" string as argument.
     * 
     * 2. Verify that the expected object was returned.
     * 
     * 3. Call the <code>valueOf()</code> method on the platform {@link Enum}
     *      implementation with the class of the {@link RestOperation} object
     *      and the "DELETE" string as arguments.
     * 
     * 4. Verify that the expected object was returned.
     * 
     * </pre>
     */
    public void testRestOperationValueOfDelete() {
        RestOperation target1 = RestOperation.valueOf("DELETE");
        assertEquals(RestOperation.DELETE, target1);

        RestOperation target2 = Enum.valueOf(RestOperation.class, "DELETE");
        assertEquals(RestOperation.DELETE, target2);
    }

    /**
     * Verifies that the {@link RestOperation} enumeration parses the "GET"
     * string into the correct enumeration value.
     * 
     * <pre>
     * 
     * 1. Call the <code>valueOf()</code> method on the {@link RestOperation}
     *      enumeration with the "GET" string as argument.
     * 
     * 2. Verify that the expected object was returned.
     * 
     * 3. Call the <code>valueOf()</code> method on the platform {@link Enum}
     *      implementation with the class of the {@link RestOperation} object
     *      and the "GET" string as arguments.
     * 
     * 4. Verify that the expected object was returned.
     * 
     * </pre>
     */
    public void testRestOperationValueOfGet() {
        RestOperation target = RestOperation.valueOf("GET");
        assertEquals(RestOperation.GET, target);

        RestOperation target2 = Enum.valueOf(RestOperation.class, "GET");
        assertEquals(RestOperation.GET, target2);
    }

    /**
     * Verifies that the {@link RestOperation} enumeration parses the "POST"
     * string into the correct enumeration value.
     * 
     * <pre>
     * 
     * 1. Call the <code>valueOf()</code> method on the {@link RestOperation}
     *      enumeration with the "POST" string as argument.
     * 
     * 2. Verify that the expected object was returned.
     * 
     * 3. Call the <code>valueOf()</code> method on the platform {@link Enum}
     *      implementation with the class of the {@link RestOperation} object
     *      and the "POST" string as arguments.
     * 
     * 4. Verify that the expected object was returned.
     * 
     * </pre>
     */
    public void testRestOperationValueOfPost() {
        RestOperation target1 = RestOperation.valueOf("POST");
        assertEquals(RestOperation.POST, target1);

        RestOperation target2 = Enum.valueOf(RestOperation.class, "POST");
        assertEquals(RestOperation.POST, target2);
    }

    /**
     * Verifies that the {@link RestOperation} enumeration parses the "PUT"
     * string into the correct enumeration value.
     * 
     * <pre>
     * 
     * 1. Call the <code>valueOf()</code> method on the {@link RestOperation}
     *      enumeration with the "PUT" string as argument.
     * 
     * 2. Verify that the expected object was returned.
     * 
     * 3. Call the <code>valueOf()</code> method on the platform {@link Enum}
     *      implementation with the class of the {@link RestOperation} object
     *      and the "PUT" string as arguments.
     * 
     * 4. Verify that the expected object was returned.
     * 
     * </pre>
     */
    public void testRestOperationValueOfPut() {
        RestOperation target = RestOperation.valueOf("PUT");
        assertEquals(RestOperation.PUT, target);

        RestOperation target2 = Enum.valueOf(RestOperation.class, "PUT");
        assertEquals(RestOperation.PUT, target2);
    }

    /**
     * Verifies that all expected {@link RestOperation} values are included in
     * the result of the <code>values()</code> method call.
     * 
     * <pre>
     * 
     * 1. Call the <code>values()</code> method on the {@link RestOperation}
     *      enumeration.
     * 
     * 2. Verify that the returned array is not a null pointer.
     * 
     * 3. Verify that the returned array has four elements.
     * 
     * 4. Verify that the DELETE, GET, POST, PUT values are all in the result
     *      array.
     * 
     * </pre>
     */
    public void testRestOperationValues() {
        RestOperation[] targetArray = RestOperation.values();
        assertNotNull(targetArray);
        assertEquals(5, targetArray.length);

        List<RestOperation> targetList = new ArrayList<RestOperation>();

        for (RestOperation operation : targetArray) {
            targetList.add(operation);
        }

        assertTrue(targetList.contains(RestOperation.AUTHORIZE));
        assertTrue(targetList.contains(RestOperation.DELETE));
        assertTrue(targetList.contains(RestOperation.GET));
        assertTrue(targetList.contains(RestOperation.POST));
        assertTrue(targetList.contains(RestOperation.PUT));
    }

}
