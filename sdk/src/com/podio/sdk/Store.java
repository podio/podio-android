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
package com.podio.sdk;

/**
 * Definition of capabilities for a Store object.
 * 
 * @author László Urszuly
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
