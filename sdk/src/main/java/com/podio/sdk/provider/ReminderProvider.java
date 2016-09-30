package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Reminder;

/**
 * Created by sai on 9/30/16.
 */

public class ReminderProvider extends Provider {

    public static class GetReminderFilter extends Filter {
        private static String itemRefType = "item";
        private static String refType = "ref_type";
        private static String refId = "ref_id";

        public GetReminderFilter() {
            super("reminder/");
        }

        public GetReminderFilter item(long itemId) {
            this.addPathSegment(itemRefType);
            this.addPathSegment(String.valueOf(itemId));

            this.addQueryParameter(refType, itemRefType);
            this.addQueryParameter(refId, String.valueOf(itemId));

            return this;
        }

    }

    public Request<Reminder> getItemReminder(long itemId) {
        return get(new GetReminderFilter().item(itemId), Reminder.class);
    }

    public Request<Void> deleteItemReminder(long itemId) {
        return delete(new GetReminderFilter().item(itemId));
    }

    public Request<Reminder> updateItemReminder(long itemId, Reminder.CreateData createData) {
        return put(new GetReminderFilter().item(itemId), createData, Reminder.class);
    }

}
