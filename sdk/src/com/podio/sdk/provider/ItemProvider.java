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

import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClient;
import com.podio.sdk.domain.Item;
import com.podio.sdk.domain.ItemRequest;
import com.podio.sdk.filter.ItemFilter;

public class ItemProvider extends BasicPodioProvider {

    public ItemProvider(RestClient client) {
        super(client);
    }

    public Object addItem(long applicationId, Object data) {
        ItemFilter filter = new ItemFilter().withApplicationId(applicationId);
        PodioParser<Item.PushResult> parser = new PodioParser<Item.PushResult>(
                Item.PushResult.class);

        return pushRequest(filter, data, parser);
    }

    public Object fetchItem(long itemId) {
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        ItemFilter filter = new ItemFilter().withItemId(itemId);

        return fetchRequest(filter, parser);
    }

    public Object fetchItemsForApplication(long applicationId) {
        ItemFilter filter = new ItemFilter().withApplicationIdFilter(applicationId);
        PodioParser<ItemRequest.Result> parser = new PodioParser<ItemRequest.Result>(
                ItemRequest.Result.class);
        ItemRequest filterRequest = new ItemRequest(null, null, null, null, null, null);

        return pushRequest(filter, filterRequest, parser);
    }

    public Object updateItem(long itemId, Object data) {
        ItemFilter filter = new ItemFilter().withItemId(itemId);
        PodioParser<Item.PushResult> parser = new PodioParser<Item.PushResult>(
                Item.PushResult.class);

        return changeRequest(filter, data, parser);
    }

}