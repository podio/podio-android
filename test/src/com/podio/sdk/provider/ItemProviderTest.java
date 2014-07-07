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
import com.podio.sdk.domain.Item;
import com.podio.sdk.provider.mock.DummyRestClient;

public class ItemProviderTest extends AndroidTestCase {

    /**
     * Verifies that the {@link ItemProvider} calls through to the (mock) rest
     * client with expected uri parameters when trying to create a new item.
     * 
     * <pre>
     * 
     * 1. Create a new ItemProvider.
     * 
     * 2. Try to create a new (mock) item.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testCreateItem() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ItemProvider provider = new ItemProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Item.PushResult> mockListener = Mockito.mock(ResultListener.class);
        provider.create(2, new Item(), mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/item/app/2"), uri);
    }

    /**
     * Verifies that the {@link ItemProvider} throws a NullPointerException when
     * trying to create a new item with a null pointer.
     * 
     * <pre>
     * 
     * 1. Create a new ItemProvider.
     * 
     * 2. Try to create a new item with a null pointer data description.
     * 
     * 3. Verify that a NullPointerException is thrown.
     * 
     * </pre>
     */
    public void testCreateItemWithNullPointer() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.failure(null));
        ItemProvider provider = new ItemProvider();
        provider.setRestClient(mockClient);

        try {
            provider.create(7, null, null, null, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Verifies that the {@link ItemProvider} calls through to the (mock) rest
     * client with expected uri parameters when trying to get a filtered set of
     * items.
     * 
     * <pre>
     * 
     * 1. Create a new ItemProvider.
     * 
     * 2. Try to get a filtered set of items.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testFilterItems() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ItemProvider provider = new ItemProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Item.FilterResult> mockListener = Mockito.mock(ResultListener.class);
        provider.filter()
                .onConstraint("test-key", "test-value")
                .onDoRemember(false)
                .onSortOrder("test-column", false)
                .onSpan(100, 1000)
                .get(4, mockListener, null, null);

        Object data = mockClient.mock_getRequestData();
        assertTrue(data instanceof Item.FilterData);
        Item.FilterData f = (Item.FilterData) data;
        assertTrue(f.hasConstraint("test-key"));
        assertEquals("test-value", f.getConstraint("test-key").toString());
        assertEquals(false, f.getDoRemember());
        assertEquals("test-column", f.getSortKey());
        assertEquals(false, f.getDoSortDescending());
        assertEquals(100, f.getLimit());
        assertEquals(1000, f.getOffset());

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/item/app/4/filter"), uri);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);
    }

    /**
     * Verifies that the {@link ItemProvider} calls through to the (mock) rest
     * client with expected uri parameters when trying to get an existing item.
     * 
     * <pre>
     * 
     * 1. Create a new ItemProvider.
     * 
     * 2. Try to get an item.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testGetItem() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        ItemProvider provider = new ItemProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Item> mockListener = Mockito.mock(ResultListener.class);
        provider.get(3, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/item/3"), uri);
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
        ItemProvider provider = new ItemProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<Item.PushResult> mockListener = Mockito.mock(ResultListener.class);
        provider.update(5, new Item(), mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/item/5"), uri);
    }

    /**
     * Verifies that the {@link ItemProvider} throws a NullPointerException when
     * trying to update an item with a null pointer.
     * 
     * <pre>
     * 
     * 1. Create a new ItemProvider.
     * 
     * 2. Try to update an item with a null pointer data description.
     * 
     * 3. Verify that a NullPointerException is thrown.
     * 
     * </pre>
     */
    public void testUpdateItemWithNullPointer() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.failure(null));
        ItemProvider provider = new ItemProvider();
        provider.setRestClient(mockClient);

        try {
            provider.update(7, null, null, null, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
