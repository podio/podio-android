---
layout: default
---
# Authentication & Session Management
The Podio API supports multiple ways of authenticating a client. The Podio SDK for Android provides two primary options:

* Authenticate with username/password.
* Authenticate with specific app tokens.

Both options are described below.

Any changes to the authentication state during a call to the Podio API is reported back to the caller through the `PodioProviderListener.onSessionChange()` callback method. It can be useful to listen to this callback if you wish to persist your authentication tokens in any way you'd find convenient. You can then later on restore a previously stored authentication session by calling:

{% highlight java %}
Podio.Client.restoreSession(myPersistedSession);
{% endhighlight %}

You can find more information on authentication and the Podio API [here](https://developers.podio.com/authentication).

## Authenticate as a user
This option is great when you want to have every user of your client app logging in using their own Podio account and thereby give them access to the content of their entire Podio account.

This is how you authenticate as a user:

{% highlight objective-c %}
String username = userTextView.getText().toString();
String password = passTextView.getText().toString();

Podio.Client.authenticateAsUser(username, password, new PodioProviderListener() {
    
    @Override
    public void onRequestComplete(Object tag, Object item) {
        // Yeay!
    }

    @Override
    public void onRequestFailure(Object tag, String msg) {
        // Ohno!
    }

    @Override
    public void onSessionChange(Object tag, Session state) {
        // Persist the Session object
    }
});
{% endhighlight %}

Good to know is that *if* the session has changed during the request, the session change callback is always reported *before* any of the success or failure callbacks.

## Authenticate as an app
This option is useful when you want all users of your client app to interact with the same [Podio `App`](https://developers.podio.com/doc/applications), regardless of who they are or who created the Podio `App`. 

This method doesn't require any username and password but an app id and app token instead. You find these credentials by logging in to Podio and browsing to the `App`. You should then click the small wrench icon to the top right of the screen and then pick the "Developer" option in the drop down menu that appears. You should then be taken to a page showing your app's ID and token.

This is how you authenticate as an app:

{% highlight objective-c %}
public static final String MY_APP_ID = "my-app-id";
public static final String MY_APP_TOKEN = "my-app-token";

Podio.Client.authenticateAsApp(MY_APP_ID, MY_APP_TOKEN, new PodioProviderListener() {
    
    @Override
    public void onRequestComplete(Object tag, Object item) {
      // Yeay!
    }

    @Override
    public void onRequestFailure(Object tag, String msg) {
        // Ohno!
    }

    @Override
    public void onSessionChange(Object tag, Session state) {
        // Persist the Session object
    }
});
{% endhighlight %}