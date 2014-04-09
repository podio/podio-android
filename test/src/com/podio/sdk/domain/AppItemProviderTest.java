package com.podio.sdk.domain;

import java.util.List;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.podio.sdk.Filter;
import com.podio.sdk.ProviderListener;
import com.podio.sdk.domain.mock.MockRestClient;

public class AppItemProviderTest extends AndroidTestCase {

    private static final class ConcurrentResult {
        private boolean isSuccessCalled = false;
        private boolean isFailureCalled = false;
        private Object ticket = null;
    }

    /**
     * Verifies that the {@link AppItemProvider} doesn't request inactive app
     * items by default.
     * 
     * <pre>
     * 
     * 1. Create a new AppItemProvider.
     * 
     * 2. Make a default request of app items for any workspace.
     * 
     * 3. Verify that the designated rest client is called with a Uri that
     *      explicitly has set the "include inactive" flag to false.
     * 
     * </pre>
     */
    public void testFetchAppItemsForSpaceDoesntRequestInactiveItems() {
        final Uri reference = Uri.parse("content://test.uri/app/space/1?include_inactive=false");
        final MockRestClient mockClient = new MockRestClient();
        final ConcurrentResult result = new ConcurrentResult();

        AppItemProvider target = new AppItemProvider();
        target.setRestClient(mockClient);
        target.setProviderListener(new ProviderListener() {
            @Override
            public void onRequestFailed(Object ticket, String message) {
                result.isFailureCalled = true;
                result.ticket = ticket;
            }

            @Override
            public void onRequestCompleted(Object ticket, List<?> items) {
                result.isSuccessCalled = true;
                result.ticket = ticket;
            }
        });

        target.fetchAppItemsForSpace(1);
        mockClient.mock_processLastPushedRestRequest(true, null, null);

        assertEquals(true, result.isSuccessCalled);
        assertEquals(false, result.isFailureCalled);

        Uri uri = ((Filter) result.ticket).buildUri("content", "test.uri");
        assertEquals(reference, uri);
    }

    /**
     * Verifies that the {@link AppItemProvider} requests inactive app items as
     * well through the custom fetch method.
     * 
     * <pre>
     * 
     * 1. Create a new AppItemProvider.
     * 
     * 2. Make a custom request of app items for any workspace.
     * 
     * 3. Verify that the designated rest client is called with a Uri that
     *      explicitly has set the "include inactive" flag to true.
     * 
     * </pre>
     */
    public void testfetchAppItemsForSpaceWithInactivesIncludedRequestsInactiveItems() {
        final Uri reference = Uri.parse("content://test.uri/app/space/2?include_inactive=true");
        final MockRestClient mockClient = new MockRestClient();
        final ConcurrentResult result = new ConcurrentResult();

        AppItemProvider target = new AppItemProvider();
        target.setRestClient(mockClient);
        target.setProviderListener(new ProviderListener() {
            @Override
            public void onRequestFailed(Object ticket, String message) {
                result.isFailureCalled = true;
                result.ticket = ticket;
            }

            @Override
            public void onRequestCompleted(Object ticket, List<?> items) {
                result.isSuccessCalled = true;
                result.ticket = ticket;
            }
        });

        target.fetchAppItemsForSpaceWithInactivesIncluded(2);
        mockClient.mock_processLastPushedRestRequest(true, null, null);

        assertEquals(true, result.isSuccessCalled);
        assertEquals(false, result.isFailureCalled);

        Uri uri = ((Filter) result.ticket).buildUri("content", "test.uri");
        assertEquals(reference, uri);
    }
}
