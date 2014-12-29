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
package com.podio.sdk.domain;

import java.util.Date;

import com.podio.sdk.internal.Utils;

/**
 * @author Tobias Lindberg
 */
public class CalendarEvent {
    private final Long ref_id = null;
    private final String ref_type = null;
    private final Boolean busy = null;
    private final String start_utc = null;
    private final String end_utc = null;
    private final String start_time = null;
    private final String end_time = null;
    private final String title = null;
    private final String description = null;
    private final String location = null;
    private final App app = null;

    public static class App {

        private final String name = null;
        private final Space space = null;

        public static class Space {
            private final String name = null;
        }

        /**
         * @return returns null if the user don't have access permission to the
         *         space
         */
        public String getWorkspaceName() {
            if (space == null) {
                return null;
            }
            return space.name;
        }

        public String getAppName() {
            return name;
        }

    }

    /**
     * Gets the end date of the calendar event as a Java Date object.
     * 
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getEndDate() {
        return Utils.parseDateTime(end_utc);
    }

    public String getEndDateString() {
        return end_utc;
    }

    /**
     * Gets the start date of the calendar event as a Java Date object.
     * 
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getStartDate() {

        return Utils.parseDateTime(start_utc);
    }

    /**
     * @return returns true if you can rely on having a start time component in
     *         the UTC start date Date object, otherwise false. NOTE depending
     *         on the reference type (i.e. item, task,etc.) this
     *         {@link CalendarEvent} is representing, this event may have a
     *         start time but no end time or vice versa!
     */
    public boolean hasStartTime() {
        return start_time != null ? true : false;
    }

    /**
     * @return returns true if you can rely on having a end time component in
     *         the UTC end date Date object, otherwise false. NOTE depending on
     *         the reference type (i.e. item, task,etc.) this
     *         {@link CalendarEvent} is representing, this event may have a
     *         start time but no end time or vice versa!
     */
    public boolean hasEndTime() {
        return end_time != null ? true : false;
    }

    public String getStartDateString() {
        return start_utc;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Warning! Use this method with caution.
     * 
     * @return returns the name of the workspace or null if the user doesn't
     *         have access permission to the workspace which this calendar event
     *         is part of.
     */
    public String getWorkspaceName() {
        return app.getWorkspaceName();
    }

    public String getAppName() {
        return app.getAppName();
    }

    public boolean isBusy() {
        return Utils.getNative(busy, false);
    }

    /**
     * @return returns the id of the entry (which is either for a task or item)
     *         or 0 if no valid id exists
     */
    public long getRefId() {
        return Utils.getNative(ref_id, 0);
    }

    public String getRefType() {
        return ref_type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ref_id == null) ? 0 : ref_id.hashCode());
        result = prime * result + ((ref_type == null) ? 0 : ref_type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CalendarEvent other = (CalendarEvent) obj;
        if (ref_id == null) {
            if (other.ref_id != null)
                return false;
        } else if (!ref_id.equals(other.ref_id))
            return false;
        if (ref_type == null) {
            if (other.ref_type != null)
                return false;
        } else if (!ref_type.equals(other.ref_type))
            return false;
        return true;
    }

}
