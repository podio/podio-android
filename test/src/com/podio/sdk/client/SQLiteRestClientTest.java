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

package com.podio.sdk.client;

import org.mockito.Mockito;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.internal.request.RestOperation;

public class SQLiteRestClientTest extends InstrumentationTestCase {

	private SQLiteRestClient target;
	private RestClientDelegate mockDelegate;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mockDelegate = Mockito.mock(RestClientDelegate.class);
		target = new SQLiteRestClient(getInstrumentation().getContext(),
				"authority", mockDelegate, 10);
	}

	/**
	 * Verifies that a authorize rest operation is delegated properly to the
	 * {@link DatabaseClientDelegate}.
	 * 
	 * <pre>
	 * 
	 * 1. Create a new {@link SQLiteRestClient} and add a mock
	 *      {@link DatabaseClientDelegate} to it.
	 * 
	 * 2. Push a authorize operation to the client.
	 * 
	 * 3. Verify that the authorize method of the database helper
	 *      is called.
	 * 
	 * </pre>
	 */
	public void testAuthorizeOperationIsDelegatedCorrectly() {
		Mockito.when(
				mockDelegate.authorize(Mockito.<Uri> any(),
						Mockito.<PodioParser<?>> any())).thenReturn(
				RestResult.success());

		BasicPodioFilter filter = new BasicPodioFilter();
		RestRequest<Object> restRequest = new RestRequest<Object>() //
				.setFilter(filter) //
				.setOperation(RestOperation.AUTHORIZE);

		target.enqueue(restRequest);

		Mockito.verify(mockDelegate, Mockito.timeout(2000)).authorize(
				Mockito.<Uri> any(), Mockito.<PodioParser<?>> any());
		Mockito.verifyZeroInteractions(mockDelegate);
	}

	/**
	 * Verifies that an IllegalArgumentException is thrown when trying to create
	 * an SQLiteRestClient with a null pointer delegate.
	 * 
	 * <pre>
	 * 
	 * 1. Create a new SQLiteRestClient with a null pointer database delegate.
	 * 
	 * 2. Verify that an IllegalArgumentException was thrown.
	 * 
	 * </pre>
	 */
	public void testConstructorThrowsIllegalArgumentExceptionOnInvalidDelegates() {
		try {
			new SQLiteRestClient(null, null, null, 0);
			fail("Should have thrown exception");
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Verifies that a delete rest operation is delegated correctly to the
	 * {@link DatabaseClientDelegate}.
	 * 
	 * <pre>
	 * 
	 * 1. Create a new {@link SQLiteRestClient} and add a mock
	 *      {@link DatabaseClientDelegate} to it.
	 * 
	 * 2. Push a delete operation to the client.
	 * 
	 * 3. Verify that the delete method of the database helper
	 *      is called.
	 * 
	 * </pre>
	 */
	public void testDeleteOperationIsDelegatedCorrectly() {
		Mockito.when(
				mockDelegate.delete(Mockito.<Uri> any(),
						Mockito.<PodioParser<?>> any())).thenReturn(
				RestResult.success());

		RestRequest<Object> restRequest = new RestRequest<Object>() //
				.setFilter(new BasicPodioFilter()) //
				.setOperation(RestOperation.DELETE);

		target.enqueue(restRequest);

		Mockito.verify(mockDelegate, Mockito.timeout(2000)).delete(
				Mockito.<Uri> any(), Mockito.<PodioParser<?>> any());
		Mockito.verifyZeroInteractions(mockDelegate);
	}

	/**
	 * Verifies that a get rest operation is delegated correctly to the
	 * {@link DatabaseClientDelegate}.
	 * 
	 * <pre>
	 * 
	 * 1. Create a new {@link SQLiteRestClient} and add a mock
	 *      {@link DatabaseClientDelegate} to it.
	 * 
	 * 2. Push a get operation to the client.
	 * 
	 * 3. Verify that the query method of the database helper
	 *      is called.
	 * 
	 * </pre>
	 */
	public void testGetOperationIsDelegatedCorrectly() {
		Mockito.when(
				mockDelegate.get(Mockito.<Uri> any(),
						Mockito.<PodioParser<?>> any())).thenReturn(
				RestResult.success());

		RestRequest<Object> restRequest = new RestRequest<Object>() //
				.setFilter(new BasicPodioFilter()) //
				.setOperation(RestOperation.GET);

		target.enqueue(restRequest);

		Mockito.verify(mockDelegate, Mockito.timeout(2000)).get(
				Mockito.<Uri> any(), Mockito.<PodioParser<?>> any());
		Mockito.verifyZeroInteractions(mockDelegate);
	}

	/**
	 * Verifies that a post rest operation is delegated correctly to the
	 * {@link DatabaseClientDelegate}.
	 * 
	 * <pre>
	 * 
	 * 1. Create a new {@link SQLiteRestClient} and add a mock
	 *      {@link DatabaseClientDelegate} to it.
	 * 
	 * 2. Push a post operation to the client.
	 * 
	 * 3. Verify that the insert method of the database helper
	 *      is called.
	 * 
	 * </pre>
	 */
	public void testPostOperationIsDelegatedCorrectly() {
		Mockito.when(
				mockDelegate.post(Mockito.<Uri> any(), Mockito.any(),
						Mockito.<PodioParser<?>> any())).thenReturn(
				RestResult.success());

		RestRequest<Object> restRequest = new RestRequest<Object>() //
				.setFilter(new BasicPodioFilter()) //
				.setOperation(RestOperation.POST);

		target.enqueue(restRequest);

		Mockito.verify(mockDelegate, Mockito.timeout(2000)).post(
				Mockito.<Uri> any(), Mockito.any(),
				Mockito.<PodioParser<?>> any());
		Mockito.verifyZeroInteractions(mockDelegate);
	}

	/**
	 * Verifies that a put rest operation is delegated correctly to the
	 * {@link DatabaseClientDelegate}.
	 * 
	 * <pre>
	 * 
	 * 1. Create a new {@link SQLiteRestClient} and add a mock
	 *      {@link DatabaseClientDelegate} to it.
	 * 
	 * 2. Push a put operation to the client.
	 * 
	 * 3. Verify that the update method of the database helper
	 *      is called.
	 * 
	 * </pre>
	 */
	public void testPutOperationIsDelegatedCorrectly() {
		Mockito.when(
				mockDelegate.put(Mockito.<Uri> any(), Mockito.any(),
						Mockito.<PodioParser<?>> any())).thenReturn(
				RestResult.success());

		RestRequest<Object> restRequest = new RestRequest<Object>() //
				.setFilter(new BasicPodioFilter()) //
				.setOperation(RestOperation.PUT);

		target.enqueue(restRequest);

		Mockito.verify(mockDelegate, Mockito.timeout(2000)).put(
				Mockito.<Uri> any(), Mockito.any(),
				Mockito.<PodioParser<?>> any());
		Mockito.verifyZeroInteractions(mockDelegate);
	}
}
