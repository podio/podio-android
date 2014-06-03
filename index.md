---
layout: default
---
# Podio Android  SDK #
The Podio SDK for Android is a client library for communicating with the [Podio API](https://developers.podio.com). It provides an easy and convenient way of integrating your Android app with Podio.

The SDK requires Android API level 11 as a minimum and it also requires  `android.permission.INTERNET` permissions being requested in your `AndroidManifest.xml` file.

Apart from above Android requirements, the SDK also uses the [Android Volley](https://android.googlesource.com/platform/frameworks/volley/) framework for the network traffic and the [Google Gson](https://code.google.com/p/google-gson/) library for parsing JSON. Both dependencies are included as pre-built jar files in the Podio SDK project sources.

The test project is using the [NanoHTTPD](http://nanohttpd.com/) web server for mocking the Internet on the test target.

The Podio SDK for Android is currently in a very early development stage where the entire feature set has not yet been fully implemented. We are working very actively on making it feature complete within the near future.

## Integrate with your Android project
The project is currently made available as raw source code. You can get the source by cloning the git repository like this: `git clone git@github.com:podio/podio-android.git`.

Your options of integration from here on are as wide as the Android framework enables: You can e.g. choose to import the cloned source as an Android Library Project or build a JAR file out of it (don't forget to manually add the Volley and Gson jars as they are not included in the podio-sdk JAR) and add it to your projects `libs` folder.

The provided Ant build script gives you the option of building a JAR file by issuing the `ant clean jar` command from the SDK root. You can then add the `podio-sdk.jar` file to your existing Android projects `libs` folder.

### Setup your API keys
But before you can communicate with the Podio API, you need to generate a set of API keys for your application from your "Account Settings" page on Podio. You can find further details on this [here](https://developers.podio.com/api-key).

Once you have a key and corresponding secret, you need to configure the Podio SDK to use it. You can do so by adding the following code to your `Application.onCreate()` method:

{% highlight java %}
Podio.setup(context, "my_api_key", "my_secret");
{% endhighlight %}

and by that you're ready to start using the Podio SDK.

## The test suite
To run the test suite locally on your machine you need to have the latest Android SDK available and the $ANDROID_HOME environment variable configured properly.

1. Fetch the latest code from this repository.
1. Make sure you have an emulator running or a physical device connected to your computer.
1. From a terminal, find your way to the test directory of this repository and issue the `ant clean emma debug install test` command.
1. The test suite will be run on the emulator or device of yours and the result will be shown in the terminal window. Also note the "emma" directive which will generate code coverage reports for you. The report is found in the `[PATH_TO_SDK]/test/bin` folder as `coverage.html`.
