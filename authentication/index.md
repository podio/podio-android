---
layout: default
---
# Authentication & Session Management
The Podio API supports multiple ways of authenticating a client. The Podio SDK for Android provides two primary options:

* Authenticate with username/password.
* Authenticate with specific app tokens.

Both options are described below and you can find more information on authentication and the Podio API [here](https://developers.podio.com/authentication).

## Authenticate as a user
This option is great when you want to have every user of your client app logging in using their own Podio account and thereby give them access to the content of their entire Podio account.

This is how you authenticate as a user:

{% highlight java %}
String username = userTextView.getText().toString();
String password = passTextView.getText().toString();

Podio.client
    .authenticateWithUserCredentials(username, password)
    .withSessionListener(new SessionListener() {

        @Override
        public boolean onSessionChanged(String authToken, String refreshToken, long expires) {
            // Yeay! My initial session is here!
            return false;
        }

    })
    .withErrorListener(new ErrorListener() {

        @Override
        public boolean onErrorOccured(Throwable cause) {
            // Oh no! I couldn't log in.
            // Maybe the 'cause' could tell me why?!
            // I sure hope it's a PodioError!
            return false;
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
    .withSessionListener(new SessionListener() {

        @Override
        public boolean onSessionChanged(String authToken, String refreshToken, long expires) {
            // Yeay!
            return false;
        }

    })
    .withErrorListener(new ErrorListener() {

        @Override
        public boolean onErrorOccured(Throwable cause) {
            // Oh no! I couldn't log in...
            return false;
        }

    });
{% endhighlight %}

## Session management
The SDK is, in some sense, quite smart as it automatically tries to refresh an expired user session for you (if it fails it will deliver an error). If you wish to get notified on these automatic and silent session changes, you need to provide a `SessionListener` callback implementation to the SDK. This is true regardless of which approach of requests you choose - synchronous or asynchronous. The below example shows how to add a `SessionListener`:

{% highlight java %}
SessionListener globalSessionListener = new SessionListener() {

    @Override
    public boolean onSessionChanged(String authToken, String refrshToken, long expires) {
        // Persist the session data.
        return false;
    }

};

Podio.addGlobalSessionListener(globalSessionListener);
{% endhighlight %}

There is a narrow but, from a usability perspective, still very important use case for this:

The Podio SDK can be initialized with previously stored session data. This basically allows you to "continue where you left of" in terms of session validity, without requiring your user to re-authenticate that frequently, especially after some time of inactivity.

You don't necessarily need this feature if you chose to [authenticate as an app](https://developers.podio.com/authentication/app_auth), as you can, then, silently re-authenticate with your app credentials in the background. However if your Android app requires [user authentication](https://developers.podio.com/authentication/username_password), you can't do that silently in the background as you need to ask for the users email and password.

> Important to understand is that the Podio SDK will *not*, under any circumstances what-so-ever, store your username and/or password. It will only use the last known *refresh token* for this auto-refresh behaviour and the validation is done on the Podio servers.

This is how you restore a previously persisted Session object in the Podio SDK:

{% highlight java %}
String authToken = somehowGetMyPreviouslyStoredAuthToken();
String refreshToken = somehowGetMyPreviouslyStoredRefreshToken();
long expires = somehowGetMyPreviouslyStoredSessionExpiryEpoch();

Podio.restoreSession(authToken, refreshToken, expires);
{% endhighlight %}

Generally (but not necessarily) you'd run this code right after the `Podio.setup()` call.

You can, technically, also add custom session listeners to each request you make, just as with the error listeners. You'll find the custom session listeners to have a very "notification-ish" nature, though, like during a sign-in flow, where you'd want to fire of any further Podio requests directly on a successful sign in. You'd catch the success-state in a custom `SessionListener` provided to the authentication request, but let the event bubble up to the global listener in order to be persisted. Be observant on how you manage the bubbling of the event, though (don't consume it unless that's what you want).

