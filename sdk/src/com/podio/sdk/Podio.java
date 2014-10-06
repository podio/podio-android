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

import javax.net.ssl.SSLSocketFactory;

import android.content.Context;

import com.podio.sdk.PodioRequest.ErrorListener;
import com.podio.sdk.PodioRequest.SessionListener;
import com.podio.sdk.provider.ApplicationProvider;
import com.podio.sdk.provider.CalendarProvider;
import com.podio.sdk.provider.ItemProvider;
import com.podio.sdk.provider.OrganizationProvider;
import com.podio.sdk.provider.UserProvider;
import com.podio.sdk.volley.VolleyClient;
import com.podio.sdk.volley.VolleyRequest;

/**
 * Enables easy access to the Podio API with a basic configuration which should
 * be suitable for most third party developers.
 * 
 * @author László Urszuly
 */
public class Podio {

    /**
     * A custom provider implementation to expose the authentication features of
     * the current {@link Client} implementation.
     * 
     * @author László Urszuly
     */
    public static class ClientProvider {
        private Client client;

        /**
         * Enables injection of the <code>Client</code> implementation, but only
         * from the parent class.
         * 
         * @param client
         *        The <code>Client</code> to use when executing authentication
         *        requests.
         */
        private void setClient(Client client) {
            this.client = client;
        }

        /**
         * Enables means of user authentication, where the caller authenticates
         * with a user name and a password.
         * 
         * @param username
         *        The user name to log in with.
         * @param password
         *        The corresponding password.
         * @return A request future, enabling the caller to hook in optional
         *         callback implementations.
         */
        public PodioRequest<Void> authenticateWithUserCredentials(String username, String password) {
            return client.authenticateWithUserCredentials(username, password);
        }

        /**
         * Enables means of user authentication, where the caller authenticates
         * with a Podio app id and app token.
         * 
         * @param appId
         *        The app id to log in with.
         * @param appToken
         *        The corresponding app token.
         * @return A request future, enabling the caller to hook in optional
         *         callback implementations.
         */
        public PodioRequest<Void> authenticateWithAppCredentials(String appId, String appToken) {
            return client.authenticateWithAppCredentials(appId, appToken);
        }

    }

    private static final String SCHEME = "https";
    private static final String AUTHORITY = "api.podio.com";
    private static final String VERSION_NAME = "0.0.1";
    private static final int VERSION_CODE = 1;

    protected static VolleyClient restClient = new VolleyClient();

    /**
     * Enables means of easy operating on the {@link ApplicationProvider} API
     * end point.
     */
    public static final ApplicationProvider application = new ApplicationProvider();

    /**
     * Enables means of easy operating on the {@link CalendarProvider} API end
     * point.
     */
    public static final CalendarProvider calendar = new CalendarProvider();

    /**
     * Enables means of easy authentication.
     */
    public static final ClientProvider client = new ClientProvider();

    /**
     * Enables means of easy operating on the {@link ItemProvider} API end
     * point.
     */
    public static final ItemProvider item = new ItemProvider();

    /**
     * Enables means of easy operating on the {@link OrganizationProvider} API
     * end point.
     */
    public static final OrganizationProvider organization = new OrganizationProvider();

    /**
     * Enables means of easy operating on the {@link UserProvider} API end
     * point.
     */
    public static final UserProvider user = new UserProvider();

    /**
     * Enables means of registering global error listeners. These callback
     * implementations apply to <em>all</em> requests until explicitly removed
     * and they are called <em>after</em> any custom callbacks added to a
     * particular request future are called.
     * <p>
     * If a callback chooses to consume a given event, then <em>all</em> further
     * bubbling is aborted, meaning that the event may not reach the global
     * event listener you add here.
     * 
     * @param errorListener
     *        The global listener to register.
     * @return The registered listener on success, null otherwise.
     */
    public static ErrorListener addGlobalErrorListener(ErrorListener errorListener) {
        return VolleyRequest.addGlobalErrorListener(errorListener);
    }

