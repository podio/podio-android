---
layout: default
---
# Working with Apps & Items

## Fetch items
[Apps](https://developers.podio.com/doc/applications) and [`Item`s](https://developers.podio.com/doc/items) are the cornerstones of the Podio platform. An App is a container of several `Item`s, which in turn may have several `Field`s. A simple analogy would be a spreadsheet of any kind. The sheet itself would be equivalent to the App, the columns in the sheet would be the `Field`s and the rows would be the `Item`s.

You can use the Podio SDK to fetch `Item`s you have already created in Podio. You can, e.g. fetch all `Item`s for a given `App` like this:

{% highlight java %}
Podio.Item.getForApplication(myAppId, new PodioProviderListener() {
    
    @Override
    public void onRequestComplete(Object tag, Object obj) {
        if (obj != null) {
            ItemRequest.Result res = (ItemRequest.Result) obj;
            Item[] items = res.items
            Log.d("MYTAG", "App count: " + item.length);
        }
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

Almost the same way you could also get a single `Item` by calling:

{% highlight java %}
Podio.Item.get(myAppId, new PodioProviderListener() {
    ...
});
{% endhighlight %}