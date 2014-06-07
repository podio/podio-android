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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;
import com.podio.sdk.provider.mock.MockRestClient;

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
        final MockRestClient client = new MockRestClient();
        final PodioFilter filter = new BasicPodioFilter();

        // Perform the delete request.
        BasicPodioProvider target = new BasicPodioProvider(client);
        Object ticket = target.request(RestOperation.DELETE, filter, null, null, null);

        // Verify that the correct DELETE RestRequest is built.
        RestRequest<Object> request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.DELETE, null, ticket, filter, request);
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
        final MockRestClient client = new MockRestClient();
        final PodioFilter filter = new BasicPodioFilter();

        // Perform the fetch request.
        BasicPodioProvider target = new BasicPodioProvider(client);
        Object ticket = target.request(RestOperation.GET, filter, null, null, null);

        // Verify that the correct GET RestRequest is built.
        RestRequest<Object> request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.GET, null, ticket, filter, request);
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
        final MockRestClient client = new MockRestClient();
        final Object item = new Object();
        final PodioFilter filter = new BasicPodioFilter();

        // Perform the push request.
        BasicPodioProvider target = new BasicPodioProvider(client);
        Object ticket = target.request(RestOperation.POST, filter, item, null, null);

        // Verify that the correct POST RestRequest is built.
        RestRequest<Object> request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.POST, item, ticket, filter, request);
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
        final MockRestClient client = new MockRestClient();
        final PodioFilter filter = new BasicPodioFilter();
        final Object item = new Object();

        // Perform the change request.
        BasicPodioProvider target = new BasicPodioProvider(client);
        Object ticket = target.request(RestOperation.PUT, filter, item, null, null);

        // Verify that the correct PUT RestRequest is built.
        RestRequest<Object> request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.PUT, item, ticket, filter, request);
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
     *      working on it and failing (the {@link MockRestClient} holds the
     *      details).
     * 
     * 3. Verify that the failure is propagated properly to your custom callback.
     * 
     * </pre>
     */
    public void testProviderCallbackFailureCalledProperly() {
        final PodioFilter itemFilter = new BasicPodioFilter();
        final Object itemObject = new Object();
        final String errorMessage = "ohno";
        final List<Object> resultList = new ArrayList<Object>();
        resultList.add(itemObject);

        final MockRestClient client = new MockRestClient();
        @SuppressWarnings("unchecked")
		final ResultListener<Object> mockListener = mock(ResultListener.class);

        BasicPodioProvider target = new BasicPodioProvider(client);
        Object ticket = target.request(RestOperation.PUT, itemFilter, itemObject, null, mockListener);

        // Allow the mock client to "process" the request (basically allow the
        // callbacks to execute).
        client.mock_processLastPushedRestRequest(false, errorMessage, resultList);

        verify(mockListener).onFailure(ticket, errorMessage);
        verifyNoMoreInteractions(mockListener);
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
     *      working successfully on it (the {@link MockRestClient} holds the
     *      details), but having the session changed.
     * 
     * 3. Verify that the success is propagated properly to your custom callback.
     * 
     * </pre>
     */
    public void testProviderCallbackSessionChangeAndSuccessCalledProperly() {
        final PodioFilter itemFilter = new BasicPodioFilter();
        final Object itemObject = new Object();
        final Session session = new Session("accessToken", "refreshToken", 3600);
        final String errorMessage = "ohno";

        final MockRestClient client = new MockRestClient();
        @SuppressWarnings("unchecked")
		final ResultListener<Object> mockListener = mock(ResultListener.class);

        // Setup the mock session.
        client.mock_setMockSession(session);

        BasicPodioProvider target = new BasicPodioProvider(client);
        Object ticket = target.request(RestOperation.PUT, itemFilter, itemObject, null, mockListener);

        // Allow the mock client to "process" the request (basically allow the
        // callbacks to execute).
        client.mock_processLastPushedRestRequest(true, errorMessage, itemObject);

        verify(mockListener).onSuccess(ticket, itemObject);
        verify(mockListener).onSessionChange(ticket, session);
        verifyNoMoreInteractions(mockListener);
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
     *      working successfully on it (the {@link MockRestClient} holds the
     *      details), but having the session changed.
     * 
     * 3. Verify that the success is propagated properly to your custom callback.
     * 
     * </pre>
     */
    public void testProviderCallbackSessionChangeAndFailureCalledProperly() {
        final PodioFilter itemFilter = new BasicPodioFilter();
        final Session session = new Session("accessToken", "refreshToken", 3600);
        final String errorMessage = "ohno";

        final MockRestClient client = new MockRestClient();
        @SuppressWarnings("unchecked")
		final ResultListener<Object> mockListener = mock(ResultListener.class);

        // Setup the mock session.
        client.mock_setMockSession(session);

        BasicPodioProvider target = new BasicPodioProvider(client);
        Object ticket = target.request(RestOperation.PUT, itemFilter, null, null, mockListener);

        // Allow the mock client to "process" the request (basically allow the
        // callbacks to execute).
        client.mock_processLastPushedRestRequest(false, errorMessage, null);

        verify(mockListener).onFailure(ticket, errorMessage);
        verify(mockListener).onSessionChange(ticket, session);
        verifyNoMoreInteractions(mockListener);
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
     *      working successfully on it (the {@link MockRestClient} holds the
     *      details).
     * 
     * 3. Verify that the success is propagated properly to your custom callback.
     * 
     * </pre>
     */
    public void testProviderCallbackSuccessCalledProperly() {
        final PodioFilter itemFilter = new BasicPodioFilter();
        final Object itemObject = new Object();
        final String errorMessage = "ohno";

        final MockRestClient client = new MockRestClient();
        @SuppressWarnings("unchecked")
		final ResultListener<Object> mockListener = mock(ResultListener.class);

        // Simulate an update request.
        BasicPodioProvider target = new BasicPodioProvider(client);
        Object ticket = target.request(RestOperation.PUT, itemFilter, itemObject, null, mockListener);

        // Allow the mock client to "process" the request (basically allow the
        // callbacks to execute).
        client.mock_processLastPushedRestRequest(true, errorMessage, itemObject);

        verify(mockListener).onSuccess(ticket, itemObject);
        verifyNoMoreInteractions(mockListener);
    }

    /**
     * Performs tests on the given {@link RestRequest} and verifies they meet
     * the provided expectations.
     * 
     * @param expectedOperation
     *            The expected {@link RestOperation}.
     * @param expectedContent
     *            The expected target item.
     * @param expectedFilter
     *            The expected request filter.
     * @param expectedPath
     *            The expected path in the request URI.
     * @param target
     *            The {@link RestRequest} that needs to live up to the
     *            expectations.
     */
    private <T> void validateRequest(RestOperation expectedOperation, T expectedContent,
            Object expectedTicket, PodioFilter expectedFilter, RestRequest<T> target) {

        assertNotNull(target);
        assertEquals(expectedContent, target.getContent());
        assertEquals(expectedOperation, target.getOperation());
        assertEquals(expectedFilter, target.getFilter());
        assertEquals(expectedTicket, target.getTicket());
    }
}
