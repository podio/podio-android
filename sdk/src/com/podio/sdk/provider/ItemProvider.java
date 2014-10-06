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

import com.podio.sdk.Filter;
import com.podio.sdk.PodioRequest;
import com.podio.sdk.domain.Item;
import com.podio.sdk.volley.VolleyProvider;

/**
 * Enables access to the item API end point.
 * 
 * @author László Urszuly
 */
public class ItemProvider extends VolleyProvider {

    static class Path extends Filter {

        private Path() {
            super("item");
        }

        private Path withApplicationId(long applicationId) {
            addPathSegment("app");
            addPathSegment(Long.toString(applicationId, 10));
            return this;
        }

        private Path withApplicationIdFilter(long applicationId) {
            addPathSegment("app");
            addPathSegment(Long.toString(applicationId, 10));
            addPathSegment("filter");
            return this;
        }

        private Path withItemId(long itemId) {
            addPathSegment(Long.toString(itemId, 10));
            return this;
        }

    }

    /**
     * Enables a forced set of methods to be called in order to be able to
     * filter items.
     * 
     * @author László Urszuly
     */
    public class ItemFilterProvider {
        private final Item.FilterData filterData;

        /**
         * Constructor.
         */
        public ItemFilterProvider() {
            filterData = new Item.FilterData();
        }

        /**
         * Prepares a filter constraint, used when fetching filtered items.
         * 
         * @param key
         *        The constraint key.
         * @param value
         *        The constraint value.
         * @return This instance of the {@link ItemFilterProvider}, to enable
         *         convenient chaining.
         * @see {@link ItemFilterProvider#get(int, ResultListener, ErrorListener, SessionListener)}
         */
        public ItemFilterProvider onConstraint(String key, Object value) {
            filterData.addConstraint(key, value);
            return this;
        }

        /**
         * Prepares the remember filter, used when fetching filtered items.
         * 
         * @param doRemember
         *        True if the API should remember this filter for you, otherwise
         *        false. Defaults to true.
         * @return This instance of the {@link ItemFilterProvider}, to enable
         *         convenient chaining.
         * @see {@link ItemFilterProvider#get(int, ResultListener, ErrorListener, SessionListener)}
         */
        public ItemFilterProvider onDoRemember(boolean doRemember) {
            filterData.setDoRemember(doRemember);
            return this;
        }

        /**
         * Prepares the limit and offset of the filter, used when fetching
         * filtered items.
         * 
         * @param maxCount
         *        The max number of items to fetch. The result could contain
         *        less items, but never more.
         * @param offset
         *        The zero-based offset of the first item in the span to fetch.
         * @return This instance of the {@link ItemFilterProvider}, to enable
         *         convenient chaining.
         * @see {@link ItemFilterProvider#get(int, ResultListener, ErrorListener, SessionListener)}
         */
        public ItemFilterProvider onSpan(int maxCount, int offset) {
            filterData.setLimit(maxCount);
            filterData.setOffset(offset);
            return this;
        }

        /**
         * Prepares the sort order of the filter, used when fetching filtered
         * items.
         * 
         * @param fieldName
         *        The name of the field to sort by.
         * @param doSortDescending
         *        True for a descending sort order, false for an ascending.
         * @return This instance of the {@link ItemFilterProvider}, to enable
         *         convenient chaining.
         * @see {@link ItemFilterProvider#get(int, ResultListener, ErrorListener, SessionListener)}
         */
        public ItemFilterProvider onSortOrder(String fieldName, boolean doSortDescending) {
            filterData.setOrderByField(fieldName, doSortDescending);
            return this;
        }

        /**
         * Fetches a set of filtered items for the application with the given
         * id.
         * <p>
         * If no filter data has been configured, then the default filter will
         * be used, with a behavior as the API sees fit.
         * <p>
         * Note that, while the other methods in this class are optional, this
         * method must be called in order for the filtered request to take place
         * 
         * @param applicationId
         *        The id of the parent application.
         * @return A ticket which the caller can use to identify this request
         *         with.
         * @see {@link ItemFilterProvider#onConstraint(String, Object)}
         * @see {@link ItemFilterProvider#onRemember(boolean)}
         * @see {@link ItemFilterProvider#onSpan(int)}
         * @see {@link ItemFilterProvider#onSortOrder(String, boolean)}
         */
        public PodioRequest<Item.FilterResult> get(long applicationId) {
            Path filter = new Path().withApplicationIdFilter(applicationId);
            return post(filter, filterData, Item.FilterResult.class);
        }
    }

    /**
     * Requests the API to create a new item
     * 
     * @param applicationId
     *        The id of the application to which this item is to be added.
     * @param item
     *        The data describing the new item to create.
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<Item.PushResult> create(long applicationId, Item item) {
        Path filter = new Path().withApplicationId(applicationId);
        Item.PushData data = item.getPushData();
        return post(filter, data, Item.PushResult.class);
    }

    /**
     * Enables filtered request of items.
     * 
     * @return An ItemFilterProvider enabling the caller to configure the filter
     *         details.
     */
    public ItemFilterProvider filter() {
        return new ItemFilterProvider();
    }

    /**
     * Fetches the single item with the given id.
     * 
     * @param itemId
     *        The id of the item to fetch.
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<Item> get(long itemId) {
        Path filter = new Path().withItemId(itemId);
        return get(filter, Item.class);
    }

    /**
     * Requests the API to update an item with new values.
     * 
     * @param itemId
     *        The id of the item to update.
     * @param item
     *        The changed data bundle.
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<Item.PushResult> update(long itemId, Item item) {
        Path filter = new Path().withItemId(itemId);
        Item.PushData data = item.getPushData();
        return put(filter, data, Item.PushResult.class);
    }

}
