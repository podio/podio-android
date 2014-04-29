package com.podio.sdk;

import android.content.Context;

import com.podio.sdk.client.CachedRestClient;
import com.podio.sdk.client.delegate.HttpClientDelegate;
import com.podio.sdk.client.delegate.SQLiteClientDelegate;
import com.podio.sdk.domain.AppItemProvider;
import com.podio.sdk.domain.Session;
import com.podio.sdk.domain.SessionFilter;
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
            // Hiding the constructor of this class as it's not meant to be
            // instantiated.
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

    /**
     * Enables means of authentication and basic session management with the
     * Podio servers.
     * 
     * @author László Urszuly
     * 
     */
    public static final class Client {

        private Client() {
            // Hiding the constructor of this class as it's not meant to be
            // instantiated.
        }

        /**
         * Authenticates the caller with the given user credentials. On success
         * a new session object with the access and refresh tokens will be
         * delivered through the given {@link ProviderListener}.
         * 
         * @param username
         *            The user name of the Podio account to authenticate with.
         * @param password
         *            The corresponding password of the Podio account.
         * @param providerListener
         *            The callback implementation called when the items are
         *            fetched. Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object authenticateAsUser(String username, String password,
                ProviderListener providerListener) {

            SessionProvider provider = new SessionProvider();
            provider.setRestClient(client);
            provider.setProviderListener(providerListener);

            return provider.authenticateWithUserCredentials(clientId, clientSecret, username,
                    password);
        }

        /**
         * Authenticates the caller with the given app credentials. On success a
         * new session object with the access and refresh tokens will be
         * delivered through the given {@link ProviderListener}.
         * 
         * @param appId
         *            The id of the app to authenticate with.
         * @param appToken
         *            The token that has been generated for a particular app.
         * @param providerListener
         *            The callback implementation called when the items are
         *            fetched. Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object authenticateAsApp(String appId, String appToken,
                ProviderListener providerListener) {

            SessionProvider provider = new SessionProvider();
            provider.setRestClient(client);
            provider.setProviderListener(providerListener);

            return provider.authenticateWithAppCredentials(clientId, clientSecret, appId, appToken);
        }

        /**
         * Revokes a previously created Podio session. Even though the access
         * token may have expired, the refresh token can be used to get a new
         * access token. The idea here is to enable the caller to persist the
         * session and avoid an unnecessary re-authentication.
         * 
         * NOTE! The server may very well invalidate both the access and refresh
         * tokens, which would require a re-authentication anyway.
         * 
         * @param session
         *            The previously stored session object.
         */
        public static final void revokeSession(Session session) {
            client.revokeSession(SessionFilter.PATH, session);
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

    private Podio() {
        // Hiding the constructor of this class as it's not meant to be
        // instantiated.
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
