package com.podio.sdk;

import android.content.Context;

import com.podio.sdk.client.CachedRestClient;
import com.podio.sdk.client.delegate.HttpClientDelegate;
import com.podio.sdk.client.delegate.ItemParser;
import com.podio.sdk.client.delegate.SQLiteClientDelegate;
import com.podio.sdk.domain.ApplicationProvider;
import com.podio.sdk.domain.OrganizationProvider;
import com.podio.sdk.domain.Session;
import com.podio.sdk.domain.SessionFilter;
import com.podio.sdk.domain.SessionProvider;

/**
 * Enables easy access to the Podio API with a basic configuration which should
 * be suitable for most third party developers.
 * 
 * @author László Urszuly
 * 
 */
public final class Podio {

    /**
     * Enables means of easy operating on the Application API end point.
     * 
     * @author László Urszuly
     * 
     */
    public static final class Application {

        private Application() {
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
        public static final Object getForSpace(long spaceId, ProviderListener providerListener) {
            ItemParser<com.podio.sdk.domain.Application[]> parser = new ItemParser<com.podio.sdk.domain.Application[]>(
                    com.podio.sdk.domain.Application[].class);

            cacheDelegate.setItemParser(parser);
            networkDelegate.setItemParser(parser);

            ApplicationProvider provider = new ApplicationProvider();
            provider.setRestClient(client);
            provider.setProviderListener(providerListener);

            return provider.fetchApplicationsForSpace(spaceId);
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
        public static final Object getForSpaceIncludingInactive(long spaceId,
                ProviderListener providerListener) {

            ItemParser<com.podio.sdk.domain.Application[]> parser = new ItemParser<com.podio.sdk.domain.Application[]>(
                    com.podio.sdk.domain.Application[].class);

            cacheDelegate.setItemParser(parser);
            networkDelegate.setItemParser(parser);

            ApplicationProvider provider = new ApplicationProvider();
            provider.setRestClient(client);
            provider.setProviderListener(providerListener);

            return provider.fetchApplicationsForSpaceWithInactivesIncluded(spaceId);
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

            ItemParser<com.podio.sdk.domain.Session> parser = new ItemParser<com.podio.sdk.domain.Session>(
                    com.podio.sdk.domain.Session.class);

            cacheDelegate.setItemParser(parser);
            networkDelegate.setItemParser(parser);

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

            ItemParser<com.podio.sdk.domain.Session> parser = new ItemParser<com.podio.sdk.domain.Session>(
                    com.podio.sdk.domain.Session.class);

            cacheDelegate.setItemParser(parser);
            networkDelegate.setItemParser(parser);

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
            cacheDelegate.setItemParser(null);
            networkDelegate.setItemParser(null);

            client.revokeSession(SessionFilter.PATH, session);
        }

    }

    /**
     * Enables means of easy operating on the Organization API end point.
     * 
     * @author László Urszuly
     * 
     */
    public static final class Organization {

        private Organization() {
            // Hiding the constructor of this class as it's not meant to be
            // instantiated.
        }

        /**
         * Fetches all Organizations (with a minimal set of information on the
         * contained workspaces as well) that are available to the user.
         * 
         * @param providerListener
         *            The callback implementation called when the items are
         *            fetched. Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object getAll(ProviderListener providerListener) {
            ItemParser<com.podio.sdk.domain.Organization[]> parser = new ItemParser<com.podio.sdk.domain.Organization[]>(
                    com.podio.sdk.domain.Organization[].class);

            cacheDelegate.setItemParser(parser);
            networkDelegate.setItemParser(parser);

            OrganizationProvider provider = new OrganizationProvider();
            provider.setRestClient(client);
            provider.setProviderListener(providerListener);

            return provider.getAll();
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

    /**
     * Initializes the Podio SDK with the given client credentials. This method
     * MUST be called before any other request is made. The caller can then
     * either choose to revoke a previously stored session (the SDK doesn't
     * store or cache the session), or authenticate with user or app
     * credentials. These operations are done in the {@link Client} area.
     * 
     * @param context
     *            The context to initialize the cache database and network
     *            clients in.
     * @param clientId
     *            The pre-shared Podio client id.
     * @param clientSecret
     *            The corresponding Podio client secret.
     */
    public static void setup(Context context, String clientId, String clientSecret) {
        Podio.clientId = clientId;
        Podio.clientSecret = clientSecret;
        Podio.networkDelegate = new HttpClientDelegate(context);
        Podio.cacheDelegate = new SQLiteClientDelegate(context, DATABASE_NAME, DATABASE_VERSION);
        Podio.client = new CachedRestClient(context, AUTHORITY, networkDelegate, cacheDelegate,
                QUEUE_CAPACITY);
    }
}
