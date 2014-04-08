package com.podio.sdk.domain;

import java.util.List;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.podio.sdk.Filter;
import com.podio.sdk.ProviderListener;
import com.podio.sdk.domain.mock.MockRestClient;

public class AppItemProviderTest extends AndroidTestCase {

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

        AppItemProvider target = new AppItemProvider();
        target.setRestClient(new MockRestClient());
        target.setProviderListener(new ProviderListener() {
            @Override
            public void onRequestFailed(Object ticket, String message) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onRequestCompleted(Object ticket, List<?> items) {
                Uri target = ((Filter) ticket).buildUri("content", "test.uri");
                assertEquals(reference, target);
            }
        });

        target.fetchAppItemsForSpace(1);
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

        AppItemProvider target = new AppItemProvider();
        target.setRestClient(new MockRestClient());
        target.setProviderListener(new ProviderListener() {
            @Override
            public void onRequestFailed(Object ticket, String message) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onRequestCompleted(Object ticket, List<?> items) {
                Uri target = ((Filter) ticket).buildUri("content", "test.uri");
                assertEquals(reference, target);
            }
        });

        target.fetchAppItemsForSpaceWithInactivesIncluded(2);
    }
}
