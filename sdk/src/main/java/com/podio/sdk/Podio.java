
package com.podio.sdk;

import android.content.Context;

import com.podio.sdk.Request.ErrorListener;
import com.podio.sdk.Request.SessionListener;
import com.podio.sdk.androidasynchttp.AndroidAsyncHttpClient;
import com.podio.sdk.provider.ApplicationProvider;
import com.podio.sdk.provider.CalendarProvider;
import com.podio.sdk.provider.ClientProvider;
import com.podio.sdk.provider.CommentProvider;
import com.podio.sdk.provider.ContactProvider;
import com.podio.sdk.provider.ConversationProvider;
import com.podio.sdk.provider.EmbedProvider;
import com.podio.sdk.provider.FileProvider;
import com.podio.sdk.provider.GrantProvider;
import com.podio.sdk.provider.ItemProvider;
import com.podio.sdk.provider.LinkedAccountProvider;
import com.podio.sdk.provider.LocationProvider;
import com.podio.sdk.provider.NotificationProvider;
import com.podio.sdk.provider.OrganizationProvider;
import com.podio.sdk.provider.RatingProvider;
import com.podio.sdk.provider.RecurrenceProvider;
import com.podio.sdk.provider.ReferenceProvider;
import com.podio.sdk.provider.ReminderProvider;
import com.podio.sdk.provider.SpacesProvider;
import com.podio.sdk.provider.StatusProvider;
import com.podio.sdk.provider.StreamProvider;
import com.podio.sdk.provider.TaskProvider;
import com.podio.sdk.provider.UserProvider;
import com.podio.sdk.provider.ViewProvider;
import com.podio.sdk.volley.VolleyClient;
import com.podio.sdk.volley.VolleyRequest;

import javax.net.ssl.SSLSocketFactory;

/**
 * Enables easy access to the Podio API with a basic configuration which should be suitable for most
 * third party developers.
 */
public class Podio {
    /**
     * The default request client for the providers.
     */
    protected static VolleyClient volleytRestClient = new VolleyClient();

    protected static AndroidAsyncHttpClient androidAsyncHttpRestClient = new AndroidAsyncHttpClient();

    /**
     * Enables means of easy operating on the Application API end point.
     */
    public static final ApplicationProvider application = new ApplicationProvider();

    /**
     * Enables means of easy operating on the Calendar API end point.
     */
    public static final CalendarProvider calendar = new CalendarProvider();

    /**
     * Enables means of easy operating on the {@link ContactProvider} API end point.
     */
    public static final ContactProvider contact = new ContactProvider();

    /**
     * Enables means of easy operating on the Conversation API end point.
     */
    public static final ConversationProvider conversation = new ConversationProvider();

    /**
     * Enables means of easy authentication.
     */
    public static final ClientProvider client = new ClientProvider();

    /**
     * Enables means of easy operating on the File API end point.
     */
    public static final FileProvider file = new FileProvider();

    /**
     * Enables means of easy operating on the Item API end point.
     */
    public static final ItemProvider item = new ItemProvider();

    /**
     * Enables means of easy operating on the Organization API end point.
     */
    public static final OrganizationProvider organization = new OrganizationProvider();

    /**
     * Enables means of easy operating on the User API end point.
     */
    public static final UserProvider user = new UserProvider();

    /**
     * Enables means of easy operating on the View API end point.
     */
    public static final ViewProvider view = new ViewProvider();

    /**
     * Enables means of easy operating on the Task API end point.
     */
    public static final TaskProvider task = new TaskProvider();

    /**
     * Enables means of easy operating on the Notification API end point.
     */
    public static final NotificationProvider notification = new NotificationProvider();

    /**
     * Enables means of easy operating on the Stream API end point.
     */
    public static final StreamProvider stream = new StreamProvider();

    /**
     * Enables means of easy operating on the Comment API end point.
     */
    public static final CommentProvider comment = new CommentProvider();

    /**
     * Enables means of easy operating on the Rating API end point.
     */
    public static final RatingProvider rating = new RatingProvider();

    /**
     * Enables means of easy operating on the Grant API end point.
     */
    public static final GrantProvider grant = new GrantProvider();

    /**
     * Enables means of easy operating on the Location API end point.
     */
    public static final LocationProvider location = new LocationProvider();

    /**
     * Enables means of easy operating on the Embed API end point.
     */
    public static final EmbedProvider embed = new EmbedProvider();

    /**
     * Enables means of easy operating on the Reference API end point.
     */
    public static final ReferenceProvider reference = new ReferenceProvider();

    /**
     * Enables means of easy operating on the Status API end point.
     */
    public static final StatusProvider status = new StatusProvider();

    /**
     * Enables means of easy operating on the Spaces API end point.
     */
    public static final SpacesProvider spaces = new SpacesProvider();

    public static final ReminderProvider reminder = new ReminderProvider();

    public static final RecurrenceProvider recurrence = new RecurrenceProvider();

