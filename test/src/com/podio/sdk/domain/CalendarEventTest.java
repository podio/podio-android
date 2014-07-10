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

import java.util.Calendar;
import java.util.Date;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

/**
 * @author László Urszuly
 */
public class CalendarEventTest extends AndroidTestCase {

    /**
     * Verifies that the members of the {@link CalendarEvent} class can be
     * initialized by parsing JSON.
     * 
     * <pre>
     * 
     * 1. Describe a CalendarEvent domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON string.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testCalendarEventCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("busy:true,")
                .append("start_utc:'2014-07-10 15:57:57',")
                .append("end_utc:'2014-07-10 15:58:12',")
                .append("title:'TITLE',")
                .append("description:'DESCRIPTION',")
                .append("location:'LOCATION'")
                .append("}").toString();

        Gson gson = new Gson();
        CalendarEvent event = gson.fromJson(json, CalendarEvent.class);

        assertNotNull(event);
        assertEquals(true, event.isBusy());
        assertEquals("2014-07-10 15:57:57", event.getStartDateString());

        Date startDate = event.getStartDate();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        assertNotNull(startDate);
        assertEquals(2014, startCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, startCalendar.get(Calendar.MONTH));
        assertEquals(10, startCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, startCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(57, startCalendar.get(Calendar.MINUTE));
        assertEquals(57, startCalendar.get(Calendar.SECOND));

        assertEquals("2014-07-10 15:58:12", event.getEndDateString());

        Date endDate = event.getEndDate();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        assertNotNull(endDate);
        assertEquals(2014, endCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, endCalendar.get(Calendar.MONTH));
        assertEquals(10, endCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, endCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(58, endCalendar.get(Calendar.MINUTE));
        assertEquals(12, endCalendar.get(Calendar.SECOND));

        assertEquals("TITLE", event.getTitle());
        assertEquals("DESCRIPTION", event.getDescription());
        assertEquals("LOCATION", event.getLocation());
    }

    /**
     * Verifies that the members of the {@link CalendarEvent} class can be
     * initialized to default by instantiation.
     * 
     * <pre>
     * 
     * 1. Create a new CalendarEvent object instance.
     * 
     * 2. Verify that the members have the default values.
     * 
     * </pre>
     */
    public void testCalendarEventCanBeCreatedFromInstantiation() {
        CalendarEvent event = new CalendarEvent();

        assertNotNull(event);
        assertEquals(false, event.isBusy());
        assertEquals(null, event.getStartDateString());
        assertEquals(null, event.getStartDate());
        assertEquals(null, event.getEndDateString());
        assertEquals(null, event.getEndDate());
        assertEquals(null, event.getTitle());
        assertEquals(null, event.getDescription());
        assertEquals(null, event.getLocation());
    }

}
