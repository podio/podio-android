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

import com.podio.sdk.ErrorListener;
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
    private ErrorListener errorListener;

    public RequestFuture(Callable<RestResult<T>> callable) {
        super(callable);
    }

    @Override
    protected void done() {
        RestResult<T> result = getResultNow("Couldn't fetch result on completion");
        reportResult(sessionListener, result);
        reportResult(errorListener, result);
        reportResult(resultListener, result);
    }

    public RequestFuture<T> setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;

        if (isDone()) {
            RestResult<T> result = getResultNow("Couldn't report error");
            reportResult(errorListener, result);
        }

        return this;
    }

    public RequestFuture<T> setResultListener(ResultListener<? super T> resultListener) {
        this.resultListener = resultListener;

        if (isDone()) {
            RestResult<T> result = getResultNow("Couldn't report result");
            reportResult(resultListener, result);
        }

        return this;
    }

    public RequestFuture<T> setSessionListener(SessionListener sessionListener) {
        this.sessionListener = sessionListener;

        if (isDone()) {
            RestResult<T> result = getResultNow("Couldn't report session change");
            reportResult(sessionListener, result);
        }

        return this;
    }

    private RestResult<T> getResultNow(String messageIfFailed) {
        try {
            return get();
        } catch (InterruptedException e) {
            throw new PodioException(messageIfFailed, e);
        } catch (ExecutionException e) {
            throw new PodioException(messageIfFailed, e);
        }
    }

    private void reportResult(final ErrorListener errorListener, final RestResult<T> result) {
        if (errorListener != null && result != null && result.hasException()) {
            HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    Exception exception = result.getException();
                    errorListener.onExceptionOccurred(exception);
                }

            });
        }
    }

    private void reportResult(final ResultListener<? super T> resultListener, final RestResult<T> result) {
        if (resultListener != null && result != null) {
            HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    T item = result.getItem();
                    resultListener.onRequestPerformed(item);
                }

            });
        }
    }

    private void reportResult(final SessionListener sessionListener, final RestResult<T> result) {
        if (sessionListener != null && result != null && result.hasSession()) {
            HANDLER.post(new Runnable() {

                @Override
                public void run() {
                    Session session = result.getSession();
                    sessionListener.onSessionChanged(session);
                }

            });
        }
    }

}
