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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import android.os.Handler;
import android.os.Looper;

import com.podio.sdk.ErrorListener;
import com.podio.sdk.ResultListener;
import com.podio.sdk.SessionListener;
import com.podio.sdk.domain.Session;

/**
 * @author László Urszuly
 */
public class RequestFuture<T> extends FutureTask<RestResult<T>> {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static final ArrayList<WeakReference<ErrorListener>> ERROR_LISTENERS = new ArrayList<WeakReference<ErrorListener>>();
    private static final ArrayList<WeakReference<SessionListener>> SESSION_LISTENERS = new ArrayList<WeakReference<SessionListener>>();

    public static void addErrorListener(ErrorListener errorListener) {
        if (errorListener != null) {
            ERROR_LISTENERS.add(new WeakReference<ErrorListener>(errorListener));
        }
    }

    public static void addSessionListener(SessionListener sessionListener) {
        if (sessionListener != null) {
            SESSION_LISTENERS.add(new WeakReference<SessionListener>(sessionListener));
        }
    }

    public static void removeErrorListener(ErrorListener errorListener) {
        for (WeakReference<ErrorListener> reference : ERROR_LISTENERS) {
            if (reference != null) {
                ErrorListener listener = reference.get();

                if (listener == errorListener || listener == null) {
                    reference.clear();
                    ERROR_LISTENERS.remove(reference);
                }
            }
        }
    }

    public static void removeSessionListener(SessionListener sessionListener) {
        for (WeakReference<SessionListener> reference : SESSION_LISTENERS) {
            if (reference != null) {
                SessionListener listener = reference.get();

                if (listener == sessionListener || listener == null) {
                    reference.clear();
                    SESSION_LISTENERS.remove(reference);
                }
            }
        }
    }

    private WeakReference<ErrorListener> errorListener;
    private WeakReference<ResultListener<? super T>> resultListener;
    private WeakReference<SessionListener> sessionListener;
    private Throwable error;

    public RequestFuture(Callable<RestResult<T>> callable) {
        super(callable);
    }

    @Override
    protected void done() {
        reportSessionChange();
        reportResult();
        reportError();
    }

    public RequestFuture<T> withErrorListener(ErrorListener errorListener) {
        this.errorListener = new WeakReference<ErrorListener>(errorListener);

        if (isDone()) {
            reportError();
        }

        return this;
    }

    public RequestFuture<T> withResultListener(ResultListener<? super T> resultListener) {
        this.resultListener = new WeakReference<ResultListener<? super T>>(resultListener);

        if (isDone()) {
            reportResult();
        }

        return this;
    }

    public RequestFuture<T> withSessionListener(SessionListener sessionListener) {
        this.sessionListener = new WeakReference<SessionListener>(sessionListener);

        if (isDone()) {
            reportSessionChange();
        }

        return this;
    }

    private void extractError(Throwable root) {
        if (root instanceof ExecutionException) {
            Throwable cause = root.getCause();
            error = cause != null ? cause : root;
        } else {
            error = root;
        }
    }

    private RestResult<T> getSilent() {
        try {
            error = null;
            return get();
        } catch (InterruptedException e) {
            extractError(e);
            return null;
        } catch (ExecutionException e) {
            extractError(e);
            return null;
        }

    }

    private void reportError() {
        getSilent();

        final boolean hasError = error != null;
        final boolean hasCustomListener = errorListener != null;
        final boolean hasGlobalListeners = !ERROR_LISTENERS.isEmpty();

        if (hasError && (hasCustomListener || hasGlobalListeners)) {
            new ReportManager<ErrorListener>() {

                @Override
                protected boolean executeReport(WeakReference<ErrorListener> reference) {
                    return reference.get().onErrorOccured(error);
                }

            }.reportToListeners(HANDLER, errorListener, ERROR_LISTENERS);
        }
    }

    private void reportResult() {
        RestResult<T> result = getSilent();

        if (resultListener != null && result != null && error == null) {
            final T item = result != null ? result.getItem() : null;

            new ReportManager<ResultListener<? super T>>() {

                @Override
                protected boolean executeReport(WeakReference<ResultListener<? super T>> reference) {
                    return reference.get().onRequestPerformed(item);
                }

            }.reportToListeners(HANDLER, resultListener, null);
        }
    }

    private void reportSessionChange() {
        final RestResult<T> result = getSilent();

        final boolean hasNewSession = result != null && result.hasSession();
        final boolean hasCustomListener = errorListener != null;
        final boolean hasGlobalListeners = !ERROR_LISTENERS.isEmpty();

        if (hasNewSession && (hasCustomListener || hasGlobalListeners)) {
            final Session session = result.getSession();

            new ReportManager<SessionListener>() {

                @Override
                protected boolean executeReport(WeakReference<SessionListener> reference) {
                    return reference.get().onSessionChanged(session);
                }

            }.reportToListeners(HANDLER, sessionListener, SESSION_LISTENERS);
        }
    }

}
