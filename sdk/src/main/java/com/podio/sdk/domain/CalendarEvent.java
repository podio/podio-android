
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

import java.util.Date;

/**
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
    private final Application app = null;
    private final Boolean forged = null;
    private final String color = null;

    /**
     * Gets the end date of the calendar event as a Java Date object.
     *
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getEndDate() {
        return Utils.parseDateTimeUtc(end_utc);
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

        return Utils.parseDateTimeUtc(start_utc);
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

    public Application getApp(){
        return app;
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

    public Boolean isForged() {
        return Utils.getNative(forged, false);
    }

    public String getColor() {
        return "#" + color;
    }

    public String getCategoryValue() {
        // TODO :: Placeholder text for now, will replace with API returned value
        return "Category label";
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
