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

import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Item;
import com.podio.sdk.domain.ItemRequest;
import com.podio.sdk.internal.request.ResultListener;
import com.podio.sdk.provider.mock.DummyRestClient;

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
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ItemProvider provider = new ItemProvider(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Item.PushResult> mockListener = Mockito.mock(ResultListener.class);
        provider.addItem(2L, new Item().getPushData(), mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.getMockUri();
        assertEquals(Uri.parse("content://test.uri/item/app/2"), uri);
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
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ItemProvider provider = new ItemProvider(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Item> mockListener = Mockito.mock(ResultListener.class);
        provider.fetchItem(3L, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.getMockUri();
        assertEquals(Uri.parse("content://test.uri/item/3"), uri);
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
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ItemProvider provider = new ItemProvider(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<ItemRequest.Result> mockListener = Mockito.mock(ResultListener.class);
        provider.fetchItemsForApplication(4L, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.getMockUri();
        assertEquals(Uri.parse("content://test.uri/item/app/4/filter"), uri);
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
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ItemProvider provider = new ItemProvider(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Item.PushResult> mockListener = Mockito.mock(ResultListener.class);
        provider.updateItem(5, new Item().getPushData(), mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.getMockUri();
        assertEquals(Uri.parse("content://test.uri/item/5"), uri);
    }
}
