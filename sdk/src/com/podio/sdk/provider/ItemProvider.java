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

import com.podio.sdk.RestClient;
import com.podio.sdk.domain.Item;
import com.podio.sdk.domain.ItemRequest;
import com.podio.sdk.filter.ItemFilter;
import com.podio.sdk.internal.request.ResultListener;

public class ItemProvider extends BasicPodioProvider {

    public ItemProvider(RestClient client) {
        super(client);
    }

    public Object addItem(long applicationId, Item.PushData data, ResultListener<? super Item.PushResult> resultListener) {
        ItemFilter filter = new ItemFilter().withApplicationId(applicationId);

        return post(filter, data, Item.PushResult.class, resultListener);
    }

    public Object fetchItem(long itemId, ResultListener<? super Item> resultListener) {
        ItemFilter filter = new ItemFilter().withItemId(itemId);

        return get(filter, Item.class, resultListener);
    }

    public Object fetchItemsForApplication(long applicationId, ResultListener<? super ItemRequest.Result> resultListener) {
        ItemFilter filter = new ItemFilter().withApplicationIdFilter(applicationId);
        ItemRequest filterRequest = new ItemRequest(null, null, null, null, null, null);

        return post(filter, filterRequest, ItemRequest.Result.class, resultListener);
    }

    public Object updateItem(long itemId, Item.PushData data, ResultListener<? super Item.PushResult> resultListener) {
        ItemFilter filter = new ItemFilter().withItemId(itemId);

        return put(filter, data, Item.PushResult.class, resultListener);
    }

}
