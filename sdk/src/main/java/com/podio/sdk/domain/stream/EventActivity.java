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
package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.Byline;
import com.podio.sdk.domain.ReferenceType;
import com.podio.sdk.internal.Utils;

import java.util.Date;

/**
 * This class is the base class for all activities.
 * <p/>
 * In most cases all information we are interested in is provided by this class so even if you are
 * getting activities of type {@link UnknownEventActivity} there is still plenty of information
 * available in that one.
 *
 * @author Tobias Lindberg
 */
public abstract class EventActivity {

    public static enum EventType {
        comment,
        file,
        rating,
        creation,
        update,
        task,
        answer,
        rsvp,
        grant,
        reference,
        like,
        vote,
        participation,
        file_delete,
        unknown
    }

    private final String type = null;
    private final String activity_type = null;
    private final Byline created_by = null;
    private final String created_on = null;

    public Date getCreatedOnDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedOnString() {
        return created_on;
    }

    public Byline getCreatedBy() {
        return created_by;
    }

    public ReferenceType getType() {
        try {
            return ReferenceType.valueOf(type);
        } catch (NullPointerException e) {
            return ReferenceType.unknown;
        } catch (IllegalArgumentException e) {
            return ReferenceType.unknown;
        }
    }

    public EventType getActivityType() {
        try {
            return EventType.valueOf(activity_type);
        } catch (NullPointerException e) {
            return EventType.unknown;
        } catch (IllegalArgumentException e) {
            return EventType.unknown;
        }
    }
}
