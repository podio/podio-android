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

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.podio.sdk.RestClient;

public class RestOperationTest extends AndroidTestCase {

    /**
     * Verifies that the {@link RestClient.Operation} enumeration parses the
     * "DELETE" string into the correct enumeration value.
     * 
     * <pre>
     * 
     * 1. Call the <code>valueOf()</code> method on the {@link RestClient.Operation}
     *      enumeration with the "DELETE" string as argument.
     * 
     * 2. Verify that the expected object was returned.
     * 
     * 3. Call the <code>valueOf()</code> method on the platform {@link Enum}
     *      implementation with the class of the {@link RestClient.Operation} object
     *      and the "DELETE" string as arguments.
     * 
     * 4. Verify that the expected object was returned.
     * 
     * </pre>
     */
    public void testRestOperationValueOfDelete() {
        RestClient.Operation target1 = RestClient.Operation.valueOf("DELETE");
        assertEquals(RestClient.Operation.DELETE, target1);

        RestClient.Operation target2 = Enum.valueOf(RestClient.Operation.class, "DELETE");
        assertEquals(RestClient.Operation.DELETE, target2);
    }

    /**
     * Verifies that the {@link RestClient.Operation} enumeration parses the
     * "GET" string into the correct enumeration value.
     * 
     * <pre>
     * 
     * 1. Call the <code>valueOf()</code> method on the {@link RestClient.Operation}
     *      enumeration with the "GET" string as argument.
     * 
     * 2. Verify that the expected object was returned.
     * 
     * 3. Call the <code>valueOf()</code> method on the platform {@link Enum}
     *      implementation with the class of the {@link RestClient.Operation} object
     *      and the "GET" string as arguments.
     * 
     * 4. Verify that the expected object was returned.
     * 
     * </pre>
     */
    public void testRestOperationValueOfGet() {
        RestClient.Operation target = RestClient.Operation.valueOf("GET");
        assertEquals(RestClient.Operation.GET, target);

        RestClient.Operation target2 = Enum.valueOf(RestClient.Operation.class, "GET");
        assertEquals(RestClient.Operation.GET, target2);
    }

    /**
     * Verifies that the {@link RestClient.Operation} enumeration parses the
     * "POST" string into the correct enumeration value.
     * 
     * <pre>
     * 
     * 1. Call the <code>valueOf()</code> method on the {@link RestClient.Operation}
     *      enumeration with the "POST" string as argument.
     * 
     * 2. Verify that the expected object was returned.
     * 
     * 3. Call the <code>valueOf()</code> method on the platform {@link Enum}
     *      implementation with the class of the {@link RestClient.Operation} object
     *      and the "POST" string as arguments.
     * 
     * 4. Verify that the expected object was returned.
     * 
     * </pre>
     */
    public void testRestOperationValueOfPost() {
        RestClient.Operation target1 = RestClient.Operation.valueOf("POST");
        assertEquals(RestClient.Operation.POST, target1);

        RestClient.Operation target2 = Enum.valueOf(RestClient.Operation.class, "POST");
        assertEquals(RestClient.Operation.POST, target2);
    }

    /**
     * Verifies that the {@link RestClient.Operation} enumeration parses the
     * "PUT" string into the correct enumeration value.
     * 
     * <pre>
     * 
     * 1. Call the <code>valueOf()</code> method on the {@link RestClient.Operation}
     *      enumeration with the "PUT" string as argument.
     * 
     * 2. Verify that the expected object was returned.
     * 
     * 3. Call the <code>valueOf()</code> method on the platform {@link Enum}
     *      implementation with the class of the {@link RestClient.Operation} object
     *      and the "PUT" string as arguments.
     * 
     * 4. Verify that the expected object was returned.
     * 
     * </pre>
     */
    public void testRestOperationValueOfPut() {
        RestClient.Operation target = RestClient.Operation.valueOf("PUT");
        assertEquals(RestClient.Operation.PUT, target);

        RestClient.Operation target2 = Enum.valueOf(RestClient.Operation.class, "PUT");
        assertEquals(RestClient.Operation.PUT, target2);
    }

    /**
     * Verifies that all expected {@link RestClient.Operation} values are
     * included in the result of the <code>values()</code> method call.
     * 
     * <pre>
     * 
     * 1. Call the <code>values()</code> method on the {@link RestClient.Operation}
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
        RestClient.Operation[] targetArray = RestClient.Operation.values();
        assertNotNull(targetArray);
        assertEquals(4, targetArray.length);

        List<RestClient.Operation> targetList = new ArrayList<RestClient.Operation>();

        for (RestClient.Operation operation : targetArray) {
            targetList.add(operation);
        }

        assertTrue(targetList.contains(RestClient.Operation.DELETE));
        assertTrue(targetList.contains(RestClient.Operation.GET));
        assertTrue(targetList.contains(RestClient.Operation.POST));
        assertTrue(targetList.contains(RestClient.Operation.PUT));
    }

}
