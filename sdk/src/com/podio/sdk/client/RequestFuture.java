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

package com.podio.sdk.client;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import android.os.Handler;
import android.os.Looper;

import com.podio.sdk.PodioException;
import com.podio.sdk.ResultListener;
import com.podio.sdk.SessionListener;
import com.podio.sdk.domain.Session;

/**
 * @author László Urszuly
 */
public class RequestFuture<T> extends FutureTask<RestResult<T>> {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private ResultListener<? super T> resultListener;
    private SessionListener sessionListener;

    public RequestFuture(Callable<RestResult<T>> callable) {
        super(callable);
    }

    @Override
    protected void done() {
        reportResult(sessionListener);
        reportResult(resultListener);
    }

    public RequestFuture<T> withResultListener(ResultListener<? super T> resultListener) {
        this.resultListener = resultListener;

        if (isDone()) {
            reportResult(resultListener);
        }

        return this;
    }

    public RequestFuture<T> withSessionListener(SessionListener sessionListener) {
        this.sessionListener = sessionListener;

        if (isDone()) {
            reportResult(sessionListener);
        }

        return this;
    }

    private RestResult<T> getResultNow() {
        try {
            return get();
        } catch (InterruptedException e) {
            throw PodioException.fromThrowable(e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof PodioException) {
                throw (PodioException) e.getCause();
            } else {
                throw PodioException.fromThrowable(e);
            }
        }
    }

    private void reportResult(final ResultListener<? super T> resultListener) {
        if (resultListener != null) {
            HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    RestResult<T> result = getResultNow();

                    T item = result.getItem();
                    resultListener.onRequestPerformed(item);
                }

            });
        }
    }

    private void reportResult(final SessionListener sessionListener) {
        if (sessionListener != null) {
            HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    RestResult<T> result = getResultNow();

                    if (result != null && result.hasSession()) {
                        Session session = result.getSession();
                        sessionListener.onSessionChanged(session);
                    }
                }

            });
        }
    }

}
