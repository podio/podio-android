package com.podio.sdk.provider;

import java.text.ParseException;
import java.util.Date;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.Request;
import com.podio.sdk.domain.CalendarEvent;
import com.podio.sdk.internal.Utils;
import com.podio.sdk.volley.MockRestClient;

public class CalendarProviderTest extends InstrumentationTestCase {

    @Mock
    Request.ResultListener<CalendarEvent[]> resultListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }


    public void testGetGlobalCalendar() throws ParseException {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        CalendarProvider provider = new CalendarProvider();
        provider.setClient(mockClient);

        Date fromDate = Utils.parseDateDefault("1970-01-01");
        Date toDate = Utils.parseDateDefault("1970-01-02");

        provider.getGlobalCalendar(fromDate, toDate, 1, false).withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);

        assertEquals(Uri.parse("https://test/calendar?date_from=1970-01-01&date_to=1970-01-02&priority=1&tasks=false"), mockClient.uri);
    }
}
