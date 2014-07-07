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

import java.sql.Date;

import org.mockito.Mockito;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.podio.sdk.ResultListener;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.CalendarEvent;
import com.podio.sdk.provider.mock.DummyRestClient;

public class CalendarProviderTest extends AndroidTestCase {

    /**
     * Verifies that the {@link CalendarProvider} calls through to the (mock)
     * rest client with expected uri parameters when trying to get the global
     * calendar.
     * 
     * <pre>
     * 
     * 1. Create a new {@link CalendarProvider}.
     * 
     * 2. Make a request for the global calendar.
     * 
     * 3. Verify that the designated rest client is called with a uri that
     *      contains the expected parameters.
     * 
     * </pre>
     */
    public void testGetGlobalCalendar() {
        DummyRestClient mockClient = new DummyRestClient(RestResult.success());
        CalendarProvider provider = new CalendarProvider();
        provider.setRestClient(mockClient);

        @SuppressWarnings("unchecked")
        ResultListener<CalendarEvent[]> mockListener = Mockito.mock(ResultListener.class);

        Date fromDate = Date.valueOf("1970-01-01");
        Date toDate = Date.valueOf("1970-01-02");
        provider.getGlobal(fromDate, toDate, 1, mockListener, null, null);

        Mockito.verify(mockListener).onRequestPerformed(null);
        Mockito.verifyNoMoreInteractions(mockListener);

        Uri uri = mockClient.mock_getUri();
        assertEquals(Uri.parse("content://test.uri/calendar?date_from=1970-01-01&date_to=1970-01-02&priority=1"), uri);
    }

}
