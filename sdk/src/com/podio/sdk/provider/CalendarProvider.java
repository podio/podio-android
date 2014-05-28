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

import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClient;
import com.podio.sdk.domain.CalendarEvent;
import com.podio.sdk.filter.CalendarFilter;

public class CalendarProvider extends BasicPodioProvider {

	public CalendarProvider(RestClient client) {
		super(client);
	}

	public Object fetchGlobalCalendar(Date from, Date to, int priority) {
		CalendarFilter filter = new CalendarFilter().withDateFromTo(from, to)
				.withPriority(priority);
		PodioParser<CalendarEvent[]> parser = new PodioParser<CalendarEvent[]>(
				CalendarEvent[].class);
		return fetchRequest(filter, parser);
	}
}