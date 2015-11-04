
package com.podio.sdk;

/**
 * Definition of capabilities for a Store object.
 * 
 */
public interface Store {

    /**
     * Enables means of freeing up memory without affecting the long term store.
     * 
     * @return The future task which enables hooking in callback listeners.
     */
    public Request<Void> free();

    /**
     * Enables means of completely erasing the store from the system.
     * 
     * @return The future task which enables hooking in callback listeners.
     */
    public Request<Void> erase();

    /**
     * Enables means of retrieving an object with the given key from the store.
     * 
     * @param key
     *        The key of the object to retrieve.
     * @param classOfValue
     *        The Class definition of any disk persisted JSON.
     * @return The future task which enables hooking in callback listeners.
     */
    public <T> Request<T> get(Object key, Class<T> classOfValue);

    /**
     * Enables means of adding or replacing an item with the given key in the
     * store.
     * 
     * @param key
     *        The key associated with the object.
     * @param value
     *        The object to cache.
     * @return The future task which enables hooking in callback listeners.
     */
    public Request<Void> set(Object key, Object value);

    /**
     * Enables means of removing an object with the given key from the store.
     * 
     * @param key
     *        The key of the object to remove.
     * @return The future task which enables hooking in callback listeners.
     */
    public Request<Void> remove(Object key);

}
