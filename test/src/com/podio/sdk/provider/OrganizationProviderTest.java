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

import android.net.Uri;
import android.test.AndroidTestCase;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.provider.mock.MockProviderListener;
import com.podio.sdk.provider.mock.MockRestClient;

public class OrganizationProviderTest extends AndroidTestCase {

    /**
     * Verifies that the {@link OrganizationProvider} calls through to the
     * (mock) rest client with expected uri parameters when trying to
     * authenticate with user credentials.
     * 
     * <pre>
     * 
     * 1. Create a new OrganizationProvider.
     * 
     * 2. Make a request for all organizations.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testGetAllOrganizations() {
        final Uri reference = Uri.parse("content://test.uri/org");

        final MockRestClient mockClient = new MockRestClient();
        final MockProviderListener mockListener = new MockProviderListener();

        OrganizationProvider target = new OrganizationProvider(mockClient);
        target.setProviderListener(mockListener);

        target.getAll();
        mockClient.mock_processLastPushedRestRequest(true, null, null);

        assertEquals(false, mockListener.mock_isSessionChangeCalled);
        assertEquals(true, mockListener.mock_isSuccessCalled);
        assertEquals(false, mockListener.mock_isFailureCalled);

        Uri uri = ((PodioFilter) mockListener.mock_ticket).buildUri("content", "test.uri");
        assertEquals(reference, uri);
    }

}
