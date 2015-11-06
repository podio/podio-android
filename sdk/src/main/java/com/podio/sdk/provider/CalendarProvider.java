
package com.podio.sdk.provider;

import android.text.format.DateFormat;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.CalendarEvent;

import java.util.Date;

/**
 * Enables access to the Calendar API end point.
 *
 */
public class CalendarProvider extends Provider {

    private static class CalendarFilter extends Filter {
        public static final String PATH = "calendar";

        public CalendarFilter() {
            super(PATH);
        }

        public CalendarFilter withWorkspaceId(long spaceId) {
            addPathSegment("space");
            addQueryParameter("space_id", Long.toString(spaceId, 10));
            addPathSegment(Long.toString(spaceId, 10));
            return this;

        }

        public CalendarFilter withDateFromTo(Date from, Date to) {

            String dateFrom = DateFormat.format("yyyy-MM-dd", from).toString();
            String dateTo = DateFormat.format("yyyy-MM-dd", to).toString();
            addQueryParameter("date_from", dateFrom);
            addQueryParameter("date_to", dateTo);
            return this;
        }

        public CalendarFilter withPriority(int priority) {
            addQueryParameter("priority", Integer.toString(priority));
            return this;
        }

        /**
         * This method will ensure that the request will return {@link CalendarEvent} objects with a
         * workspace name (if the requester has access to the workspace that this CalendarEvent is
         * associated with)
         *
         * @return
         */
        public CalendarFilter withWorkspaceNameField() {
            addQueryParameter("fields", "app.fields(space)");
            return this;
        }

        public CalendarFilter withTasks(boolean includeTasks) {
            addQueryParameter("tasks", Boolean.toString(includeTasks));
            return this;
        }
    }

    /**
     * Fetches all global calendar events.
     *
     * @param from
     *         The Date from which the result should start from.
     * @param to
     *         The Date from which the result should end at.
     * @param priority
     *         The priority level of the results.
     * @param includeTasks
     *         set to true if tasks should be included in the calendar, false otherwise.
     *
     * @return
     */
    public Request<CalendarEvent[]> getGlobalCalendar(Date from, Date to, int priority,
                                                      boolean includeTasks) {

        CalendarFilter filter = new CalendarFilter().withDateFromTo(from, to)
                .withPriority(priority).withTasks(includeTasks);

        return get(filter, CalendarEvent[].class);
    }

    /**
     * Fetches all space calendar events.
     *
     * @param spaceId
     * @param from
     * @param to
     * @param priority
     * @param includeTasks
     *         set to true if tasks should be included in the calendar, false otherwise.
     *
     * @return
     */
    public Request<CalendarEvent[]> getSpaceCalendar(long spaceId, Date from, Date to,
                                                     int priority, boolean includeTasks) {

        CalendarFilter filter = new CalendarFilter().withWorkspaceId(spaceId)
                .withDateFromTo(from, to).withPriority(priority).withTasks(includeTasks);

        return get(filter, CalendarEvent[].class);
    }
}
