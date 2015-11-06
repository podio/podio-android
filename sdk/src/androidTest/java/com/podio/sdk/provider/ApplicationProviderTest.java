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

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.Request;
import com.podio.sdk.domain.Application;
import com.podio.sdk.mock.MockRestClient;

public class ApplicationProviderTest extends InstrumentationTestCase {

    @Mock
    Request.ResultListener<Application> resultListener;

    @Mock
    Request.ResultListener<Application[]> arrayResultListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }


    public void testFetchApplicationRequestsFullItemSetByDefault() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.get(2L).withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/2?type=full"), mockClient.uri);
    }


    public void testFetchApplicationMicroRequestsMicroItemSet() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.getMicro(2L).withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/2?type=micro"), mockClient.uri);
    }


    public void testFetchApplicationMiniRequestsMiniItemSet() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.getMini(6L).withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/6?type=mini"), mockClient.uri);
    }


    public void testFetchApplicationsForSpaceDoesntRequestInactiveItems() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.getAllActive(1L).withResultListener(arrayResultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/space/1?include_inactive=false"), mockClient.uri);
    }

    public void testFetchApplicationsForSpaceWithInactivesIncludedRequestsInactiveItems() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.getAll(2L).withResultListener(arrayResultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/space/2?include_inactive=true"), mockClient.uri);
    }


    public void testFetchApplicationShortRequestsShortItemSet() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.getShort(2L).withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/2?type=short"), mockClient.uri);
    }

}
