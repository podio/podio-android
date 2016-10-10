
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Item;
import com.podio.sdk.domain.ItemParticipation;

/**
 * Enables access to the item API end point.
 *
 */
public class ItemProvider extends Provider {

    static class Path extends Filter {

        Path() {
            super("item");
        }

        Path withApplicationId(long applicationId) {
            addPathSegment("app");
            addPathSegment(Long.toString(applicationId, 10));
            return this;
        }

        Path withApplicationIdFilter(long applicationId) {
            addPathSegment("app");
            addPathSegment(Long.toString(applicationId, 10));
            addPathSegment("filter");
            return this;
        }

        Path withApplicationAndViewIdFilter(long applicationId, long viewId) {
            addPathSegment("app");
            addPathSegment(Long.toString(applicationId, 10));
            addPathSegment("filter");
            addPathSegment(Long.toString(viewId, 10));
            return this;
        }

        Path withItemId(long itemId) {
            addPathSegment(Long.toString(itemId, 10));
            return this;
        }

        Path withParticipation(long itemId) {
            addPathSegment(Long.toString(itemId, 10));
            addPathSegment("participation");
            return this;
        }

    }

    /**
     * Enables a forced set of methods to be called in order to be able to filter items.
     *
     */
    public class ItemFilterProvider {
        private final Item.FilterData filterData;

        /**
         * Constructor.
         */
        private ItemFilterProvider() {
            filterData = new Item.FilterData();
        }

        /**
         * Prepares a filter constraint, used when fetching filtered items.
         *
         * @param key
         *         The constraint key.
         * @param value
         *         The constraint value.
         *
         * @return This instance of the ItemFilterProvider, to enable convenient chaining.
         *
         * @see com.podio.sdk.provider.ItemProvider.ItemFilterProvider#get(long)
         * @see com.podio.sdk.provider.ItemProvider.ItemFilterProvider#get(long, long)
         */
        public ItemFilterProvider onConstraint(String key, Object value) {
            filterData.addConstraint(key, value);
            return this;
        }

        /**
         * Prepares the remember filter, used when fetching filtered items.
         *
         * @param doRemember
         *         True if the API should remember this filter for you, otherwise false. Defaults to
         *         true.
         *
         * @return This instance of the ItemFilterProvider, to enable convenient chaining.
         *
         * @see com.podio.sdk.provider.ItemProvider.ItemFilterProvider#get(long)
         * @see com.podio.sdk.provider.ItemProvider.ItemFilterProvider#get(long, long)
         */
        public ItemFilterProvider onDoRemember(boolean doRemember) {
            filterData.setDoRemember(doRemember);
            return this;
        }

        /**
         * Prepares the limit and offset of the filter, used when fetching filtered items.
         *
         * @param maxCount
         *         The max number of items to fetch. The result could contain less items, but never
         *         more.
         * @param offset
         *         The zero-based offset of the first item in the span to fetch.
         *
         * @return This instance of the ItemFilterProvider, to enable convenient chaining.
         *
         * @see com.podio.sdk.provider.ItemProvider.ItemFilterProvider#get(long)
         * @see com.podio.sdk.provider.ItemProvider.ItemFilterProvider#get(long, long)
         */
        public ItemFilterProvider onSpan(int maxCount, int offset) {
            filterData.setLimit(maxCount);
            filterData.setOffset(offset);
            return this;
        }

        /**
         * Prepares the sort order of the filter, used when fetching filtered items.
         *
         * @param fieldName
         *         The name of the field to sort by.
         * @param doSortDescending
         *         True for a descending sort order, false for an ascending.
         *
         * @return This instance of the ItemFilterProvider, to enable convenient chaining.
         *
         * @see com.podio.sdk.provider.ItemProvider.ItemFilterProvider#get(long)
         * @see com.podio.sdk.provider.ItemProvider.ItemFilterProvider#get(long, long)
         */
        public ItemFilterProvider onSortOrder(String fieldName, boolean doSortDescending) {
            filterData.setOrderByField(fieldName, doSortDescending);
            return this;
        }

