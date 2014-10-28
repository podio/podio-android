---
layout: default
---
# Local Storage and Caching
Sometimes you'll find yourself with the need of temporarilly storing data in a way so that it survives your app being brought down by the Android system. For these cases the Podio SDK can offer a simple local storage infrastructure. It is actually a combination of an in-memory cache (based on the [Android LruCache](http://developer.android.com/reference/android/util/LruCache.html)) and good old file storage.

You can basically put anything in the local store, it doesn't even have to be Podio domain objects. Bare in mind, though, that the items will be serialized to JSON and stored as such on the file system. Hence, the object must be serializable to JSON notation in order to be persisted on disk.

The local store works in an asynchronous manner, just as the Podio SDK. You even get the same type of `Request` objects back when trying to manipulate it and you inject the same `ResultListener` and `ErrorListener` callbacks as you would do when accesing the Podio API. The local store doesn't handle `SessionListener` callbacks, though. It will throw an `UnsupportedOperationException` would you still try to inject such a listener.

This is how you setup a local store to interact with:

{% highlight java %}
Store localStore = null;
Context context = getContext();
String storeName = "myStoreName";
int cacheSizeInKb = 1000;

Podio.store
    .open(context, storeName, cacheSizeInKb)
    .withResultListener(new ResultListener<Store>() {

        @Override
        public boolean onRequestPerformed(Store store) {
            localStore = store;
            return false;
        }

    })
    .withErrorListener(new ErrorListener() {

        @Override
        public boolean onErrorOccured(Throwable cause) {
            cause.printStackTrace();
            return false;
        }
        
    });
{% endhighlight %}

The main difference in how you use the local store, compared to how you use the Podio API, is that you're operating on an instance variable when manipulating the store (the API is manipulated through the static `Podio` facade). A call to the `Podio.store.open(...)` method will, through the injected `ResultListener` callback, return the actual store instance you later on can interact with. The store doesn't hold any references to the provided context, it's only used to find the path to the disk store on the internal memory.

The initialization is done on a spawned worker thread as it involves file system access, which - as we all know - doesn't really have a predictable timing behavior by its nature.

Would you pass on a null-pointer `Context` or an empty store name, then the disk store will not be initialized and you'd have a really fast in-memory cache only. The way to operate the store wouldn't change by this, though.

## Store objects
When you're adding an object to the local store you just pass it to the infrastructure along with a key. The object will be added to the cache as well as written to the filesystem in your internal memory. Any previous objects with the same key will be overwritten silently.

The key can be any object, but the local store implementation will call the `toString()` method on it prior to persisting it to disk, so you need to make sure your keys don't change over time.

This is how you put an item in the store:

{% highlight java %}
Object myObject = someHowGetMyObject();
String key = "myObject";

localStore
    .set(key, myObject)
    .withResultListener(new ResultListener<Void>() {

        @Override
        public boolean onRequestPerformed(Void nothing) {
            //  Yeay!
            return false;
        }

    })
    .withErrorListener(new ErrorListener() {

        @Override
        public boolean onErrorOccured(Throwable throwable) {
            // The disk may be full.
            throwable.printStackTrace();
            return false;
        }

    });
{% endhighlight %}

The callbacks are, of course, optional. If you're feeling a bit crazy, you can completely omit them and you'd have a one-liner method call instead.

## Retrieve objects
To retrieve an object you simply use the same key you used when you stored the object (yes, the disk store is case sensitive when it comes to strings).

The store tries to fetch the object from the memory cache first. This is an extremely fast operation, it will most likely be finished by the time you add your result listener if the object is found directly.

In case the requested object is *not* found in the cache, the store will look for it on disk as well. If found there, it will be put back into the cache (possibly evicting something else if the memory constraints are violated). If the object isn't found on disk either, then simply a null-pointer is delivered.

{% highlight java %}
localStore
    .get(key, MyObject.class)
    .withResultListener(new ResultListener<MyObject>() {

        @Override
        public boolean onRequestPerformed(MyObject myObject) {
            if (myObject != null) {
                Log.d("TAG", "My object fetched: " + myObject.getValue());
            } else {
                Log.d("TAG", "The requested object couldn't be found in the local store");
            }

            return false;
        }

    })
    .withErrorListener(new ErrorListener() {

        @Override
        public boolean onErrorOccured(Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }

    });
{% endhighlight %}

Note, that if you don't want to use the disk store (as described above), you can execute the get call in a synchronous manner and will thereby have a very compact cache fetch call:

{% highlight java %}
MyObject object = localStore.get(key, null).get();
{% endhighlight %}

This is possible while the `get(key, null)` returns a custom `Future` object, which can be used to block the current thread until the `Future` delivers it's result (by calling the second, no-arguments `get()` method). Since we are not depending on any nasty file system access, this call will return virtually immediately, hence no risk for blocking the main thread for too long.

## Remove objects
Some use cases call for removing single objects from the store, e.g. when you remove the corresponding item from the API. The `remove` request removes the object both from the memory cache as well as the disk store. Again, it's imperative to use the same key you used when you added the object:

{% highlight java %}
localStore
    .remove(key)
    .withResultListener(new ResultListener<Void>() {

        @Override
        public boolean onRequestPerformed(Void nothing) {
            Log.d("TAG", "Object removed");
            return false;
        }
        
    })
    .withErrorListener(new ErrorListener() {

        @Override
        public boolean onErrorOccured(Throwable cause) {
            cause.printStackTrace();
            return false;
        }
        
    });
{% endhighlight %}

## Clear cache
There are two additional ways to clear the local store which have different consequences. One approach is to clear the memory cache only, leaving the disk store intact. This is interesting when you get notified on low memory by the Android system. You can then act accordingly in a somewhat controlled manner before the system gets harsh with you. You can clear the memory cache like this:

{% highlight java %}
localStore
    .free()
    .withResultListener(new ResultListener<Void>() {

        @Override
        public boolean onRequestPerformed(Void nothing) {
            Log.d("TAG", "Memory released");
            return false;
        }
        
    })
    .withErrorListener(new ErrorListener() {

        @Override
        public boolean onErrorOccured(Throwable cause) {
            cause.printStackTrace();
            return false;
        }
        
    });
{% endhighlight %}

There are also some use cases when you would want to erase the entire local store, including the disk store, say, if you're managing an account and your user explicitly chooses to log out. Common courtesy suggests you to remove any previously stored content for that account as well. In such a case you could call the `erase()` method instead, which then would remove the entire store in both the memory cache as well as on disk:

{% highlight java %}
localStore
    .erase()
    .withResultListener(new ResultListener<Void>() {

        @Override
        public boolean onRequestPerformed(Void nothing) {
            Log.d("TAG", "Memory released");
            return false;
        }
        
    })
    .withErrorListener(new ErrorListener() {

        @Override
        public boolean onErrorOccured(Throwable cause) {
            cause.printStackTrace();
            return false;
        }
        
    });
{% endhighlight %}

Note, that if you're maintaining multiple stores, you'll need to pass the `erase` request to all of them.