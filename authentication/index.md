---
layout: default
---
# Authentication & Session Management
The Podio API supports multiple ways of authenticating a client. The Podio SDK for Android provides two primary options:

* Authenticate with username/password.
* Authenticate with specific app tokens.

Both options are described below.

Any changes to the authentication state during a call to the Podio API is reported back to the caller through the `SessionListener.onSessionChanged(Sessoin session)` callback method. It can be useful to listen to this callback if you wish to persist your authentication tokens in any way you'd find convenient. You can then later on restore a previously stored authentication session by calling:

{% highlight java %}
Podio.restoreSession(myPersistedSession);
{% endhighlight %}

You can find more information on authentication and the Podio API [here](https://developers.podio.com/authentication).

## Authenticate as a user
This option is great when you want to have every user of your client app logging in using their own Podio account and thereby give them access to the content of their entire Podio account.

This is how you authenticate as a user:

{% highlight java %}
String username = userTextView.getText().toString();
String password = passTextView.getText().toString();

Podio.client
        .authenticateWithUserCredentials(username, password)
        .withResultListener(new ResultListener<Session>() {

            @Override
            public void onRequestPerformed(Session session) {
                // Yeay!
            }

        });
{% endhighlight %}

## Authenticate as an app
This option is useful when you want all users of your client app to interact with the same Podio app, regardless of who they are or who created the Podio app. 

This method doesn't require any username and password but an app id and app token instead. You find these credentials by logging in to Podio and browsing to the app in question. You should then click the wrench icon to the top right of the screen and then pick the "Developer" option in the drop down menu. You should then be taken to a page showing your app's ID and token.

This is how you authenticate as an app:

{% highlight java %}
public static final String MY_APP_ID = "my-app-id";
public static final String MY_APP_TOKEN = "my-app-token";

Podio.client
        .authenticateWithAppCredentials(MY_APP_ID, MY_APP_TOKEN)
        .withResultListener(new ResultListener<Session>() {

            @Override
            public void onRequestPerformed(Session session) {
                // Yeay!
            }

        });
{% endhighlight %}

## Session management
The SDK is, in some sense, quite smart as it automatically tries to refresh an expired user session for you (if it fails it will deliver an error through any of the previously mentioned error infrastructures). If you wish to get notified on these automatic and silent session changes, you need to provide a `SessionListener` callback implementation. This is done the same way, regardless of which, synchronous or asynchronous, approach you chose.

Just as with all other Podio SDK callbacks, you can inject your `SessionListener` like in the below example where we're trying to fetch a Podio app:

{% highlight java %}
Podio.application
    .get(123)
    .withResultListener(new ResultListener<Application>() {

            @Override
            public void onRequestPerformed(Session session) {
                // Yeay!
            }

    })
    .withSessionListener(new SessionListener() {

        @Override
        public boolean onSessionChanged(Session session) {
            // Persist the session data.
            // SharedPreferences? SQLite? Your choice!
            return false;
        }

    });
{% endhighlight %}

There is a narrow but, from a usability perspective, still very important use case for this:

The Podio SDK can be initialized with a previously stored `Session` object. This basically allows you to "continue where you left of" in terms of session validity, without requiring your user to re-authenticate that frequently.

You don't necessarily need this feature if you chose to [authenticate as an app](https://developers.podio.com/authentication/app_auth), as you can silently re-authenticate with your app credentials in the background. However if your Android app requires [user authentication](https://developers.podio.com/authentication/username_password), you can't do that silently in the background as you need to ask for the users email and password.

This is how you restore a previously persisted Session object in the Podio SDK:

{% highlight java %}
Session persistedSession = getMyPersistedSession();
Podio.restoreSession(persistedSession);
{% endhighlight %}
