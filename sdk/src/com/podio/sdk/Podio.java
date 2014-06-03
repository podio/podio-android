/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */

package com.podio.sdk;

import java.util.Date;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.client.CachedRestClient;
import com.podio.sdk.client.HttpRestClient;
import com.podio.sdk.client.cache.CacheClient;
import com.podio.sdk.client.cache.SQLiteCacheClient;
import com.podio.sdk.client.delegate.HttpClientDelegate;
import com.podio.sdk.domain.Application;
import com.podio.sdk.domain.CalendarEvent;
import com.podio.sdk.domain.Item;
import com.podio.sdk.domain.ItemRequest;
import com.podio.sdk.domain.Organization;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.SessionFilter;
import com.podio.sdk.internal.request.ResultListener;
import com.podio.sdk.provider.ApplicationProvider;
import com.podio.sdk.provider.CalendarProvider;
import com.podio.sdk.provider.ItemProvider;
import com.podio.sdk.provider.OrganizationProvider;
import com.podio.sdk.provider.SessionProvider;

/**
 * Enables easy access to the Podio API with a basic configuration which should
 * be suitable for most third party developers.
 * 
 * @author László Urszuly
 */
public final class Podio {

    /**
     * Enables means of easy operating on the Application API end point.
     * 
     * @author László Urszuly
     */
    public static final class ApplicationAPI {

        private ApplicationAPI() {
            // Hiding the constructor of this class as it's not meant to be
            // instantiated.
        }

        /**
         * Fetches the full content set of the Podio Application with the given
         * id.
         * 
         * @param applicationId
         *        The id of the application to fetch.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object get(long applicationId, ResultListener<? super Application> resultListener) {
            ApplicationProvider provider = new ApplicationProvider(client);

            return provider.fetchApplication(applicationId, resultListener);
        }

        /**
         * Fetches all App items in the workspace with the given id.
         * 
         * @param spaceId
         *        The id of the parent workspace.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object getForSpace(long spaceId, ResultListener<? super Application[]> resultListener) {
            ApplicationProvider provider = new ApplicationProvider(client);

            return provider.fetchApplicationsForSpace(spaceId, resultListener);
        }

        /**
         * Fetches all App items, including the inactive ones, in the workspace
         * with the given id.
         * 
         * @param spaceId
         *        The id of the parent workspace.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object getForSpaceIncludingInactive(long spaceId,
                ResultListener<? super Application[]> resultListener) {

            ApplicationProvider provider = new ApplicationProvider(client);

            return provider.fetchApplicationsForSpaceWithInactivesIncluded(spaceId, resultListener);
        }

        /**
         * Fetches a micro content subset of the Podio Application with the
         * given id.
         * 
         * @param applicationId
         *        The id of the application to fetch.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object getMicro(long applicationId,
                ResultListener<? super Application> resultListener) {

            ApplicationProvider provider = new ApplicationProvider(client);

            return provider.fetchApplicationMicro(applicationId, resultListener);
        }

        /**
         * Fetches a mini content subset of the Podio Application with the given
         * id.
         * 
         * @param applicationId
         *        The id of the application to fetch.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object getMini(long applicationId,
                ResultListener<? super Application> resultListener) {

            ApplicationProvider provider = new ApplicationProvider(client);

            return provider.fetchApplicationMini(applicationId, resultListener);
        }

        /**
         * Fetches a short content subset of the Podio Application with the
         * given id.
         * 
         * @param applicationId
         *        The id of the application to fetch.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object getShort(long applicationId,
                ResultListener<? super Application> resultListener) {

            ApplicationProvider provider = new ApplicationProvider(client);

            return provider.fetchApplicationShort(applicationId, resultListener);
        }
    }

    /**
     * Enables means of authentication and basic session management with the
     * Podio servers.
     * 
     * @author László Urszuly
     */
    public static final class ClientAPI {

        private ClientAPI() {
            // Hiding the constructor of this class as it's not meant to be
            // instantiated.
        }

