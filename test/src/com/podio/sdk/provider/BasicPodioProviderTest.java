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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Matchers;

import android.test.AndroidTestCase;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;
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

		RestClient client = mock(RestClient.class);
		doReturn(true).when(client).enqueue(Matchers.<RestRequest<Object>>anyObject());
		
		// Perform the delete request.
		BasicPodioProvider provider = new BasicPodioProvider(client);
		Object ticket = provider.request(RestOperation.DELETE, filter, null, null, null);

		assertEquals(ticket, filter);
		
		// Verify that the correct DELETE RestRequest is built.
		RestRequest<Object> expectedRequest = new RestRequest<Object>()
				.setOperation(RestOperation.DELETE)
				.setFilter(filter)
				.setTicket(ticket);
		verify(client).enqueue(expectedRequest);
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

		RestClient client = mock(RestClient.class);
		doReturn(true).when(client).enqueue(Matchers.<RestRequest<Object>>anyObject());

		// Perform the fetch request.
		BasicPodioProvider provider = new BasicPodioProvider(client);
		Object ticket = provider.request(RestOperation.GET, filter, null, null,
				null);
		
		assertEquals(ticket, filter);

		// Verify that the correct GET RestRequest is built.
		RestRequest<Object> expectedRequest = new RestRequest<Object>()
				.setOperation(RestOperation.GET)
				.setFilter(filter)
				.setTicket(ticket);
		verify(client).enqueue(expectedRequest);
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
		RestClient client = mock(RestClient.class);
		doReturn(true).when(client).enqueue(Matchers.<RestRequest<Object>>anyObject());

		PodioFilter filter = new BasicPodioFilter();
		Object item = new Object();
		
		// Perform the push request.
		BasicPodioProvider provider = new BasicPodioProvider(client);
		Object ticket = provider.request(RestOperation.POST, filter, item,
				null, null);
		
		assertEquals(ticket, filter);

		// Verify that the correct POST RestRequest is built.
		RestRequest<Object> expectedRequest = new RestRequest<Object>()
				.setOperation(RestOperation.POST)
				.setFilter(filter)
				.setContent(item)
				.setTicket(ticket);
		verify(client).enqueue(expectedRequest);
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

		RestClient client = mock(RestClient.class);
		doReturn(true).when(client).enqueue(Matchers.<RestRequest<Object>>anyObject());

		// Perform the change request.
		BasicPodioProvider provider = new BasicPodioProvider(client);
		Object ticket = provider.request(RestOperation.PUT, filter, item, null,
				null);
		
		assertEquals(ticket, filter);

		// Verify that the correct PUT RestRequest is built.
		RestRequest<Object> expectedRequest = new RestRequest<Object>()
				.setOperation(RestOperation.PUT)
				.setFilter(filter)
				.setContent(item)
				.setTicket(ticket);
		verify(client).enqueue(expectedRequest);
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
		String errorMessage = "ohno";
		List<Object> resultList = new ArrayList<Object>();
		resultList.add(itemObject);

		DummyRestClient client = new DummyRestClient(new RestResult<Object>(
				false, errorMessage, resultList));
		@SuppressWarnings("unchecked")
		ResultListener<Object> mockListener = mock(ResultListener.class);

		BasicPodioProvider provider = new BasicPodioProvider(client);
		Object ticket = provider.request(RestOperation.PUT, itemFilter,
				itemObject, null, mockListener);

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
		String errorMessage = "ohno";

		DummyRestClient client = new DummyRestClient(new RestResult<Object>(true,
				session, errorMessage, itemObject));
		BasicPodioProvider provider = new BasicPodioProvider(client);

		@SuppressWarnings("unchecked")
		ResultListener<Object> mockListener = mock(ResultListener.class);
		Object ticket = provider.request(RestOperation.PUT, itemFilter,
				itemObject, null, mockListener);

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
	 *      working successfully on it (the {@link DummyRestClient} holds the
	 *      details), but having the session changed.
	 * 
	 * 3. Verify that the success is propagated properly to your custom callback.
	 * 
	 * </pre>
	 */
	public void testProviderCallbackSessionChangeAndFailureCalledProperly() {
		PodioFilter itemFilter = new BasicPodioFilter();
		Session session = new Session("accessToken", "refreshToken", 3600);
		String errorMessage = "ohno";

		DummyRestClient client = new DummyRestClient(new RestResult<Object>(
				false, session, errorMessage, null));
		BasicPodioProvider provider = new BasicPodioProvider(client);

		@SuppressWarnings("unchecked")
		ResultListener<Object> mockListener = mock(ResultListener.class);
		Object ticket = provider.request(RestOperation.PUT, itemFilter, null,
				null, mockListener);

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
		String errorMessage = "ohno";

		DummyRestClient client = new DummyRestClient(new RestResult<Object>(true,
				errorMessage, itemObject));
		BasicPodioProvider provider = new BasicPodioProvider(client);

		@SuppressWarnings("unchecked")
		ResultListener<Object> mockListener = mock(ResultListener.class);
		Object ticket = provider.request(RestOperation.PUT, itemFilter,
				itemObject, null, mockListener);

		verify(mockListener).onSuccess(ticket, itemObject);
		verifyNoMoreInteractions(mockListener);
	}
}
