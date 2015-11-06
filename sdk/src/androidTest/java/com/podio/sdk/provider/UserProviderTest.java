
package com.podio.sdk.provider;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.Request;
import com.podio.sdk.domain.Profile;
import com.podio.sdk.domain.User;
import com.podio.sdk.domain.UserStatus;
import com.podio.sdk.mock.MockRestClient;

public class UserProviderTest extends InstrumentationTestCase {

    @Mock
    Request.ResultListener<Profile> profileResultListener;

    @Mock
    Request.ResultListener<User> userResultListener;

    @Mock
    Request.ResultListener<UserStatus> statusResultListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    public void testGetUserData() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        UserProvider provider = new UserProvider();
        provider.setClient(mockClient);

        provider.getUser().withResultListener(userResultListener);

        Mockito.verify(userResultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/user"), mockClient.uri);
    }

    public void testGetUserStatus() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        UserProvider provider = new UserProvider();
        provider.setClient(mockClient);

        provider.getUserStatus().withResultListener(statusResultListener);

        Mockito.verify(statusResultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/user/status"), mockClient.uri);
    }

    public void testGetUserProfile() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        UserProvider provider = new UserProvider();
        provider.setClient(mockClient);
        provider.getProfile().withResultListener(profileResultListener);

        Mockito.verify(profileResultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/user/profile"), mockClient.uri);
    }
}
