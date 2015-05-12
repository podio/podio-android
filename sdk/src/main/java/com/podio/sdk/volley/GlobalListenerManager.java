package com.podio.sdk.volley;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rabie
 */
public class GlobalListenerManager<T> {
    private final ArrayList<T> globalListeners = new ArrayList<T>();

    public T removeGlobalListener(T listener){
        int index = globalListeners.indexOf(listener);

        return globalListeners.contains(listener) ?
                globalListeners.remove(index) :
                null;
    }

    public T addGlobalListener(T listener){
        return listener != null && globalListeners.add(listener) ?
                listener :
                null;
    }

    public List<T> getGlobalErrorListeners() {
        //TODO is the return type ok?
        return globalListeners;
    }
}