        /**
         * Authenticates the caller with the given user credentials. On success
         * a new session object with the access and refresh tokens will be
         * delivered through the given {@link ResultListener}.
         * 
         * @param username
         *        The user name of the Podio account to authenticate with.
         * @param password
         *        The corresponding password of the Podio account.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object authenticateAsUser(String username, String password,
                ResultListener<? super Session> resultListener) {

            SessionProvider provider = new SessionProvider(client);

            return provider.authenticateWithUserCredentials(clientId, clientSecret, username,
                    password, resultListener);
        }

        /**
         * Authenticates the caller with the given app credentials. On success a
         * new session object with the access and refresh tokens will be
         * delivered through the given {@link ResultListener}.
         * 
         * @param appId
         *        The id of the app to authenticate with.
         * @param appToken
         *        The token that has been generated for a particular app.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object authenticateAsApp(String appId, String appToken,
                ResultListener<? super Session> resultListener) {

            SessionProvider provider = new SessionProvider(client);

            return provider.authenticateWithAppCredentials(clientId, clientSecret, appId, appToken, resultListener);
        }

        /**
         * Revokes a previously created Podio session. Even though the access
         * token may have expired, the refresh token can be used to get a new
         * access token. The idea here is to enable the caller to persist the
         * session and avoid an unnecessary re-authentication. NOTE! The server
         * may very well invalidate both the access and refresh tokens, which
         * would require a re-authentication anyway.
         * 
         * @param session
         *        The previously stored session object.
         */
        public static final void restoreSession(Session session) {
            Uri sessionRefreshUri = new Uri.Builder()
                    .scheme(client.getScheme())
                    .authority(client.getAuthority())
                    .appendEncodedPath(SessionFilter.PATH)
                    .build();
            String url = sessionRefreshUri.toString();
            networkDelegate.restoreSession(url, session);
        }

    }

    /**
     * Enables means of easy operating on the Item API end point.
     * 
     * @author László Urszuly
     */
    public static final class ItemAPI {

        private ItemAPI() {
            // Hiding the constructor of this class as it's not meant to be
            // instantiated.
        }

        /**
         * Sends a new item to the Podio servers.
         * 
         * @param applicationId
         *        The id of the application to which this item is to be added.
         * @param data
         *        The definition of the item content to push.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object add(long applicationId, Item.PushData data,
        		ResultListener<? super Item.PushResult> resultListener) {

            ItemProvider provider = new ItemProvider(client);

            return provider.addItem(applicationId, data, resultListener);
        }

        /**
         * Fetches a single item with the given id.
         * 
         * @param itemId
         *        The id of the item to fetch.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object get(long itemId, ResultListener<? super Item> resultListener) {
            ItemProvider provider = new ItemProvider(client);

            return provider.fetchItem(itemId, resultListener);
        }

        /**
         * Fetches a default set of filtered items for the application with the
         * given id.
         * 
         * @param applicationId
         *        The id of the parent application.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object getForApplication(long applicationId,
        		ResultListener<? super ItemRequest.Result> resultListener) {

            ItemProvider provider = new ItemProvider(client);

            return provider.fetchItemsForApplication(applicationId, resultListener);
        }

        /**
         * Sends the new values for the fields of an item to the API.
         * 
         * @param itemId
         *        The id of the item to update.
         * @param data
         *        The changed data bundle.
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object update(long itemId, Item.PushData data,
        		ResultListener<? super Item.PushResult> resultListener) {

            ItemProvider provider = new ItemProvider(client);

            return provider.updateItem(itemId, data, resultListener);

        }
    }

    /**
     * Enables means of easy operating on the Organization API end point.
     * 
     * @author László Urszuly
     */
    public static final class OrganizationAPI {

        private OrganizationAPI() {
            // Hiding the constructor of this class as it's not meant to be
            // instantiated.
        }

