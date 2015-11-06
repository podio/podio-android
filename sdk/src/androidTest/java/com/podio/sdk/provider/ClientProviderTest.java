
package com.podio.sdk.provider;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.net.Uri;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.Suppress;

import com.podio.sdk.Request;
import com.podio.sdk.volley.MockRestClient;

public class ClientProviderTest extends InstrumentationTestCase {

    @Mock
    Request.ResultListener<Void> resultListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Suppress
    public void testAuthenticateWithUserCredentials() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ClientProvider provider = new ClientProvider();
        provider.setClient(mockClient);

        provider.authenticateWithUserCredentials("USERNAME", "PASSWORD").withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(1000).times(0)).onRequestPerformed(null);

        assertEquals(Uri.parse("/oauth/token?grant_type=password&username=USERNAME&password=PASSWORD"), mockClient.uri);
    }


    @Suppress
    public void testAuthenticateWithAppCredentials() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ClientProvider provider = new ClientProvider();
        provider.setClient(mockClient);

        provider.authenticateWithAppCredentials("APPID", "APPTOKEN").withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("/oauth/token?grant_type=app&app_id=APPID&app_token=APPTOKEN"), mockClient.uri);
    }

}
