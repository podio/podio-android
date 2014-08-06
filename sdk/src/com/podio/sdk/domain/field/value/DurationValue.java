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

import java.util.HashMap;

import com.podio.sdk.internal.Utils;

/**
 * @author László Urszuly
 */
public final class DurationValue extends AbstractValue {
    private final Long value;

    public DurationValue(long value) {
        this.value = Long.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DurationValue) {
            DurationValue other = (DurationValue) o;

            if (other != null && other.value != null && this.value != null) {
                return other.value.longValue() == this.value.longValue();
            }
        }

        return false;
    }

    @Override
    public Object getPushData() {
        HashMap<String, Long> data = null;

        if (value != null) {
            data = new HashMap<String, Long>();
            data.put("value", value);
        }

        return data;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public long getDuration() {
        return Utils.getNative(value, 0L);
    }
}