        /**
         * If you sort by a certain field and there are items that doesn't have this field set then
         * if sortNullLast is set to true then these not set values will be delivered in the end of
         * the sort result instead of the beginning.
         *
         * @param sortNullLast
         *         true if null values should be in the end, false otherwise.
         *
         * @return This instance of the ItemFilterProvider, to enable convenient chaining.
         *
         * @see com.podio.sdk.provider.ItemProvider.ItemFilterProvider#get(long)
         * @see com.podio.sdk.provider.ItemProvider.ItemFilterProvider#get(long, long)
         */
        public ItemFilterProvider onSortNullLast(boolean sortNullLast) {
            filterData.setSortNullLast(sortNullLast);

            return this;
        }

        /**
         * Fetches a set of filtered items for the application with the given id.
         * <p/>
         * If no filter data has been configured, then the default filter will be used, with a
         * behavior as the API sees fit.
         * <p/>
         * Note that, while the other methods in this class are optional, this method must be called
         * in order for the filtered request to take place
         *
         * @param applicationId
         *         The id of the parent application.
         *
         * @return A ticket which the caller can use to identify this request with.
         */
        public Request<Item.FilterResult> get(long applicationId) {
            Path filter = new Path().withApplicationIdFilter(applicationId);
            return post(filter, filterData, Item.FilterResult.class);
        }

        /**
         * Fetches a set of filtered items for the application with the given id and a given
         * view_id.
         * <p/>
         * If no filter data has been configured, then the default filter will be used, with a
         * behavior as the API sees fit.
         * <p/>
         * Note that, while the other methods in this class are optional, this method must be called
         * in order for the filtered request to take place
         *
         * @param applicationId
         *         The id of the parent application.
         * @param viewId
         *         The id of the view.
         *
         * @return A ticket which the caller can use to identify this request with.
         */
        public Request<Item.FilterResult> get(long applicationId, long viewId) {
            Path filter = new Path().withApplicationAndViewIdFilter(applicationId, viewId);
            return post(filter, filterData, Item.FilterResult.class);
        }
    }

    /**
     * Requests the API to create a new item
     *
     * @param applicationId
     *         The id of the application to which this item is to be added.
     * @param item
     *         The data describing the new item to create.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Item.CreateResult> create(long applicationId, Item item) {
        Path filter = new Path().withApplicationId(applicationId);
        Item.CreateData data = item.getCreateData(false);
        return post(filter, data, Item.CreateResult.class);
    }

    public Request<Void> delete(long itemId) {
        Path filter = new Path().withItemId(itemId);
        return delete(filter);
    }

    /**
     * Requests the API to update an item with new values.
     *
     * @param itemId
     *         The id of the item to update.
     * @param item
     *         The changed data bundle.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Item.CreateResult> update(long itemId, Item item) {
        // TODO: Consider a mirror "ChangeData" + "ChangeResult" implementation to avoid confusion.
        Path filter = new Path().withItemId(itemId);
        Item.CreateData data = item.getCreateData(false);
        return put(filter, data, Item.CreateResult.class);
    }

    /**
     * Fetches the single item with the given id.
     *
     * @param itemId
     *         The id of the item to fetch.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Item> get(long itemId) {
        Path filter = new Path().withItemId(itemId);
        return get(filter, Item.class);
    }

    /**
     * Enables filtered request of items.
     *
     * @return An ItemFilterProvider enabling the caller to configure the filter details.
     */
    public ItemFilterProvider filter() {
        return new ItemFilterProvider();
    }

    public Request<ItemParticipation> setParticipation(long itemId, ItemParticipation itemParticipation) {
        Path filter = new Path().withParticipation(itemId);
        return put(filter, itemParticipation, ItemParticipation.class);
    }
}