    /**
     * Registers a global session listeners. These callback implementations
     * apply to <em>all</em> requests until explicitly removed and they are
     * called <em>after</em> any custom callbacks added to a particular request
     * future are called.
     * <p>
     * If a callback chooses to consume a given event, then <em>all</em> further
     * bubbling is aborted, meaning that the event may not reach the global
     * event listener you add here.
     * 
     * @param sessionListener
     *        The global listener to register.
     * @return The registered listener on success, null otherwise.
     */
    public static SessionListener addGlobalSessionListener(SessionListener sessionListener) {
        return VolleyRequest.addGlobalSessionListener(sessionListener);
    }

    /**
     * Unregisters a previously registered global error listener.
     * 
     * @param errorListener
     *        The listener to unregister.
     * @return The listener that has just been unregistered, or null if the
     *         listener couldn't be found.
     */
    public static ErrorListener removeGlobalErrorListener(ErrorListener errorListener) {
        return VolleyRequest.removeGlobalErrorListener(errorListener);
    }

    /**
     * Unregisters a previously registered global session listener.
     * 
     * @param sessionListener
     *        The listener to unregister.
     * @return The listener that has just been unregistered, or null if the
     *         listener couldn't be found.
     */
    public static SessionListener removeGlobalSessionListener(SessionListener sessionListener) {
        return VolleyRequest.removeGlobalSessionListener(sessionListener);
    }

    /**
     * The same as {@link Podio#setup(Context, String, String, String)} but with
     * the default scheme and authority.
     * 
     * @param context
     *        The context to initialize the cache database and network clients
     *        in.
     * @param clientId
     *        The pre-shared Podio client id.
     * @param clientSecret
     *        The corresponding Podio client secret.
     * @see Podio#setup(Context, String, String, String)
     */
    public static void setup(Context context, String clientId, String clientSecret) {
        setup(context, SCHEME, AUTHORITY, clientId, clientSecret, null);
    }

    /**
     * Initializes the Podio SDK with the given client credentials. This method
     * MUST be called before any other request is made.
     * 
     * @param context
     *        The context to initialize the cache database and network clients
     *        in.
     * @param authority
     *        The host the SDK will target with its requests.
     * @param clientId
     *        The pre-shared Podio client id.
     * @param clientSecret
     *        The corresponding Podio client secret.
     * @param sslSocketFactory
     *        Optional custom SSL socket factory to use in the HTTP requests.
     */
    public static void setup(Context context, String scheme, String authority, String clientId, String clientSecret, SSLSocketFactory sslSocketFactory) {
        restClient.setup(context, scheme, authority, clientId, clientSecret, sslSocketFactory);

        application.setClient(restClient);
        calendar.setClient(restClient);
        client.setClient(restClient);
        item.setClient(restClient);
        organization.setClient(restClient);
        user.setClient(restClient);
    }

    /**
     * Restores a previously created Podio session. Even though the access token
     * may have expired, the refresh token can be used to get a new access
     * token. The idea here is to enable the caller to persist the session and
     * avoid an unnecessary re-authentication. NOTE! The server may very well
     * invalidate both the access and refresh tokens, which would require a
     * re-authentication anyway.
     * 
     * @param session
     *        The previously stored session object.
     */
    public static void restoreSession(String accessToken, String refreshToken, long expires) {
        Session.set(accessToken, refreshToken, expires);
    }

    /**
     * Returns the version name of this Podio SDK instance.
     * 
     * @return A string representation of the Podio SDK version.
     */
    public static String sdkVersionName() {
        return VERSION_NAME;
    }

    /**
     * Returns the version code of this Podio SDK instance.
     * 
     * @return An integer representation of the Podio SDK version.
     */
    public static int sdkVersionCode() {
        return VERSION_CODE;
    }
}
