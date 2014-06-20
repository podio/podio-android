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
import java.util.concurrent.Future;

import com.podio.sdk.ErrorListener;
import com.podio.sdk.RestClient;
import com.podio.sdk.ResultListener;
import com.podio.sdk.SessionListener;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.CalendarEvent;
import com.podio.sdk.filter.CalendarFilter;

/**
 * Enables access to the Calendar API end point.
 * 
 * @author Tobias Lindberg
 */
public class CalendarProvider extends BasicPodioProvider {

    public CalendarProvider(RestClient client) {
        super(client);
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
     * @param resultListener
     *        The callback implementation called when the calendar events are
     *        fetched. Null is valid, but doesn't make any sense.
     * @return
     */
    public Future<RestResult<CalendarEvent[]>> getGlobal(Date from, Date to, int priority, ResultListener<? super CalendarEvent[]> resultListener, ErrorListener errorListener, SessionListener sessionListener) {
        CalendarFilter filter = new CalendarFilter()
                .withDateFromTo(from, to)
                .withPriority(priority);

        return get(filter, CalendarEvent[].class, resultListener, errorListener, sessionListener);
    }
}
