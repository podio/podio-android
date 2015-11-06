
package com.podio.sdk.volley;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.Filter;
import com.podio.sdk.Request;
import com.podio.sdk.json.JsonParser;

public class MockRestClient<T> extends VolleyClient {
    public String data;
    public Uri uri;

    public MockRestClient(Context context) {
        super();
        super.setup(context, "https", "test", clientId, clientSecret, null, null);
    }

    @Override
    public <T> Request<T> request(Request.Method method, Filter filter, Object requestData, Class<T> classOfExpectedResult) {
        this.uri = filter.buildUri(scheme, authority);
        this.data = requestData != null ? JsonParser.toJson(requestData) : null;
        return VolleyRequest.newRequest(userAgent, method, this.uri.toString(), this.data, classOfExpectedResult);
    }
}
