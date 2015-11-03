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

package com.podio.sdk.domain.field;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

/**
 * @author László Urszuly
 */
public class FieldTest extends AndroidTestCase {

    /**
     * Verifies that a {@link Field.Status} enum can be parsed from a JSON
     * string, using the Gson library.
     * 
     * <pre>
     * 
     * 1. Describe a simple Field object as a JSON string. Make sure it
     *      has a 'status' attribute.
     * 
     * 2. Parse the JSON string to an Field instance.
     * 
     * 3. Verify that the 'status' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testStatusEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        TextField field;

        field = gson.fromJson("{status:'active'}", TextField.class);
        assertEquals(Field.Status.active, field.getStatus());

        field = gson.fromJson("{}", TextField.class);
        assertEquals(Field.Status.undefined, field.getStatus());

        field = gson.fromJson("{status:'fdafadsfads'}", TextField.class);
        assertEquals(Field.Status.undefined, field.getStatus());
    }

    /**
     * Verifies that the {@link Field.Status} enum returns the correct value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfStatus() {
        assertEquals(Field.Status.active, Field.Status.valueOf("active"));
        assertEquals(Field.Status.deleted, Field.Status.valueOf("deleted"));
        assertEquals(Field.Status.undefined, Field.Status.valueOf("undefined"));

        assertEquals(Field.Status.active, Enum.valueOf(Field.Status.class, "active"));
        assertEquals(Field.Status.deleted, Enum.valueOf(Field.Status.class, "deleted"));
        assertEquals(Field.Status.undefined, Enum.valueOf(Field.Status.class, "undefined"));
    }

}
