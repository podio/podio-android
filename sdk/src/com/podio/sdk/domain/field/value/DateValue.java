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

package com.podio.sdk.domain.field.value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.podio.sdk.internal.utils.Utils;

/**
 * @author László Urszuly
 */
public final class DateValue extends AbstractValue {
    private static final transient SimpleDateFormat FORMATTER_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final transient SimpleDateFormat FORMATTER_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final transient SimpleDateFormat FORMATTER_TIME = new SimpleDateFormat("HH:mm:ss");

    private final String end;
    private final String end_date = null;
    private final String end_date_utc = null;
    private final String end_time = null;
    private final String end_time_utc = null;
    private final String end_utc = null;
    private final String start;
    private final String start_date = null;
    private final String start_date_utc = null;
    private final String start_time = null;
    private final String start_time_utc = null;
    private final String start_utc = null;

    public DateValue(java.util.Date start) {
        this(start, null);
    }

    public DateValue(java.util.Date start, java.util.Date end) {
        this.start = start != null ? FORMATTER_DATETIME.format(start) : null;
        this.end = end != null ? FORMATTER_DATETIME.format(end) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DateValue) {
            DateValue other = (DateValue) o;

            boolean equalStart = other.start != null && other.start.equals(this.start)
                    || other.start == null && this.start == null;
            boolean equalEnd = other.end != null && other.end.equals(this.end)
                    || other.end == null && this.end == null;

            return equalStart && equalEnd;
        }

        return false;
    }

    @Override
    public Object getPushData() {
        HashMap<String, String> data = null;

        if (Utils.notEmpty(start) || Utils.notEmpty(end)) {
            data = new HashMap<String, String>();

            if (Utils.notEmpty(start)) {
                data.put("start", start);
            }

            if (Utils.notEmpty(end)) {
                data.put("end", end);
            }
        }

        return data;
    }

    @Override
    public int hashCode() {
        String s1 = start != null ? start : "";
        String s2 = end != null ? end : "";

        return (s1 + s2).hashCode();
    }

    public java.util.Date getEndDateTime() {
        return parse(end, FORMATTER_DATETIME);
    }

    public java.util.Date getEndDate() {
        return parse(end_date, FORMATTER_DATE);
    }

    public java.util.Date getEndDateUtc() {
        return parse(end_date_utc, FORMATTER_DATE);
    }

    public java.util.Date getEndTime() {
        return parse(end_time, FORMATTER_TIME);
    }

    public java.util.Date getEndTimeUtc() {
        return parse(end_time_utc, FORMATTER_TIME);
    }

    public java.util.Date getEndUtc() {
        return parse(end_utc, FORMATTER_DATETIME);
    }

    public java.util.Date getStartDateTime() {
        return parse(start, FORMATTER_DATETIME);
    }

    public java.util.Date getStartDate() {
        return parse(start_date, FORMATTER_DATE);
    }

    public java.util.Date getStartDateUtc() {
        return parse(start_date_utc, FORMATTER_DATE);
    }

    public java.util.Date getStartTime() {
        return parse(start_time, FORMATTER_TIME);
    }

    public java.util.Date getStartTimeUtc() {
        return parse(start_time_utc, FORMATTER_TIME);
    }

    public java.util.Date getStartUtc() {
        return parse(start_utc, FORMATTER_DATETIME);
    }

    private java.util.Date parse(String value, SimpleDateFormat formatter) {
        java.util.Date date = null;

        if (formatter != null && Utils.notEmpty(value)) {
            try {
                date = formatter.parse(value);
            } catch (ParseException e) {
                date = null;
            }
        }

        return date;
    }
}
