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

package com.podio.sdk.domain;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class StatusTest extends AndroidTestCase {

    /**
     * Verifies that a {@link Status} enum can be parsed from a JSON
     * string, using the Gson library.
     * 
     * <pre>
     * 
     * 1. Describe a simple Application object as a JSON string. Make sure it
     *      has a 'status' attribute.
     * 
     * 2. Parse the JSON string to an Application instance.
     * 
     * 3. Verify that the 'status' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testStatusEnumCanBeParsedFromJson() {
        Gson gson = new Gson();

        String json1 = "{status:'active'}";
        Application application1 = gson.fromJson(json1, Application.class);

        assertNotNull(application1);
        assertEquals(Status.active, application1.status);

        String json2 = "{status:'inactive'}";
        Application application2 = gson.fromJson(json2, Application.class);

        assertNotNull(application2);
        assertEquals(Status.inactive, application2.status);

        String json3 = "{status:'deleted'}";
        Application application3 = gson.fromJson(json3, Application.class);

        assertNotNull(application3);
        assertEquals(Status.deleted, application3.status);
    }

    /**
     * Verifies that the {@link Status} enum returns the correct
     * value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testStatusEnumGivesExpectedValueOf() {
        assertEquals(Status.active, Status.valueOf("active"));
        assertEquals(Status.inactive, Status.valueOf("inactive"));
        assertEquals(Status.deleted, Status.valueOf("deleted"));
    }

}
