package com.podio.sdk.provider;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.Request;
import com.podio.sdk.domain.Organization;
import com.podio.sdk.mock.MockRestClient;

public class OrganizationProviderTest extends InstrumentationTestCase {

    @Mock
    Request.ResultListener<Organization[]> resultListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    public void testGetAllOrganizations() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        OrganizationProvider provider = new OrganizationProvider();
        provider.setClient(mockClient);

        provider.getAll().withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/org"), mockClient.uri);
    }

}
