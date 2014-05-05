# Podio SDK for Android #
The Podio SDK for Android is a client library for communicating with the [Podio API](https://developers.podio.com). It provides an easy and convenient way of integrating your Android app with Podio.

The SDK requires Android API level 11 as a minimum and it also requires  `android.permission.INTERNET` permissions being requested in your `AndroidManifest.xml` file.

Apart from above Android requirements, the SDK also uses the [Android Volley](https://android.googlesource.com/platform/frameworks/volley/) framework for the network traffic and the [Google Gson](https://code.google.com/p/google-gson/) library for parsing Json. Both dependencies are included as pre-build jar files in the Podio SDK project sources.

The Podio SDK for Android is currently in a very early development stage where the entire feature set has not yet been fully implemented. We are working very actively with making it feature complete within the near future.

## Integrate with your Android project ##
The project is currently made available as raw source code. You can get the source by cloning the git repository like this: `git clone git@github.com:podio/android-sdk.git` (VERIFY THIS!).

Your options of integration from here on are as wide as the Android framework enables: You can e.g choose to import the cloned source as an Android Library Project or build a JAR file out of it (don't forget to manually add the Volley and Gson jars as they are not included in the podio-sdk JAR) and add it to your projects `libs` folder.

The provided Ant build script gives you the option of building a JAR file by issuing the `ant clean jar` command from the SDK root. You can then add the `podio-sdk.jar` file to your existing Android projects `libs` folder.

## Use the Podio SDK ##
The use of the Podio SDK is made as straight forward as possible. A convenience "facade" is offered where you basically just make static method calls to achieve your Podio goals.

### Setup your API keys ###
But before you can communicate with the Podio API, you need to generate a set of API keys for your application from your "Account Settings" page on Podio. You can find further details on this [here](https://developers.podio.com/api-key).

Once you have a key and corresponding secret, you need to configure the Podio SDK to use it. You can do so by adding the following code to your `Application.onCreate()` method:

```java
Podio.Client.setup("my_api_key", "my_secret");
```

and by that you're ready to Podio.

### Authenticate ###
The Podio API supports multiple ways of authenticating a client. The Podio SDK for Android provides two primary options:

* Authenticate with username/password.
* Authenticate specific app tokens.

Both options are described below.

Any changes to the authentication state during a call to the Podio API is reported back to the caller through the `PodioProviderListener.onSessionChange()` callback method. It can be useful to listen to this callback if you wish to persist your authentication tokens in any way you'd find convenient. You can then later on revoke a previously stored authentication session by calling:

```java
Podio.Client.revokeSession(myPersistedSession);
```

You can find more information on authentication and the Podio API [here](https://developers.podio.com/authentication).

#### Authenticate as a user ####
This option is great when you want to have every user of your client app logging in using their own Podio account and thereby give them access to the content of their entire Podio account.

This is how you authenticate as a user:

```java
String username = userTextView.getText().toString();
String password = passTextView.getText().toString();

Podio.Client.authenticateAsUser(username, password,
        new PodioProviderListener() {
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
```

Good to know is that *if* the session has changed during the request, the session change callback is always reported *before* any of the success or failure callbacks.

#### Authenticate as an app ####
This option is useful when you want all users of your client app to interact with the same [Podio App](https://developers.podio.com/doc/applications), regardless of who they are or who created the Podio App. 

This method doesn't require any username and password but an AppId and AppToken instead. You find these credentials by logging in to Podio and browsing to the App. You should then click the small wrench icon to the top right of the screen and then pick the "Developer" option in the drop down menu that appears. You should then be taken to a page showing your app's ID and token.

This is how you authenticate as an app:

```java
public static final String MY_APP_ID = "my-app-id";
public static final String MY_APP_TOKEN = "my-app-token";

Podio.Client.authenticateAsApp(MY_APP_ID, MY_APP_TOKEN,
        new PodioProviderListener() {
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
```

### Fetch items ###
[Apps](https://developers.podio.com/doc/applications) and [Items](https://developers.podio.com/doc/items) are the cornerstones of the Podio platform. An App is a container of several Items, which in turn may have several Fields. A simple analogy would be a spreadsheet of any kind. The sheet itself would be equivalent to the App, the columns in the sheet would be the Fields and the rows would be the Items.

You can use the Podio SDK to fetch Items you have already created in Podio. You can, e.g fetch all Items for a given App like this:

```java
Podio.Item.getForApplication(myAppId,
        new PodioProviderListener() {
            @Override
            public void onRequestComplete(Object tag, Object obj) {
                Item item = (Item) obj;
                Log.d("MYTAG", "App Title: " + item.title);
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
```

Almost the same way you could also get a single Item by calling:

```java
Podio.Item.get(myAppId, new PodioProviderListener() {
            ...
        });
```

### Create items ###
Yet to be published.

### Update items ###
Yet to be implemented.

## The test suite ##
To run the test suite locally on your machine you need to have the latest Android SDK available and the $ANDROID_HOME environment variable configured properly.

1. Fetch the latest code from this repository.
1. Make sure you have an emulator running or a physical device connected to your computer.
1. From a terminal, find your way to the test directory of this repository and issue the `ant clean emma debug install test` command.
1. The test suite will be run on the emulator or device of yours and the result will be shown in the terminal window. Also note the "emma" directive which will generate code coverage reports for you. The report is found in the `[PATH_TO_SDK]/test/bin` folder as `coverage.html`.
