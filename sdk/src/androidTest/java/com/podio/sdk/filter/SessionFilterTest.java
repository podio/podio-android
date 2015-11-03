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

public class SessionFilterTest extends AndroidTestCase {

    /**
     * Verifies that the {@link SessionFilter} doesn't omit any properties when
     * describing a app authorization request.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SessionFilter} object.
     * 
     * 2. Add values for app authorization properties.
     * 
     * 3. Make it build the result Uri.
     * 
     * 4. Verify that all properties are reflected in the result Uri.
     * 
     * </pre>
     */
    public void testAllAppCredentialsIncludedInResultUri() {
        Uri reference = Uri
                .parse("content://test.uri/oauth/token?grant_type=app&app_id=APPID&app_token=APPTOKEN");

        Uri result = new SessionFilter()
                .withAppCredentials("APPID", "APPTOKEN")
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that the {@link SessionFilter} doesn't omit any properties when
     * describing a client authentication request.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SessionFilter} object.
     * 
     * 2. Add values for client authentication properties.
     * 
     * 3. Make it build the result Uri.
     * 
     * 4. Verify that all properties are reflected in the result Uri.
     * 
     * </pre>
     */
    public void testAllClientCredentialsIncludedInResultUri() {
        Uri reference = Uri
                .parse("content://test.uri/oauth/token?client_id=CLIENTID&client_secret=CLIENTSECRET");

        Uri result = new SessionFilter()
                .withClientCredentials("CLIENTID", "CLIENTSECRET")
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that the {@link SessionFilter} doesn't omit any properties when
     * describing a refresh auth-tokens request.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SessionFilter} object.
     * 
     * 2. Add values for auth-token refresh properties.
     * 
     * 3. Make it build the result Uri.
     * 
     * 4. Verify that all properties are reflected in the result Uri.
     * 
     * </pre>
     */
    public void testAllRefreshTokensIncludedInResultUri() {
        Uri reference = Uri
                .parse("content://test.uri/oauth/token?grant_type=refresh_token&refresh_token=REFRESHTOKEN");

        Uri result = new SessionFilter()
                .withRefreshToken("REFRESHTOKEN")
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

    /**
     * Verifies that the {@link SessionFilter} doesn't omit any properties when
     * describing a user authorization request.
     * 
     * <pre>
     * 
     * 1. Create a new {@link SessionFilter} object.
     * 
     * 2. Add values for user authorization properties.
     * 
     * 3. Make it build the result Uri.
     * 
     * 4. Verify that all properties are reflected in the result Uri.
     * 
     * </pre>
     */
    public void testAllUserCredentialsIncludedInResultUri() {
        Uri reference = Uri
                .parse("content://test.uri/oauth/token?grant_type=password&username=USERNAME&password=PASSWORD");

        Uri result = new SessionFilter()
                .withUserCredentials("USERNAME", "PASSWORD")
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

}
