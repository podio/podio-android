
package com.podio.sdk.internal;

import java.util.HashMap;

/**
 * The purpose of this map is to be able to define a default value when there is no corresponding
 * key.
 *
 * @author Tobias Lindberg
 */
public class DefaultHashMap<K, V> extends HashMap<K, V> {

    protected V mDefaultValue;

    public DefaultHashMap(V defaultValue) {
        super();
        mDefaultValue = defaultValue;
    }

    @Override
    public V get(Object k) {
        V v = super.get(k);
        return ((v == null) && !this.containsKey(k)) ? mDefaultValue : v;
    }
}
