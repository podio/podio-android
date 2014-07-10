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

/**
 * @author László Urszuly
 */
public class UserEmailTest extends AndroidTestCase {

    /**
     * Verifies that the members of the {@link User.Email} class can be
     * initialized by parsing JSON.
     * 
     * <pre>
     * 
     * 1. Describe a User Email domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON string.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testUserEmailCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("disabled:true,")
                .append("primary:true,")
                .append("verified:true,")
                .append("mail:'MAIL'")
                .append("}").toString();

        Gson gson = new Gson();
        User.Email email = gson.fromJson(json, User.Email.class);

        assertNotNull(email);
        assertEquals(true, email.isDisabled());
        assertEquals(true, email.isPrimary());
        assertEquals(true, email.isVerified());
        assertEquals("MAIL", email.getAddress());
    }

}
