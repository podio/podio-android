
package com.podio.sdk.provider;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.Request;
import com.podio.sdk.domain.Application;
import com.podio.sdk.volley.MockRestClient;

public class ApplicationProviderTest extends InstrumentationTestCase {

    @Mock
    Request.ResultListener<Application> resultListener;

    @Mock
    Request.ResultListener<Application[]> arrayResultListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }


    public void testFetchApplicationRequestsFullItemSetByDefault() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.get(2L).withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/2?type=full"), mockClient.uri);
    }


    public void testFetchApplicationMicroRequestsMicroItemSet() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.getMicro(2L).withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/2?type=micro"), mockClient.uri);
    }


    public void testFetchApplicationMiniRequestsMiniItemSet() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.getMini(6L).withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/6?type=mini"), mockClient.uri);
    }


    public void testFetchApplicationsForSpaceDoesntRequestInactiveItems() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.getAllActive(1L).withResultListener(arrayResultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/space/1?include_inactive=false"), mockClient.uri);
    }

    public void testFetchApplicationsForSpaceWithInactivesIncludedRequestsInactiveItems() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.getAll(2L).withResultListener(arrayResultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/space/2?include_inactive=true"), mockClient.uri);
    }


    public void testFetchApplicationShortRequestsShortItemSet() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ApplicationProvider provider = new ApplicationProvider();
        provider.setClient(mockClient);
        provider.getShort(2L).withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/app/2?type=short"), mockClient.uri);
    }

}
