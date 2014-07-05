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

import android.test.AndroidTestCase;

import com.podio.sdk.ErrorListener;
import com.podio.sdk.PodioException;
import com.podio.sdk.PodioFilter;
import com.podio.sdk.RestClient;
import com.podio.sdk.ResultListener;
import com.podio.sdk.SessionListener;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.provider.mock.DummyRestClient;

public class BasicPodioProviderTest extends AndroidTestCase {

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
        provider.request(RestClient.Operation.DELETE, filter, null, null, null, null, null);

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
        provider.request(RestClient.Operation.GET, filter, null, null, null, null, null);

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
        provider.request(RestClient.Operation.POST, filter, item, null, null, null, null);

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
        provider.request(RestClient.Operation.PUT, filter, item, null, null, null, null);

        // Verify that the correct PUT RestRequest is built.
        RestRequest<Object> expectedRequest = new RestRequest<Object>()
                .setOperation(RestClient.Operation.PUT)
                .setFilter(filter)
                .setContent(item);

        Mockito.verify(client).enqueue(expectedRequest);
    }

    /**
     * Verifies that the failure method on the {@link PodioresultListener}
     * callback is called if the (mocked) {@link RestClient} fails to finish
     * properly.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around. Also assign a custom {@link PodioresultListener} to the
     *      ItemProvider.
     * 
     * 2. Perform a request (any rest request) and simulate the mock RestClient
     *      working on it and failing (the {@link DummyRestClient} holds the
     *      details).
     * 
     * 3. Verify that the failure is propagated properly to your custom callback.
     * 
     * </pre>
     */
    public void testProviderCallbackFailureCalledProperly() {
        PodioFilter itemFilter = new BasicPodioFilter();
        Object itemObject = new Object();
        PodioException exception = new PodioException("ohno");

        DummyRestClient client = new DummyRestClient(RestResult.failure(exception));

        ErrorListener mockListener = Mockito.mock(ErrorListener.class);

        BasicPodioProvider provider = new BasicPodioProvider();
        provider.setRestClient(client);
        provider.request(RestClient.Operation.PUT, itemFilter, itemObject, null, null, mockListener, null);

        Mockito.verify(mockListener).onExceptionOccurred(exception);
        Mockito.verifyNoMoreInteractions(mockListener);
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

        DummyRestClient client = new DummyRestClient(RestResult.success(itemObject, session));
        BasicPodioProvider provider = new BasicPodioProvider();
        provider.setRestClient(client);

        @SuppressWarnings("unchecked")
        ResultListener<Object> mockResultListener = Mockito.mock(ResultListener.class);
        SessionListener mockSessionListener = Mockito.mock(SessionListener.class);

        provider.request(RestClient.Operation.PUT, itemFilter, itemObject, null, mockResultListener, null, mockSessionListener);

        Mockito.verify(mockResultListener).onRequestPerformed(itemObject);
        Mockito.verify(mockSessionListener).onSessionChanged(session);
        Mockito.verifyNoMoreInteractions(mockResultListener);
        Mockito.verifyNoMoreInteractions(mockSessionListener);
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

        DummyRestClient client = new DummyRestClient(RestResult.success(itemObject));
        BasicPodioProvider provider = new BasicPodioProvider();
        provider.setRestClient(client);

        @SuppressWarnings("unchecked")
        ResultListener<Object> mockListener = Mockito.mock(ResultListener.class);
        provider.request(RestClient.Operation.PUT, itemFilter, itemObject, null, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(itemObject);
        Mockito.verifyNoMoreInteractions(mockListener);
    }
}
