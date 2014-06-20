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

package com.podio.sdk.provider;

import org.mockito.Mockito;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.podio.sdk.ResultListener;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Session;
import com.podio.sdk.provider.mock.DummyRestClient;

public class SessionProviderTest extends AndroidTestCase {

    /**
     * Verifies that the {@link SessionProvider} calls through to the (mock)
     * rest client with expected uri parameters when trying to authenticate with
     * user credentials.
     * 
     * <pre>
     * 
     * 1. Create a new SessionProvider.
     * 
     * 2. Make a request for authentication with user credentials.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testAuthenticateWithUserCredentials() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        SessionProvider provider = new SessionProvider(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Session> mockListener = Mockito.mock(ResultListener.class);
        provider.setup("CLIENTID", "CLIENTSECRET");
        provider.authenticateWithUserCredentials("USERNAME", "PASSWORD", mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.getMockUri();
        assertEquals(Uri.parse("content://test.uri/oauth/token"
                + "?client_id=CLIENTID&client_secret=CLIENTSECRET"
                + "&grant_type=password&username=USERNAME&password=PASSWORD"), uri);
    }

    /**
     * Verifies that the {@link SessionProvider} calls through to the (mock)
     * rest client with expected uri parameters when trying to authenticate with
     * app credentials.
     * 
     * <pre>
     * 
     * 1. Create a new SessionProvider.
     * 
     * 2. Make a request for authentication with app credentials.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testAuthenticateWithAppCredentials() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        SessionProvider provider = new SessionProvider(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Session> mockListener = Mockito.mock(ResultListener.class);
        provider.setup("CLIENTID", "CLIENTSECRET");
        provider.authenticateWithAppCredentials("APPID", "APPTOKEN", mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.getMockUri();
        assertEquals(Uri.parse("content://test.uri/oauth/token"
                + "?client_id=CLIENTID&client_secret=CLIENTSECRET"
                + "&grant_type=app&app_id=APPID&app_token=APPTOKEN"), uri);
    }

}
