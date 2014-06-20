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

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.client.CachedRestClient;
import com.podio.sdk.client.HttpRestClient;
import com.podio.sdk.client.cache.CacheClient;
import com.podio.sdk.client.cache.SQLiteCacheClient;
import com.podio.sdk.client.delegate.HttpClientDelegate;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.SessionFilter;
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

    private static HttpClientDelegate networkDelegate;
    private static RestClient restClient;

    /**
     * Enables means of easy operating on the {@link ApplicationProvider} API
     * end point.
     */
    public static final ApplicationProvider application = new ApplicationProvider(restClient);

    /**
     * Enables means of easy operating on the {@link CalendarProvider} API end
     * point.
     */
    public static final CalendarProvider calendar = new CalendarProvider(restClient);

    /**
     * Enables means of easy operating on the {@link ItemProvider} API end
     * point.
     */
    public static final ItemProvider item = new ItemProvider(restClient);

    /**
     * Enables means of easy operating on the {@link OrganizationProvider} API
     * end point.
     */
    public static final OrganizationProvider organization = new OrganizationProvider(restClient);

    /**
     * Enables means of easy operating on the {@link SessionProvider} API end
     * point.
     */
    public static final SessionProvider client = new SessionProvider(restClient);

    /**
     * Hidden constructor.
     */
    private Podio() {
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
        RestClientDelegate networkDelegate = new HttpClientDelegate(context);
        client.setup(clientId, clientSecret);

        switch (behavior) {
        case HTTP_ONLY:
            Podio.restClient = new HttpRestClient(context, AUTHORITY, networkDelegate);
            break;
        case CACHED_HTTP:
            CacheClient cacheClient = new SQLiteCacheClient(context, DATABASE_NAME, DATABASE_VERSION);
            Podio.restClient = new CachedRestClient(context, AUTHORITY, networkDelegate, cacheClient);
            break;
        default:
            Podio.restClient = new HttpRestClient(context, AUTHORITY, networkDelegate);
            break;
        }
    }

    /**
     * Revokes a previously created Podio session. Even though the access token
     * may have expired, the refresh token can be used to get a new access
     * token. The idea here is to enable the caller to persist the session and
     * avoid an unnecessary re-authentication. NOTE! The server may very well
     * invalidate both the access and refresh tokens, which would require a
     * re-authentication anyway.
     * 
     * @param session
     *        The previously stored session object.
     */
    public static final void restoreSession(Session session) {
        Uri sessionRefreshUri = new Uri.Builder()
                .scheme(restClient.getScheme())
                .authority(restClient.getAuthority())
                .appendEncodedPath(SessionFilter.PATH)
                .build();
        String url = sessionRefreshUri.toString();
        networkDelegate.restoreSession(url, session);
    }

}
