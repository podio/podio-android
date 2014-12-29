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

import java.util.Date;

import android.text.format.DateFormat;

import com.podio.sdk.Filter;
import com.podio.sdk.Request;
import com.podio.sdk.domain.CalendarEvent;
import com.podio.sdk.volley.VolleyProvider;

/**
 * Enables access to the Calendar API end point.
 * 
 * @author Tobias Lindberg
 */
public class CalendarProvider extends VolleyProvider {

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
         * This method will ensure that the request will return
         * {@link CalendarEvent} objects with a workspace name (if the requester
         * has access to the workspace that this CalendarEvent is associated
         * with)
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
     *        The Date from which the result should start from.
     * @param to
     *        The Date from which the result should end at.
     * @param priority
     *        The priority level of the results.
     * @param includeTasks
     *        set to true if tasks should be included in the calendar, false
     *        otherwise.
     * @return
     */
    public Request<CalendarEvent[]> getGlobalCalendar(Date from, Date to, int priority,
            boolean includeTasks) {

        CalendarFilter filter = new CalendarFilter().withDateFromTo(from, to)
                .withPriority(priority).withTasks(includeTasks).withWorkspaceNameField();

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
     *        set to true if tasks should be included in the calendar, false
     *        otherwise.
     * @return
     */
    public Request<CalendarEvent[]> getSpaceCalendar(long spaceId, Date from, Date to,
            int priority, boolean includeTasks) {

        CalendarFilter filter = new CalendarFilter().withWorkspaceId(spaceId)
                .withDateFromTo(from, to).withPriority(priority).withTasks(includeTasks);

        return get(filter, CalendarEvent[].class);
    }
}