        /**
         * Fetches all Organizations (including a minimal set of information on
         * the contained workspaces) that are available to the user.
         * 
         * @param resultListener
         *        The callback implementation called when the items are fetched.
         *        Null is valid, but doesn't make any sense.
         * @return A ticket which the caller can use to identify this request
         *         with.
         */
        public static final Object getAll(ResultListener<? super Organization[]> resultListener) {
            OrganizationProvider provider = new OrganizationProvider(client);

            return provider.getAll(resultListener);
        }
    }

    /**
     * Enables means of easy operating on the Calendar API end point.
     * 
     * @author Tobias Lindberg
     */
    public static final class CalendarAPI {

        private CalendarAPI() {
            // Hiding the constructor of this class as it's not meant to be
            // instantiated.
        }

        /**
         * Fetches all global Calendar events.
         * 
         * @param from
         *        The Date from which the result should start from.
         * @param to
         *        The Date from which the result should end at.
         * @param priority
         *        The priority level of the results.
         * @param resultListener
         *        The callback implementation called when the calendar events
         *        are fetched. Null is valid, but doesn't make any sense.
         * @return
         */
        public static final Object getGlobalCalendar(Date from, Date to, int priority,
                ResultListener<? super CalendarEvent[]> resultListener) {
            CalendarProvider provider = new CalendarProvider(client);

            return provider.fetchGlobalCalendar(from, to, priority, resultListener);
        }
    }

    /**
     * Describes the behavior of the {@link RestClient} implementation used by
     * the SDK. The {@link RestBehavior#HTTP_ONLY} option doesn't cache any
     * content locally, but hits the API for each call. The
     * {@link RestBehavior#CACHED_HTTP} option on the other hand, tries to cache
     * some content. It also returns the cached content first when requested and
     * then hits the API. The caller will therefore sometimes get two callback
     * calls; once for the cache request and once for the API request.
     */
    public static enum RestBehavior {
        HTTP_ONLY, CACHED_HTTP
    }

    private static final String AUTHORITY = "api.podio.com";
    private static final String DATABASE_NAME = "podio.db";
    private static final int DATABASE_VERSION = 1;
    private static final int QUEUE_CAPACITY = 10;

    private static SQLiteClientDelegate cacheDelegate;
    private static HttpClientDelegate networkDelegate;

    private static RestClient client;
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
     * credentials. These operations are done in the {@link ClientAPI} area.
     * 
     * @param context
     *        The context to initialize the cache database and network clients
     *        in.
     * @param clientId
     *        The pre-shared Podio client id.
     * @param clientSecret
     *        The corresponding Podio client secret.
     */
    public static void setup(Context context, String clientId, String clientSecret) {
        Podio.setup(context, clientId, clientSecret, RestBehavior.HTTP_ONLY);
    }

    /**
     * Initializes the Podio SDK with the given client credentials. This method
     * MUST be called before any other request is made. The caller can then
     * either choose to revoke a previously stored session (the SDK doesn't
     * store or cache the session), or authenticate with user or app
     * credentials. These operations are done in the {@link ClientAPI} area.
     * 
     * @param context
     *        The context to initialize the cache database and network clients
     *        in.
     * @param clientId
     *        The pre-shared Podio client id.
     * @param clientSecret
     *        The corresponding Podio client secret.
     * @param behavior
     *        The behavior to expect from the {@link RestClient} implementation.
     */
    public static void setup(Context context, String clientId, String clientSecret, RestBehavior behavior) {
        Podio.clientId = clientId;
        Podio.clientSecret = clientSecret;
        RestClientDelegate networkDelegate = new HttpClientDelegate(context);

		switch (behavior) {
        case HTTP_ONLY:
            Podio.client = new HttpRestClient(context, AUTHORITY, networkDelegate, QUEUE_CAPACITY);
            break;
        case CACHED_HTTP:
        	CacheClient cacheClient = new SQLiteCacheClient(context, DATABASE_NAME, DATABASE_VERSION);
            Podio.client = new CachedRestClient(context, AUTHORITY, networkDelegate, cacheClient, QUEUE_CAPACITY);
            break;
        default:
            Podio.client = new HttpRestClient(context, AUTHORITY, networkDelegate, QUEUE_CAPACITY);
            break;
        }
    }
}
