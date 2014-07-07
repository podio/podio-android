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
import com.podio.sdk.domain.Application;
import com.podio.sdk.provider.mock.DummyRestClient;

public class ApplicationProviderTest extends AndroidTestCase {

    /**
     * Verifies that the {@link ApplicationProvider} requests a full set of
     * contents by default when requesting a single application.
     * 
     * <pre>
     * 
     * 1. Create a new ApplicationProvider.
     * 
     * 2. Make a default request for a specific app item.
     * 
     * 3. Verify that the designated rest client is called with a Uri that
     *      explicitly has set the "type" property to full.
     * 
     * </pre>
     */
    public void testFetchApplicationRequestsFullItemSetByDefault() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Application> mockListener = Mockito.mock(ResultListener.class);
        provider.get(2L, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/app/2?type=full"), uri);
    }

    /**
     * Verifies that the {@link ApplicationProvider} requests a micro set of
     * contents when requesting so.
     * 
     * <pre>
     * 
     * 1. Create a new ApplicationProvider.
     * 
     * 2. Make an explicit micro request for a specific app item.
     * 
     * 3. Verify that the designated rest client is called with a Uri that
     *      has set the "type" property to micro.
     * 
     * </pre>
     */
    public void testFetchApplicationMicroRequestsMicroItemSet() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Application> mockListener = Mockito.mock(ResultListener.class);
        provider.getMicro(2L, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/app/2?type=micro"), uri);
    }

    /**
     * Verifies that the {@link ApplicationProvider} requests a mini set of
     * contents when requesting so.
     * 
     * <pre>
     * 
     * 1. Create a new ApplicationProvider.
     * 
     * 2. Make an explicit mini request for a specific app item.
     * 
     * 3. Verify that the designated rest client is called with a Uri that
     *      has set the "type" property to mini.
     * 
     * </pre>
     */
    public void testFetchApplicationMiniRequestsMiniItemSet() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Application> mockListener = Mockito.mock(ResultListener.class);
        provider.getMini(2L, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/app/2?type=mini"), uri);
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
    public void testFetchApplicationsForSpaceDoesntRequestInactiveItems() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Application[]> mockListener = Mockito.mock(ResultListener.class);
        provider.getAllActive(1, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/app/space/1?include_inactive=false"), uri);
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
    public void testFetchApplicationsForSpaceWithInactivesIncludedRequestsInactiveItems() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Application[]> mockListener = Mockito.mock(ResultListener.class);
        provider.getAll(2, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/app/space/2?include_inactive=true"), uri);
    }

    /**
     * Verifies that the {@link ApplicationProvider} requests a short set of
     * contents when requesting so.
     * 
     * <pre>
     * 
     * 1. Create a new ApplicationProvider.
     * 
     * 2. Make an explicit short request for a specific app item.
     * 
     * 3. Verify that the designated rest client is called with a Uri that
     *      has set the "type" property to short.
     * 
     * </pre>
     */
    public void testFetchApplicationShortRequestsShortItemSet() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Application> mockListener = Mockito.mock(ResultListener.class);
        provider.getShort(2L, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/app/2?type=short"), uri);
    }

}
