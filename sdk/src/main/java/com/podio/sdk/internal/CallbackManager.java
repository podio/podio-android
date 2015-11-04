
package com.podio.sdk.internal;

import android.os.Handler;
import android.os.Looper;

import com.podio.sdk.PodioError;
import com.podio.sdk.Request.ErrorListener;
import com.podio.sdk.Request.ResultListener;

import java.util.ArrayList;

public class CallbackManager<T> {
    private static final ArrayList<ErrorListener> GLOBAL_ERROR_LISTENERS;

    static {
        GLOBAL_ERROR_LISTENERS = new ArrayList<ErrorListener>();
    }

    public static ErrorListener addGlobalErrorListener(ErrorListener errorListener) {
        return errorListener != null && GLOBAL_ERROR_LISTENERS.add(errorListener) ?
                errorListener :
                null;
    }

    public static ErrorListener removeGlobalErrorListener(ErrorListener errorListener) {
        int index = GLOBAL_ERROR_LISTENERS.indexOf(errorListener);

        return GLOBAL_ERROR_LISTENERS.contains(errorListener) ?
                GLOBAL_ERROR_LISTENERS.remove(index) :
                null;
    }

    private final ArrayList<ResultListener<T>> resultListeners;
    private final ArrayList<ErrorListener> errorListeners;
    private final Object RESULT_LISTENER_LOCK = new Object();
    private final Object ERROR_LISTENER_LOCK = new Object();

    public CallbackManager() {
        this.resultListeners = new ArrayList<ResultListener<T>>();
        this.errorListeners = new ArrayList<ErrorListener>();
    }

    public void addErrorListener(ErrorListener listener, boolean deliverErrorNow, Throwable error) {
        if (listener != null) {
            if (deliverErrorNow) {
                listener.onErrorOccurred(error);
            } else {
                synchronized (ERROR_LISTENER_LOCK) {
                    errorListeners.add(listener);
                }
            }
        }
    }

    public void addResultListener(ResultListener<T> listener, boolean deliverResultNow, T result) {
        if (listener != null) {
            if (deliverResultNow) {
                listener.onRequestPerformed(result);
            } else {
                synchronized (RESULT_LISTENER_LOCK) {
                    resultListeners.add(listener);
                }
            }
        }
    }

    public void deliverError(Throwable error) {
        // We will not be delivering a result -> clear the listener references.
        synchronized (RESULT_LISTENER_LOCK) {
            resultListeners.clear();
        }

        if (Utils.isEmpty(errorListeners) && Utils.isEmpty(GLOBAL_ERROR_LISTENERS)) {
            throw new PodioError(error);
        }

        synchronized (ERROR_LISTENER_LOCK) {
            boolean isConsumed = false;
            for (ErrorListener listener : errorListeners) {
                if (listener != null) {
                    if (listener.onErrorOccurred(error)) {
                        // The callback consumed the event, stop the bubbling.
                        isConsumed = true;
                        break;
                    }
                }
            }

            errorListeners.clear();
            if (isConsumed) {
                return;
            }
        }

        for (ErrorListener listener : GLOBAL_ERROR_LISTENERS) {
            if (listener != null) {
                if (listener.onErrorOccurred(error)) {
                    // The callback consumed the event, stop the bubbling.
                    return;
                }
            }
        }
    }

    public void deliverErrorOnMainThread(final Throwable error) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                deliverError(error);
            }

        });
    }

    public void deliverResult(T result) {
        // We will not be delivering any error -> clear the listener references.
        synchronized (ERROR_LISTENER_LOCK) {
            errorListeners.clear();
        }

        synchronized (RESULT_LISTENER_LOCK) {
            for (ResultListener<T> listener : resultListeners) {
                if (listener != null) {
                    if (listener.onRequestPerformed(result)) {
                        // The callback consumed the event, stop the bubbling.
                        break;
                    }
                }
            }

            resultListeners.clear();
        }
    }

    public void deliverResultOnMainThread(final T result) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                deliverResult(result);
            }

        });
    }

    public ResultListener<T> removeResultListener(ResultListener<T> listener) {
        synchronized (RESULT_LISTENER_LOCK) {
            if (resultListeners.contains(listener)) {
                int index = resultListeners.indexOf(listener);
                return resultListeners.remove(index);
            }

            return null;
        }
    }

    public ErrorListener removeErrorListener(ErrorListener listener) {
        synchronized (ERROR_LISTENER_LOCK) {
            if (errorListeners.contains(listener)) {
                int index = errorListeners.indexOf(listener);
                return errorListeners.remove(index);
            }

            return null;
        }
    }

}
