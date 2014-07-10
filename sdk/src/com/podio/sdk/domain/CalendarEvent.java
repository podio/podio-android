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
    private final Boolean busy = null;
    private final String start_utc = null;
    private final String end_utc = null;
    private final String title = null;
    private final String description = null;
    private final String location = null;

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

    public boolean isBusy() {
        return Utils.getNative(busy, false);
    }

}
