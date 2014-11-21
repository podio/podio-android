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

import java.util.concurrent.ExecutionException;

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

public class VolleyLongPollingTransport implements Transport, Listener<String>, ErrorListener {
    private static RequestQueue metaQueue;
    private static RequestQueue dataQueue;

    private static final Listener<String> SILENT = new Listener<String>() {

        @Override
        public void onResponse(String json) {
            // This listener won't propagate any results to the external
            // callbacks.
        }

    };

    private static class VolleyRequest extends StringRequest {
        private final Object data;

        private VolleyRequest(String url, Object data, ErrorListener errorListener) {
            this(url, data, SILENT, errorListener);
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

    private com.podio.sdk.Request.ErrorListener errorListener;
    private com.podio.sdk.Request.ResultListener<String> resultListener;

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
        this.errorListener = null;
        this.resultListener = null;
    }

    @Override
    public String connect(Object connectData, int timeoutMillis) {
        VolleyRequest request = new VolleyRequest(url, connectData, this);
        request.setRetryPolicy(new DefaultRetryPolicy(timeoutMillis, 0, 0.0f));
        return waitForResult(request, dataQueue);
    }

    @Override
    public String disconnect(Object disconnectData) {
        VolleyRequest request = new VolleyRequest(url, disconnectData, this);
        return waitForResult(request, metaQueue);
    }

    @Override
    public String open(Object handshakeData) {
        clearQueue(dataQueue);
        VolleyRequest request = new VolleyRequest(url, handshakeData, this);
        return waitForResult(request, dataQueue);
    }

    @Override
    public String close() {
        clearQueue(metaQueue);
        clearQueue(dataQueue);
        return null;
    }

    @Override
    public String send(final Object object) {
        VolleyRequest request = new VolleyRequest(url, object, this, this);
        return waitForResult(request, metaQueue);
    }

    @Override
    public void setErrorListener(com.podio.sdk.Request.ErrorListener listener) {
        errorListener = listener;
    }

    @Override
    public void setEventListener(com.podio.sdk.Request.ResultListener<String> listener) {
        resultListener = listener;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        deliverError(error.getCause());
    }

    @Override
    public void onResponse(String json) {
        if (resultListener != null) {
            resultListener.onRequestPerformed(json);
        }
    }

    private String waitForResult(VolleyRequest request, RequestQueue queue) {
        RequestFuture<String> future = RequestFuture.newFuture();
        future.setRequest(request);

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
        if (errorListener != null) {
            errorListener.onErrorOccured(cause);
        }
    }

}
