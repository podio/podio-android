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

package com.podio.sdk.domain.field;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Date extends Field {

    public static enum State {
        disabled, enabled, undefined
    }

    public static final class Config {
        public final String default_value = null;
        public final String description = null;
        public final Settings settings = null;
        public final Boolean required = null;
        public final String label = null;
        public final Boolean visible = null;
        public final Integer delta = null;
        public final Boolean hidden = null;
    }

    public static final class Settings {
        public final Boolean calendar = null;
        public final State end = null;
        public final State time = null;
    }

    public static final class Value {
        public final String end;
        public final String end_date = null;
        public final String end_date_utc = null;
        public final String end_time = null;
        public final String end_time_utc = null;
        public final String end_utc = null;
        public final String start;
        public final String start_date = null;
        public final String start_date_utc = null;
        public final String start_time = null;
        public final String start_time_utc = null;
        public final String start_utc = null;

        private static transient final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public Value(java.util.Date start) {
            this(start, null);
        }

        public Value(java.util.Date start, java.util.Date end) {
            this.start = start != null ? DATE_FORMAT.format(start) : null;
            this.end = end != null ? DATE_FORMAT.format(end) : null;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Value) {
                Value other = (Value) o;

                boolean equalStart = other.start_utc != null && other.start_utc.equals(this.start_utc)
                        || other.start_utc == null && this.start_utc == null;
                boolean equalEnd = other.end_utc != null && other.end_utc.equals(this.end_utc)
                        || other.end_utc == null && this.end_utc == null;

                return equalStart && equalEnd;
            }

            return false;
        }

        @Override
        public int hashCode() {
            int startHashCode = start_utc != null ? start_utc.hashCode() : 0;
            int endHashCode = end_utc != null ? end_utc.hashCode() : 0;

            return startHashCode + endHashCode;
        }
    }

    public final List<Value> values;

    public Date(String externalId) {
        super(externalId);
        this.values = new ArrayList<Value>();
    }

    @Override
    public boolean addValue(Object value) throws FieldTypeMismatchException {
        boolean isSuccess = false;
        Value v = tryCast(value);

        if (values != null && !values.contains(value)) {
            isSuccess = values.add(v);
        }

        return isSuccess;
    }

    @Override
    public Object getPushData() {
        List<Object> pushData = new ArrayList<Object>();

        if (values != null) {
            for (Value value : values) {
                if (value != null) {
                    HashMap<String, String> data = new HashMap<String, String>();

                    if (value.start_utc != null) {
                        data.put("start_utc", value.start_utc);
                    }

                    if (value.end_utc != null) {
                        data.put("end_utc", value.end_utc);
                    }

                    if (data.size() > 0) {
                        pushData.add(data);
                    }
                }
            }
        }

        return pushData;
    }

    @Override
    public boolean removeValue(Object value) throws FieldTypeMismatchException {
        boolean isSuccess = false;
        Value v = tryCast(value);

        if (values != null && values.contains(v)) {
            values.remove(v);
            isSuccess = true;
        }

        return isSuccess;
    }

    private Value tryCast(Object value) throws FieldTypeMismatchException {
        if (value instanceof Value) {
            return (Value) value;
        } else if (value instanceof java.util.Date) {
            return new Value((java.util.Date) value);
        } else {
            throw new FieldTypeMismatchException();
        }
    }

}
