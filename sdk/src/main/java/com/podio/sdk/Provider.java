
package com.podio.sdk;

public abstract class Provider {

    protected Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    protected void validateClient() {
        if (this.client == null) {
            throw new NullPointerException("Your provider subclass MUST set a Client instance prior to a REST operation!");
        }
    }

    protected <T> Request<T> delete(Filter filter) {
        validateClient();
        return client.request(Request.Method.DELETE, filter, null, null);
    }

    protected <T> Request<T> get(Filter filter, Class<T> classOfResult) {
        validateClient();
        return client.request(Request.Method.GET, filter, null, classOfResult);
    }

    protected <T> Request<T> post(Filter filter, Object item, Class<T> classOfItem) {
        validateClient();
        return client.request(Request.Method.POST, filter, item, classOfItem);
    }

    protected <T> Request<T> put(Filter filter, Object item, Class<T> classOfItem) {
        validateClient();
        return client.request(Request.Method.PUT, filter, item, classOfItem);
    }

}
