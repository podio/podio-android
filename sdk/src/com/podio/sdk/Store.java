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
     * Enables means of closing the store. The implementation should free up
     * memory here.
     * 
     * @return The future task which enables hooking in callback listeners.
     */
    public Request<Void> close();

    /**
     * Enables means of completely clearing the store from the system.
     * 
     * @return The future task which enables hooking in callback listeners.
     */
    public Request<Void> destroy();

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
     * @param classOfValue
     *        The Class definition of any disk persisted JSON.
     * @return The future task which enables hooking in callback listeners.
     */
    public <T> Request<T> set(Object key, Object value, Class<T> classOfValue);

    /**
     * Enables means of removing an object with the given key from the store.
     * 
     * @param key
     *        The key of the object to remove.
     * @param classOfValue
     *        The Class definition of any disk persisted JSON.
     * @return The future task which enables hooking in callback listeners.
     */
    public <T> Request<T> remove(Object key, Class<T> classOfValue);

}
