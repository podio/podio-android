package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Recurrence;

/**
 * Created by sai on 9/30/16.
 */

public class RecurrenceProvider extends Provider {

    public static class RecurrenceFilter extends Filter {
        private static String itemRefType = "item";
        private static String refType = "ref_type";
        private static String refId = "ref_id";

        public RecurrenceFilter() {
            super("recurrence/");
        }

        public RecurrenceFilter item(long itemId) {
            this.addPathSegment(itemRefType);
            this.addPathSegment(String.valueOf(itemId));

            this.addQueryParameter(refType, itemRefType);
            this.addQueryParameter(refId, String.valueOf(itemId));

            return this;
        }
    }

    public Request<Recurrence> getItemRecurrence(long itemId) {
        return get(new RecurrenceFilter().item(itemId), Recurrence.class);
    }

    public Request<Void> deleteItemRecurrence(long itemId) {
        return delete(new RecurrenceFilter().item(itemId));
    }

    public Request<Recurrence> updateItemRecurrence(long itemId, Recurrence.CreateData createData) {
        return put(new RecurrenceFilter().item(itemId), createData, Recurrence.class);
    }
}
