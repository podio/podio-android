package com.podio.sdk;

import android.content.Context;

import com.podio.sdk.client.CachedRestClient;
import com.podio.sdk.client.delegate.HttpClientDelegate;
import com.podio.sdk.client.delegate.SQLiteClientDelegate;
import com.podio.sdk.domain.AppItemProvider;
import com.podio.sdk.domain.SessionProvider;

/**
 * Enables easy access to the Podio API with a basic configuration which should
 * be suitable for most third party developers.
 * 
 * @author László Urszuly
 */
public final class Podio {

    /**
     * Helps fetching a predefined set of AppItem items using default filters.
     * 
     * @author László Urszuly
     */
    public static final class App {

        /**
         * Creates a new instance of the AppItem section of the Podio facade.
         * This constructor is hidden.
         */
        private App() {
        }

        /**
         * Fetches all App items in the workspace with the given id.
         * 
         * @param spaceId
         *            The id of the parent workspace.
         * @param providerListener
         *            The callback implementation called when the items are
         *            fetched. Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object fetch(long spaceId, ProviderListener providerListener) {
            AppItemProvider provider = new AppItemProvider();
            provider.setRestClient(client);
            provider.setProviderListener(providerListener);

            return provider.fetchAppItemsForSpace(spaceId);
        }

        /**
         * Fetches all App items, including the inactive ones, in the workspace
         * with the given id.
         * 
         * @param spaceId
         *            The id of the parent workspace.
         * @param providerListener
         *            The callback implementation called when the items are
         *            fetched. Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object fetchIncludingInactive(long spaceId,
                ProviderListener providerListener) {

            AppItemProvider provider = new AppItemProvider();
            provider.setRestClient(client);
            provider.setProviderListener(providerListener);

            return provider.fetchAppItemsForSpaceWithInactivesIncluded(spaceId);
        }
    }

    public static final class Client {

        public static final Object authenticateWithCredentials(String username, String password,
                ProviderListener providerListener) {

            SessionProvider provider = new SessionProvider();
            provider.setRestClient(client);
            provider.setProviderListener(providerListener);

            return provider.authenticateWithUserCredentials(clientId, clientSecret, username,
                    password);
        }

        public static final Object authenticateWithApp(String appId, String appToken,
                ProviderListener providerListener) {

            SessionProvider provider = new SessionProvider();
            provider.setRestClient(client);
            provider.setProviderListener(providerListener);

            return provider.authenticateWithAppCredentials(clientId, clientSecret, appId, appToken);
        }

        public static final void revokeSession(Session session) {
            networkDelegate.setSession(session);
        }
    }

    private static final String AUTHORITY = "api.podio.com";
    private static final String DATABASE_NAME = "podio.db";
    private static final int DATABASE_VERSION = 1;
    private static final int QUEUE_CAPACITY = 10;

    private static SQLiteClientDelegate cacheDelegate;
    private static HttpClientDelegate networkDelegate;

    private static CachedRestClient client;
    private static String clientId;
    private static String clientSecret;

    /**
     * Creates a new instance of the Podio facade. You have to call the
     * {@link Podio#setup(Context, String, String)} method before you start
     * using this object.
     */
    public Podio() {
    }

    public static void setup(Context context, String clientId, String clientSecret) {
        Podio.clientId = clientId;
        Podio.clientSecret = clientSecret;
        Podio.networkDelegate = new HttpClientDelegate(context);
        Podio.cacheDelegate = new SQLiteClientDelegate(context, DATABASE_NAME, DATABASE_VERSION);
        Podio.client = new CachedRestClient(context, AUTHORITY, networkDelegate, cacheDelegate,
                QUEUE_CAPACITY);
    }
}
