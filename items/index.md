---
layout: default
---
# Working with Apps & Items

Apps and items are the cornerstones of the Podio platform. A Podio app is a container of several items, which in turn may have several fields. A simple analogy would be a spreadsheet of any kind. The sheet itself would be equivalent to the Podio app, the columns in the sheet would be the fields and the rows would be the items.

## Fetch items
You can use the Podio SDK to fetch items you have already created in Podio. You can, e.g. fetch the 20 last edited items for a given app like this:

{% highlight java %}
Podio.item
        .filter()
            .onSpan(20, 0)
            .onSortOrder("last_edit_on", true)
        .get(appId)
        .withResultListener(new ResultListener<Item.FilterResult>() {

            @Override
            public boolean onRequestPerformed(Item.FilterResult result) {
                // The result will contain your items and some
                // other information on the filter result.
                return false;
            }

        })
        .withErrorListener(new ErrorListener() {

            @Override
            public boolean onErrorOccured(Throwable cause) {
                // Check if cause is a PodioException.
                // Handle error accordingly.
                return false;
            }
        })
        .withSessionListener(new SessionListener() {

            @Override
            public boolean onSessionChanged(Session session) {
                // Persist the Session object.
                return false;
            }

        });
{% endhighlight %}

Would you want to get a single item, this would be the way to go:

{% highlight java %}
Podio.item
        .get(itemId)
        .withResultListener(new ResultListener<Item>() {

            @Override
            public boolean onRequestPerformed(Item result) {
                // Do something with the result.
                return false;
            }

        });
{% endhighlight %}

Note how all callback interfaces are optional to provide (even though a get-request wouldn't make sense without a result listener).

### Create items ###
You need to know the names for the fields you want to populate in your new items. These names are not necessarily the same names you see in your browser, though. You should instead go to the app view of interest, click the little wrench icon in the top right of the view, and pick the "Developer" option in the pop-up menu. The view that will be presented to you now will hold the programmatic names of your fields.

Once you know the field names you should create a new `Item` object, add the values to it and send it to the Podio API. Like this:

{% highlight java %}
Item item = new Item();
item.addValue("my-text-field", "hello there");
item.addValue("my-number-field", 12);

Podio.item.create(appId, item);
{% endhighlight %}

Would you be interested in the result, you'd have to provide a result listener and/or an error listener as usual.

### Update items ###
The update flow follows the exact same principles as the create flow; You create an item, add the field values you wish to update and send the item to the Podio API:

{% highlight java %}
Item item = new Item();
item.addValue("my-text-field", "updated: bye bye");
item.addValue("my-contact-field", 4232334); // the new user profile id

Podio.item.update(appId, item);
{% endhighlight %}
