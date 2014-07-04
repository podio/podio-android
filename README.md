# Podio SDK for Android #
The Podio SDK for Android is a client library for communicating with the [Podio API](https://developers.podio.com). It provides an easy and convenient way of integrating your Android app with Podio.

The SDK requires Android API level 14 as a minimum and it also requires  `android.permission.INTERNET` permissions being requested in your `AndroidManifest.xml` file.

Apart from above Android requirements, the SDK also uses the [Android Volley](https://android.googlesource.com/platform/frameworks/volley/) framework for the network traffic and the [Google Gson](https://code.google.com/p/google-gson/) library for parsing JSON. Both dependencies are included as pre-built jar files in the Podio SDK project sources.

The test project is using the [NanoHTTPD](http://nanohttpd.com/) web server for mocking the Internet on the test target.

The Podio SDK for Android is currently in a early development stage where the entire feature set has not yet been fully implemented. We are working very actively on making it feature complete within the near future.

## Integrate with your Android project ##
The project is currently made available as raw source code. You can get the source by cloning the git repository: `git clone git@github.com:podio/podio-android.git` or by downloading it as a ZIP archive from this very page (find the "Download ZIP" button above).

Your options of integration from here on are as wide as the Android framework enables: You can e.g. choose to import the cloned source as an Android Library Project or build a JAR file out of it (don't forget to manually copy the Volley and Gson JARs to your `libs` directory as they are not included in the podio-sdk JAR).

The provided Ant build script gives you the option of building a JAR file by issuing the `ant clean jar` command from the SDK root. You can then add the `podio-sdk.jar` file to your existing Android projects `libs` folder.

## How to use the Podio SDK ##
The use of the Podio SDK is made as straight forward as possible. A convenience "facade" is offered where you basically just make static method calls to interact with the Podio API.

### Setup your API keys ###
But before you can communicate with the Podio API, you need to generate a set of API keys for your application from your "Account Settings" page on Podio. You can find further details on this [here](https://developers.podio.com/api-key).

Once you have a key and corresponding secret, you need to setup the Podio SDK to use them.

```java
Podio.setup(context, "my_api_key", "my_secret");
```

and by that you're ready to Podio.

### Authenticate ###
The Podio API supports multiple ways of authenticating a client. The Podio SDK for Android provides two primary options:

* Authenticate with username/password.
* Authenticate with specific app tokens.

Both options are described below.

Any changes to the authentication state during a call to the Podio API is reported back to the caller through the `SessionListener.onSessionChanged(Sessoin session)` callback method. It can be useful to listen to this callback if you wish to persist your authentication tokens in any way you'd find convenient. You can then later on restore a previously stored authentication session by calling:

```java
Podio.restoreSession(myPersistedSession);
```

You can find more information on authentication and the Podio API [here](https://developers.podio.com/authentication).

#### Authenticate as a user ####
This option is great when you want to have every user of your client app logging in using their own Podio account and thereby give them access to the content of their entire Podio account.

This is how you authenticate as a user:

```java
String username = userTextView.getText().toString();
String password = passTextView.getText().toString();


Podio.client.authenticateWithUserCredentials(username, password,
        new ResultListener<Session>() {

            @Override
            public void onRequestPerformed(Session session) {
                // Yeay!
            }

        },
        new ErrorListener() {

            @Override
            public void onExceptionOccurred(Exception exception) {
                // Oh no!
            }

        });
```

#### Authenticate as an app ####
This option is useful when you want all users of your client app to interact with the same Podio app, regardless of who they are or who created the Podio app. 

This method doesn't require any username and password but an app id and app token instead. You find these credentials by logging in to Podio and browsing to the app in question. You should then click the wrench icon to the top right of the screen and then pick the "Developer" option in the drop down menu. You should then be taken to a page showing your app's ID and token.

This is how you authenticate as an app:

```java
public static final String MY_APP_ID = "my-app-id";
public static final String MY_APP_TOKEN = "my-app-token";

Podio.client.authenticateWithAppCredentials(MY_APP_ID, MY_APP_TOKEN,
        new ResultListener<Session>() {

            @Override
            public void onRequestPerformed(Session session) {
                // Yeay!
            }

        },
        new ErrorListener() {

            @Override
            public void onExceptionOccurred(Exception exception) {
                // Oh no!
            }

        });
```

### Fetch items ###
Apps and items are the cornerstones of the Podio platform. A Podio app is a container of several items, which in turn may have several fields. A simple analogy would be a spreadsheet of any kind. The sheet itself would be equivalent to the Podio app, the columns in the sheet would be the fields and the rows would be the items.

You can use the Podio SDK to fetch items you have already created in Podio. You can, e.g. fetch the 20 last edited items for a given app like this:

```java
Podio.item.filter()
        .onSpan(20, 0)
        .onSortOrder("last_edit_on", true)
        .get(appId,
            new ResultListener<Item.FilterResult>() {

                @Override
                public void onRequestPerformed(Item.FilterResult result) {
                    Item[] items = result.items
                    Log.d("MYTAG", "App count: " + item.length);
                }

            },
            new ErrorListener() {

                @Override
                public void onExceptionOccurred(Exception exception) {
                    // Oh no!
                }

            },
            new SessionListener() {

                @Override
                public void onSessionChanged(Session session) {
                    // Persist new session
                }

            });
```

Would you want to get a single item, this would be the way to go:

```java
Podio.item.get(itemId,
        new ResultListener<Item>() {

            @Override
            public void onRequestPerformed(Item result) {
            }

        },
        new ErrorListener() {

            @Override
            public void onExceptionOccurred(Exception exception) {
            }

        },
        new SessionListener() {

            @Override
            public void onSessionChanged(Session session) {
            }

        });
```

### Create items ###
You need to know the names for the fields you want to populate in your new items. These names are not necessarily the same names you see in the web-browser, though. You should instead go to the same page where you found the app id and token (the wrench icon and the "Developer" option, remember?) to find the programmatic names for them.

Once you know the field names you should create a new `Item` object, add the values to it and send it to the Podio API. Like this:

```java
Item item = new Item();
item.addValue("my-text-field", "hello there");
item.addValue("my-number-field", 12);

Podio.item.create(appId, item,
        new ResultListener<Item.PushResult>() {

            @Override
            public void onRequestPerformed(Item.PushResult result) {
            }

        },
        new ErrorListener() {

            @Override
            public void onExceptionOccurred(Exception exception) {
            }

        },
        new SessionListener() {

            @Override
            public void onSessionChanged(Session session) {
            }

        });
```

### Update items ###
The update flow follows the same principles as the create flow; You create an item, add the field values you wish to update and send the item to the Podio API:

```java
Item item = new Item();
item.addValue("my-text-field", "updated: bye bye");
item.addValue("my-contact-field", 4232334); // the user profile id

Podio.item.update(appId, item,
        new ResultListener<Item.PushResult>() {

            @Override
            public void onRequestPerformed(Item.PushResult result) {
            }

        },
        new ErrorListener() {

            @Override
            public void onExceptionOccurred(Exception exception) {
            }

        },
        new SessionListener() {

            @Override
            public void onSessionChanged(Session session) {
            }

        });
```

## The test suite ##
To run the test suite locally on your machine you need to have the latest Android SDK available and the $ANDROID_HOME environment variable configured properly.

1. Fetch the latest code from this repository.
1. Make sure you have an emulator running or a physical device connected to your computer.
1. From a terminal, find your way to the test directory of this repository and issue the `ant clean emma debug install test` command.
1. The test suite will be run on the emulator or device of yours and the result will be shown in the terminal window. Also note the "emma" directive which will generate code coverage reports for you. The report is found in the `[PATH_TO_SDK]/test/bin` folder as `coverage.html`.

