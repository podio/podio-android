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
import android.net.Uri;
import android.test.AndroidTestCase;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.domain.Item;
import com.podio.sdk.domain.ItemRequest;
import com.podio.sdk.internal.request.ResultListener;
import com.podio.sdk.provider.mock.MockRestClient;

public class ItemProviderTest extends AndroidTestCase {

    /**
     * Verifies that the {@link ItemProvider} calls through to the (mock) rest
     * client with expected uri parameters when trying to add a new item.
     * 
     * <pre>
     * 
     * 1. Create a new ItemProvider.
     * 
     * 2. Try to push a new (mock) item.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testAddItem() {
        final Uri reference = Uri.parse("content://test.uri/item/app/2");

        final MockRestClient mockClient = new MockRestClient();
        @SuppressWarnings("unchecked")
		final ResultListener<Item.PushResult> mockListener = mock(ResultListener.class);

        ItemProvider target = new ItemProvider(mockClient);

        Object ticket = target.addItem(2L, new Item().getPushData(), mockListener);
        mockClient.mock_processLastPushedRestRequest(true, null, null);

        verify(mockListener).onSuccess(ticket, null);
        verifyNoMoreInteractions(mockListener);

        Uri uri = ((PodioFilter) ticket).buildUri("content", "test.uri");
        assertEquals(reference, uri);
    }

    /**
     * Verifies that the {@link ItemProvider} calls through to the (mock) rest
     * client with expected uri parameters when trying to fetch an existing
     * item.
     * 
     * <pre>
     * 
     * 1. Create a new ItemProvider.
     * 
     * 2. Try to fetch an item.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testFetchItem() {
        final Uri reference = Uri.parse("content://test.uri/item/3");

        final MockRestClient mockClient = new MockRestClient();
        @SuppressWarnings("unchecked")
		final ResultListener<Item> mockListener = mock(ResultListener.class);

        ItemProvider target = new ItemProvider(mockClient);

        Object ticket = target.fetchItem(3L, mockListener);
        mockClient.mock_processLastPushedRestRequest(true, null, null);
        
        verify(mockListener).onSuccess(ticket, null);
        verifyNoMoreInteractions(mockListener);

        Uri uri = ((PodioFilter) ticket).buildUri("content", "test.uri");
        assertEquals(reference, uri);
    }

    /**
     * Verifies that the {@link ItemProvider} calls through to the (mock) rest
     * client with expected uri parameters when trying to fetch all items for a
     * given application.
     * 
     * <pre>
     * 
     * 1. Create a new ItemProvider.
     * 
     * 2. Try to fetch all items for a known application.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testFetchItemsForApplication() {
        final Uri reference = Uri.parse("content://test.uri/item/app/4/filter");

        final MockRestClient mockClient = new MockRestClient();
        @SuppressWarnings("unchecked")
		final ResultListener<ItemRequest.Result> mockListener = mock(ResultListener.class);

        ItemProvider target = new ItemProvider(mockClient);

        Object ticket = target.fetchItemsForApplication(4L, mockListener);
        mockClient.mock_processLastPushedRestRequest(true, null, null);
        
        verify(mockListener).onSuccess(ticket, null);
        verifyNoMoreInteractions(mockListener);

        Uri uri = ((PodioFilter) ticket).buildUri("content", "test.uri");
        assertEquals(reference, uri);
    }

    /**
     * Verifies that the {@link ItemProvider} calls through to the (mock) rest
     * client with expected uri parameters when trying to update an existing
     * item.
     * 
     * <pre>
     * 
     * 1. Create a new ItemProvider.
     * 
     * 2. Try to update an item.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testUpdateItem() {
        final Uri reference = Uri.parse("content://test.uri/item/5");

        final MockRestClient mockClient = new MockRestClient();
        @SuppressWarnings("unchecked")
		final ResultListener<Item.PushResult> mockListener = mock(ResultListener.class);

        ItemProvider target = new ItemProvider(mockClient);

        Object ticket = target.updateItem(5, new Item().getPushData(), mockListener);
        mockClient.mock_processLastPushedRestRequest(true, null, null);

        verify(mockListener).onSuccess(ticket, null);
        verifyNoMoreInteractions(mockListener);

        Uri uri = ((PodioFilter) ticket).buildUri("content", "test.uri");
        assertEquals(reference, uri);
    }
}
