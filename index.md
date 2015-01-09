---
layout: default
---
# The Podio Android  SDK #
The Podio SDK for Android is a client library for communicating with the [Podio API](https://developers.podio.com). It provides an easy and convenient way of integrating your Android app with Podio.

The SDK requires Android API level 11 and  `android.permission.INTERNET` permissions being requested in your `AndroidManifest.xml` file.

Apart from above Android requirements, the SDK also uses the [Android Volley](https://android.googlesource.com/platform/frameworks/volley/) framework for the network traffic and the [Google Gson](https://code.google.com/p/google-gson/) library for parsing JSON. Both dependencies are included as pre-built jar files in the Podio SDK project sources.

The test project is using the [NanoHTTPD](http://nanohttpd.com/) web server for mocking the Internet on the test target.

The Podio SDK for Android is currently in a early development stage where the entire feature set has not yet been fully implemented. We are working very actively on making it feature complete within the near future.

## Integrate with your Android project

### Android Studio
Since the Podio SDK is now distributed through JCenter, you can very conveniently just add the Podio SDK as a module (or app) dependency like this:

{% highlight java %}
dependencies {
    compile 'com.podio.android:sdk:0.1.0'
}
{% endhighlight %}

And that's it. You may, however, want to check for the [latest version](https://bintray.com/podio/android/sdk/_latestVersion).


### Maven
As the Podio SDK doesn't exist in Maven Central, you'll need to add JCenter as a custom repository to your Maven POM file. An example of how to do it could look like this:

{% highlight xml %}
<project>
...
  <repositories>
    <repository>
      <id>my-repo1</id>
      <name>your custom repo</name>
      <url>http://jcenter.bintray.com/</url>
    </repository>
  </repositories>
...
</project>
{% endhighlight %}

And then you need to define your dependencies, which could look something like this:

{% highlight xml %}
<project>
...
  <dependencies>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.3.1</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.mcxiaoke.volley</groupId>
      <artifactId>library</artifactId>
      <version>1.0.9</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.podio.android</groupId>
      <artifactId>sdk</artifactId>
      <version>0.1.0</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>
...
</project>
{% endhighlight %}

### Other IDE, like Eclipse, IntelliJ etc
If your development environment isn't covered by above examples, you may need to manually download and provide the libraries as required by your environment. You'll need the [Podio SDK](https://bintray.com/podio/android/sdk/_latestVersion), which in turn depends on [Google Gson](https://bintray.com/bintray/jcenter/com.google.code.gson%3Agson/_latestVersion) and [Android Volley](https://bintray.com/bintray/jcenter/com.mcxiaoke.volley%3Alibrary/_latestVersion). You can find them all on JCenter (the "Files" link on respective page will take you to the actual binaries).

## Setup your API keys
Before you can communicate with the Podio API, you need to generate a set of API keys for your application from your "Account Settings" page on Podio. You can find further details [here](https://developers.podio.com/api-key).

Once you have a key and a corresponding secret, you need to setup the Podio SDK to use them:

{% highlight java %}
Podio.setup(context, "my_api_key", "my_secret");
{% endhighlight %}

And by that you're ready to start using the Podio SDK.

## How to use the SDK
Requesting data from the SDK can be done with two different approaches, both will deliver the same result, but in different ways.

Regardless of which approach you choose, the SDK will give you a `Future` object upon performing a request. You then have the option of providing a set of (optional) callback interfaces that will be called by the SDK when something is ready.

You can also choose to block the current thread while the SDK executes and get the result back directly from the request method.

A very simple example of how to request an app could look something like this (it, of course, requires you to already be authenticated through the SDK):

{% highlight java %}
RequestFuture<Application> future = Podio.application.get(123);
{% endhighlight %}

### Using the SDK in an asynchronous manner
The returned `Future` object offers ways of providing callback interfaces which will be called (on the main thread) at any point in the future when there is something to notify.

There are mainly two callback interfaces you should familiarize yourself with; the `ResultListener`, which will be called once the result of your request has been produced for you, and the `ErrorListener` which will notify you on any SDK or API provided errors.

Receiving a requested app asynchronously from the Podio SDK could, hence, look something like this:

{% highlight java %}
future.withResultListener(new ResultListener<Application>() {

    @Override
    public boolean onRequestPerformed(Application content) {
        // Do something with the result.
        return false;
    }

});
{% endhighlight %}

In the same manner you can provide an `ErrorListener`:

{% highlight java %}
future.withErrorListener(new ErrorListener() {

    @Override
    public boolean onErrorOccured(Throwable cause) {
        // Check for PodioError
        return false;
    }

});
{% endhighlight %}

Note the different injection methods (`withResultListener` vs. `withErrorListener`).

#### Global error listeners
If you don't want to provide an explicit error listener for each call you make, but rather prefer to have the same error management for all your requests, you can register any number of *global* error listeners directly on the `Podio` facade:

{% highlight java %}
ErrorListener globalErrorListener = new ErrorListener() {

    @Override
    public boolean onErrorOccured(Throwable cause) {
        // Handle the error once and for all.
        return false;
    }

};

Podio.addGlobalErrorListener(globalErrorListener);

// You remove a global listener like this:
// Podio.removeGlobalErrorListener(globalErrorListener);
{% endhighlight %}

An error event will now not only be given to your custom error listener on the request, would you provide one, but will also bubble up to your global error listeners. The custom callback can, however, choose to consume the event (by returning boolean `true`) and thereby prevent any further bubbling of it. This enables you to make specific error handling for specific calls, while having a general fallback for the others. 

### Using the SDK in a synchronous manner
Now, you may want to block the current thread while the SDK is executing. This is not recommended on the UI thread, though. However, you might be executing on a worker thread already, like with an `IntentService`, which you may want to keep alive during the entire Podio SDK execution flow. You can then take advantage of the Java `Future` aspects of the returned future object and block the current thread until the SDK delivers.

You'll still perform the request itself in the same way as with the asynchronous approach, but you'll also need to call the `get()` method on the returned future, something like below:

{% highlight java %}
try {
    Application application = future.get(20, TimeUnit.SECONDS);
} catch (ExecutionException e) {
    // Check the cause of e for PodioError.
} catch (TimeoutException e) {
    // No network error???
} catch (InterruptedException e) {
    // Unexpected error.
}
{% endhighlight %}

The timing arguments to the `get()` method are optional, which, when omitted, will render the `TimeoutException` trap superfluous.

The actual result of the request will be delivered as a return value of the `get()` method. This behaviour is coming with the Java `Future` heritage and is replacing the `ResultListener` callback.

Also note how the native `Future` implementation declares some checked exceptions which you need to handle in order to get past compiling. These exceptions are also replacing the `ErrorListener` callback.

Interesting to know is that the Podio SDK will internally still spawn a worker thread of its own and perform the request on that thread. You are, however, blocking any further execution of "your current thread" with the `get()` method of your request future, which will return first when the request is fully performed by the SDK (or an error occurs).

## The test suite
To run the test suite locally on your machine you need to have the latest Android SDK available and the $ANDROID_HOME environment variable configured properly.

1. Fetch the latest code from this repository.
1. Make sure you have an emulator running or a physical device connected to your computer.
1. From a terminal, find your way to the test directory of this repository and issue the `ant clean emma debug install test` command.
1. The test suite will be run on the emulator or device of yours and the result will be shown in the terminal window. Also note the "emma" directive which will generate code coverage reports for you. The report is found in the `[PATH_TO_SDK]/test/bin` folder as `coverage.html`.
