/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of
 *  this software and associated documentation files (the "Software"), to deal in
 *  the Software without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.podio.sdk.push;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.podio.sdk.internal.CallbackManager;

import java.util.concurrent.ExecutionException;

public class VolleyLongPollingTransport implements Transport, Listener<String>, ErrorListener {
    private static RequestQueue metaQueue;
    private static RequestQueue dataQueue;

    private static class VolleyRequest extends StringRequest {
        private final Object data;

        private VolleyRequest(String url, Object data, RequestFuture<String> future) {
            this(url, data, future, future);
        }

        private VolleyRequest(String url, Object data, Listener<String> resultListener, ErrorListener errorListener) {
            super(Request.Method.POST, url, resultListener, errorListener);
            setShouldCache(false);
            this.data = data;
        }

        @Override
        public String getBodyContentType() {
            return "application/json; charset=UTF-8";
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            if (data != null) {
                Gson gson = new Gson();
                String json = gson.toJson(data);

                if (json != null) {
                    return json.getBytes();
                }
            }

            return super.getBody();
        }
    }

    private final String url;
    private final CallbackManager<String> callbackManager;

    public VolleyLongPollingTransport(Context context, String url) {
        if (metaQueue == null) {
            metaQueue = Volley.newRequestQueue(context);
            metaQueue.start();
        }

        if (dataQueue == null) {
            dataQueue = Volley.newRequestQueue(context);
            dataQueue.start();
        }

        this.url = url;
        this.callbackManager = new CallbackManager<String>();
    }

    @Override
    public String connect(Object connectData, int timeoutMillis) {
        VolleyRequest request = new VolleyRequest(url, connectData, this, this);
        request.setRetryPolicy(new DefaultRetryPolicy(timeoutMillis, 0, 0.0f));
        dataQueue.add(request);
        request.setRequestQueue(dataQueue);

        return null;
    }

    @Override
    public String disconnect(Object disconnectData) {
        RequestFuture<String> future = RequestFuture.newFuture();
        VolleyRequest request = new VolleyRequest(url, disconnectData, future);
        return waitForResult(future, request, metaQueue);
    }

    @Override
    public String initialize(Object handshakeData) {
        clearQueue(dataQueue);
        RequestFuture<String> future = RequestFuture.newFuture();
        VolleyRequest request = new VolleyRequest(url, handshakeData, future);
        return waitForResult(future, request, metaQueue);
    }

    @Override
    public String close() {
        clearQueue(metaQueue);
        clearQueue(dataQueue);
        return null;
    }

    @Override
    public String configure(Object object) {
        RequestFuture<String> future = RequestFuture.newFuture();
        VolleyRequest request = new VolleyRequest(url, object, future);
        return waitForResult(future, request, metaQueue);
    }

    @Override
    public void setErrorListener(com.podio.sdk.Request.ErrorListener listener) {
        callbackManager.addErrorListener(listener, false, null);
    }

    @Override
    public void setEventListener(com.podio.sdk.Request.ResultListener<String> listener) {
        callbackManager.addResultListener(listener, false, null);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        deliverError(error.getCause());
    }

    @Override
    public void onResponse(String json) {
        callbackManager.deliverResultOnMainThread(json);
    }

    private String waitForResult(RequestFuture<String> future, VolleyRequest request, RequestQueue queue) {
        queue.add(request);
        request.setRequestQueue(queue);

        try {
            String result = future.get();
            return result;
        } catch (InterruptedException e) {
            deliverError(e);
            return null;
        } catch (ExecutionException e) {
            deliverError(e.getCause());
            return null;
        }
    }

    private void clearQueue(RequestQueue queue) {
        queue.cancelAll(new RequestFilter() {

            @Override
            public boolean apply(Request<?> request) {
                return true;
            }

        });
    }

    private void deliverError(Throwable cause) {
        callbackManager.deliverErrorOnMainThread(cause);
    }

}
