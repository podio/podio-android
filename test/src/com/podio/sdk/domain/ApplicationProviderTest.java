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

import android.net.Uri;
import android.test.AndroidTestCase;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.PodioProviderListener;
import com.podio.sdk.domain.mock.MockRestClient;
import com.podio.sdk.provider.ApplicationProvider;

public class ApplicationProviderTest extends AndroidTestCase {

    private static final class ConcurrentResult {
        private boolean isSessionChangeCalled = false;
        private boolean isSuccessCalled = false;
        private boolean isFailureCalled = false;
        private Object ticket = null;
    }

    /**
     * Verifies that the {@link ApplicationProvider} doesn't request inactive
     * app items by default.
     * 
     * <pre>
     * 
     * 1. Create a new AppItemProvider.
     * 
     * 2. Make a default request of app items for any workspace.
     * 
     * 3. Verify that the designated rest client is called with a Uri that
     *      explicitly has set the "include inactive" flag to false.
     * 
     * </pre>
     */
    public void testFetchAppItemsForSpaceDoesntRequestInactiveItems() {
        final Uri reference = Uri.parse("content://test.uri/app/space/1?include_inactive=false");
        final MockRestClient mockClient = new MockRestClient();
        final ConcurrentResult result = new ConcurrentResult();

        ApplicationProvider target = new ApplicationProvider();
        target.setRestClient(mockClient);
        target.setProviderListener(new PodioProviderListener() {
            @Override
            public void onRequestFailure(Object ticket, String message) {
                result.isFailureCalled = true;
                result.ticket = ticket;
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
                result.isSessionChangeCalled = true;
                result.ticket = ticket;
            }

            @Override
            public void onRequestComplete(Object ticket, Object item) {
                result.isSuccessCalled = true;
                result.ticket = ticket;
            }
        });

        target.fetchApplicationsForSpace(1);
        mockClient.mock_processLastPushedRestRequest(true, null, null);

        assertEquals(false, result.isSessionChangeCalled);
        assertEquals(true, result.isSuccessCalled);
        assertEquals(false, result.isFailureCalled);

        Uri uri = ((PodioFilter) result.ticket).buildUri("content", "test.uri");
        assertEquals(reference, uri);
    }

    /**
     * Verifies that the {@link ApplicationProvider} requests inactive app items
     * as well through the custom fetch method.
     * 
     * <pre>
     * 
     * 1. Create a new AppItemProvider.
     * 
     * 2. Make a custom request of app items for any workspace.
     * 
     * 3. Verify that the designated rest client is called with a Uri that
     *      explicitly has set the "include inactive" flag to true.
     * 
     * </pre>
     */
    public void testfetchAppItemsForSpaceWithInactivesIncludedRequestsInactiveItems() {
        final Uri reference = Uri.parse("content://test.uri/app/space/2?include_inactive=true");
        final MockRestClient mockClient = new MockRestClient();
        final ConcurrentResult result = new ConcurrentResult();

        ApplicationProvider target = new ApplicationProvider();
        target.setRestClient(mockClient);
        target.setProviderListener(new PodioProviderListener() {
            @Override
            public void onRequestFailure(Object ticket, String message) {
                result.isFailureCalled = true;
                result.ticket = ticket;
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
                result.isSessionChangeCalled = true;
                result.ticket = ticket;
            }

            @Override
            public void onRequestComplete(Object ticket, Object item) {
                result.isSuccessCalled = true;
                result.ticket = ticket;
            }
        });

        target.fetchApplicationsForSpaceWithInactivesIncluded(2);
        mockClient.mock_processLastPushedRestRequest(true, null, null);

        assertEquals(false, result.isSessionChangeCalled);
        assertEquals(true, result.isSuccessCalled);
        assertEquals(false, result.isFailureCalled);

        Uri uri = ((PodioFilter) result.ticket).buildUri("content", "test.uri");
        assertEquals(reference, uri);
    }
}