    /**
     * Enables means of easy operating on the Linked Account API end point.
     */
    public static final LinkedAccountProvider linkedAccount = new LinkedAccountProvider();

    /**
     * Enables means of registering global error listeners. These callback implementations apply to
     * <em>all</em> requests until explicitly removed and they are called <em>after</em> any custom
     * callbacks added to a particular request future are called.
     * <p/>
     * If a callback chooses to consume a given event, then <em>all</em> further bubbling is
     * aborted, meaning that the event may not reach the global event listener you add here.
     *
     * @param errorListener
     *         The global listener to register.
     *
     * @return The registered listener on success, null otherwise.
     */
    public static ErrorListener addGlobalErrorListener(ErrorListener errorListener) {
        return VolleyRequest.addGlobalErrorListener(errorListener);
    }

    /**
     * Registers a global session listeners. These callback implementations apply to <em>all</em>
     * requests until explicitly removed and they are called <em>after</em> any custom callbacks
     * added to a particular request future are called.
     * <p/>
     * If a callback chooses to consume a given event, then <em>all</em> further bubbling is
     * aborted, meaning that the event may not reach the global event listener you add here.
     *
     * @param sessionListener
     *         The global listener to register.
     *
     * @return The registered listener on success, null otherwise.
     */
    public static SessionListener addGlobalSessionListener(SessionListener sessionListener) {
        return VolleyRequest.addGlobalSessionListener(sessionListener);
    }

    /**
     * Unregisters a previously registered global error listener.
     *
     * @param errorListener
     *         The listener to unregister.
     *
     * @return The listener that has just been unregistered, or null if the listener couldn't be
     * found.
     */
    public static ErrorListener removeGlobalErrorListener(ErrorListener errorListener) {
        return VolleyRequest.removeGlobalErrorListener(errorListener);
    }

    /**
     * Unregisters a previously registered global session listener.
     *
     * @param sessionListener
     *         The listener to unregister.
     *
     * @return The listener that has just been unregistered, or null if the listener couldn't be
     * found.
     */
    public static SessionListener removeGlobalSessionListener(SessionListener sessionListener) {
        return VolleyRequest.removeGlobalSessionListener(sessionListener);
    }

    /**
     * Initializes the Podio facade to it's default initial state.
     *
     * @param context
     *         The context to initialize the cache database and network clients in.
     * @param clientId
     *         The pre-shared Podio client id.
     * @param clientSecret
     *         The corresponding Podio client secret.
     *
     * @see Podio#setup(Context, String, String, String, String, String, SSLSocketFactory,
     * cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory)
     */
    public static void setup(Context context, String clientId, String clientSecret) {
        setup(context, BuildConfig.SCHEME, BuildConfig.API_AUTHORITY, clientId, clientSecret, null, null, null);
    }

    /**
     * Initializes the Podio SDK with the given client credentials. This method MUST be called
     * before any other request is made.
     *
     * @param context
     *         The context to initialize the cache database and network clients in.
     * @param authority
     *         The host the SDK will target with its requests.
     * @param clientId
     *         The pre-shared Podio client id.
     * @param clientSecret
     *         The corresponding Podio client secret.
     * @param userAgent
     *         Optional user agent.
     * @param volleySslSocketFactory
     *         Optional custom SSL socket factory to use in the HTTP requests.
     * @param androidAsyncHttpSslSocketFactory
     *         Optional custom SSL sockey factory to use for uploading files.
     */
    public static void setup(Context context, String scheme, String authority, String clientId, String clientSecret, String userAgent, SSLSocketFactory volleySslSocketFactory, cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory androidAsyncHttpSslSocketFactory) {
        volleytRestClient.setup(context, scheme, authority, clientId, clientSecret, userAgent, volleySslSocketFactory);
        androidAsyncHttpRestClient.setup(context, scheme, authority, userAgent, androidAsyncHttpSslSocketFactory);
        // Providers relying on a rest client in order to operate properly.
        application.setClient(volleytRestClient);
        calendar.setClient(volleytRestClient);
        client.setClient(volleytRestClient);
        contact.setClient(volleytRestClient);
        conversation.setClient(volleytRestClient);
        file.setClient(androidAsyncHttpRestClient);
        item.setClient(volleytRestClient);
        organization.setClient(volleytRestClient);
        user.setClient(volleytRestClient);
        view.setClient(volleytRestClient);
        task.setClient(volleytRestClient);
        notification.setClient(volleytRestClient);
        stream.setClient(volleytRestClient);
        comment.setClient(volleytRestClient);
        rating.setClient(volleytRestClient);
        grant.setClient(volleytRestClient);
        location.setClient(volleytRestClient);
        embed.setClient(volleytRestClient);
        reference.setClient(volleytRestClient);
        status.setClient(volleytRestClient);
        linkedAccount.setClient(volleytRestClient);
        reminder.setClient(volleytRestClient);
        recurrence.setClient(volleytRestClient);
    }
}
