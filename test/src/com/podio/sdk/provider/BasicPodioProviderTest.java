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

import android.test.AndroidTestCase;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.RestClient;
import com.podio.sdk.ResultListener;
import com.podio.sdk.SessionListener;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.mock.MockRestClient;

public class BasicPodioProviderTest extends AndroidTestCase {

    @Mock
    ResultListener<Object> resultListener;

    @Mock
    SessionListener sessionListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Verify that the abstract {@link BasicPodioProvider} implementation builds
     * the correct {@link RestRequest} for a DELETE request.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around.
     * 
     * 2. Call a delete operation on the ItemProvider object.
     * 
     * 3. Pick up the produced RestRequest from the mocked RestClient and
     *      verify that it conforms to expectations.
     * 
     * </pre>
     */
    public void testCorrectDeleteRestRequestProduced() {
        PodioFilter filter = new BasicPodioFilter();
        RestClient client = Mockito.mock(RestClient.class);

        // Perform the delete request.
        BasicPodioProvider provider = new BasicPodioProvider();
        provider.setRestClient(client);
        provider.request(RestClient.Operation.DELETE, filter, null, null);

        // Verify that the correct DELETE RestRequest is built.
        RestRequest<Object> expectedRequest = new RestRequest<Object>()
                .setOperation(RestClient.Operation.DELETE)
                .setFilter(filter);

        Mockito.verify(client).enqueue(expectedRequest);
    }

    /**
     * Verify that the abstract {@link BasicPodioProvider} implementation builds
     * the correct {@link RestRequest} for a GET request.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around.
     * 
     * 2. Call a fetch operation on the ItemProvider object.
     * 
     * 3. Pick up the produced RestRequest from the mocked RestClient and
     *      verify that it conforms to expectations.
     * 
     * </pre>
     */
    public void testCorrectGetRestRequestProduced() {
        PodioFilter filter = new BasicPodioFilter();
        RestClient client = Mockito.mock(RestClient.class);

        // Perform the fetch request.
        BasicPodioProvider provider = new BasicPodioProvider();
        provider.setRestClient(client);
        provider.request(RestClient.Operation.GET, filter, null, null);

        // Verify that the correct GET RestRequest is built.
        RestRequest<Object> expectedRequest = new RestRequest<Object>()
                .setOperation(RestClient.Operation.GET)
                .setFilter(filter);

        Mockito.verify(client).enqueue(expectedRequest);
    }

    /**
     * Verify that the abstract {@link BasicPodioProvider} implementation builds
     * the correct {@link RestRequest} for a POST request.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around.
     * 
     * 2. Call a push operation on the ItemProvider object.
     * 
     * 3. Pick up the produced RestRequest from the mocked RestClient and
     *      verify that it conforms to expectations.
     * 
     * </pre>
     */
    public void testCorrectPostRestRequestProduced() {
        PodioFilter filter = new BasicPodioFilter();
        Object item = new Object();
        RestClient client = Mockito.mock(RestClient.class);

        // Perform the push request.
        BasicPodioProvider provider = new BasicPodioProvider();
        provider.setRestClient(client);
        provider.request(RestClient.Operation.POST, filter, item, null);

        // Verify that the correct POST RestRequest is built.
        RestRequest<Object> expectedRequest = new RestRequest<Object>()
                .setOperation(RestClient.Operation.POST)
                .setFilter(filter)
                .setContent(item);

        Mockito.verify(client).enqueue(expectedRequest);
    }

    /**
     * Verify that the abstract {@link BasicPodioProvider} implementation builds
     * the correct {@link RestRequest} for a PUT request.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around.
     * 
     * 2. Call a change operation on the ItemProvider object.
     * 
     * 3. Pick up the produced RestRequest from the mocked RestClient and
     *      verify that it conforms to expectations.
     * 
     * </pre>
     */
    public void testCorrectPutRestRequestProduced() {
        PodioFilter filter = new BasicPodioFilter();
        Object item = new Object();
        RestClient client = Mockito.mock(RestClient.class);

        // Perform the change request.
        BasicPodioProvider provider = new BasicPodioProvider();
        provider.setRestClient(client);
        provider.request(RestClient.Operation.PUT, filter, item, null);

        // Verify that the correct PUT RestRequest is built.
        RestRequest<Object> expectedRequest = new RestRequest<Object>()
                .setOperation(RestClient.Operation.PUT)
                .setFilter(filter)
                .setContent(item);

        Mockito.verify(client).enqueue(expectedRequest);
    }

    /**
     * Verifies that the session change method on the
     * {@link PodioresultListener} callback is called if the (mocked)
     * {@link RestClient} decides to change the session object.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around. Also assign a custom {@link PodioresultListener} to the
     *      ItemProvider.
     * 
     * 2. Perform a request (any rest request) and simulate the mock RestClient
     *      working successfully on it (the {@link DummyRestClient} holds the
     *      details), but having the session changed.
     * 
     * 3. Verify that the success is propagated properly to your custom callback.
     * 
     * </pre>
     */
    public void testProviderCallbackSessionChangeAndSuccessCalledProperly() {
        PodioFilter itemFilter = new BasicPodioFilter();
        Object itemObject = new Object();
        Session session = new Session("accessToken", "refreshToken", 3600);

        MockRestClient client = new MockRestClient(RestResult.success(itemObject, session));
        BasicPodioProvider provider = new BasicPodioProvider();
        provider.setRestClient(client);

        provider.request(RestClient.Operation.PUT, itemFilter, itemObject, null)
                .setResultListener(resultListener)
                .setSessionListener(sessionListener);

        Mockito.verify(resultListener, Mockito.timeout(100)).onRequestPerformed(itemObject);
        Mockito.verify(sessionListener, Mockito.timeout(100)).onSessionChanged(session);
        Mockito.verifyNoMoreInteractions(resultListener);
        Mockito.verifyNoMoreInteractions(sessionListener);
    }

    /**
     * Verifies that the success method on the {@link PodioresultListener}
     * callback is called if the (mocked) {@link RestClient} finishes
     * successfully.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around. Also assign a custom {@link PodioresultListener} to the
     *      ItemProvider.
     * 
     * 2. Perform a request (any rest request) and simulate the mock RestClient
     *      working successfully on it (the {@link DummyRestClient} holds the
     *      details).
     * 
     * 3. Verify that the success is propagated properly to your custom callback.
     * 
     * </pre>
     */
    public void testProviderCallbackSuccessCalledProperly() {
        PodioFilter itemFilter = new BasicPodioFilter();
        Object itemObject = new Object();

        MockRestClient client = new MockRestClient(RestResult.success(itemObject));
        BasicPodioProvider provider = new BasicPodioProvider();
        provider.setRestClient(client);

        provider.request(RestClient.Operation.PUT, itemFilter, itemObject, null)
                .setResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100)).onRequestPerformed(itemObject);
        Mockito.verifyNoMoreInteractions(resultListener);
    }
}
