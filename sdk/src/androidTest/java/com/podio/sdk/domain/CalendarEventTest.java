
package com.podio.sdk.domain;

import java.util.Calendar;
import java.util.TimeZone;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class CalendarEventTest extends AndroidTestCase {


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

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        startCalendar.setTime(event.getStartDate());

        assertEquals(2014, startCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, startCalendar.get(Calendar.MONTH));
        assertEquals(10, startCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, startCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(57, startCalendar.get(Calendar.MINUTE));
        assertEquals(57, startCalendar.get(Calendar.SECOND));

        assertEquals("2014-07-10 15:58:12", event.getEndDateString());

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        endCalendar.setTime(event.getEndDate());

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
