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
 * An interface defining the protocol for switching domain data protocols back
 * and forth. An example would be how to parse JSON data into Podio domain
 * objects and, vice versa, turn Podio domain objects into JSON strings.
 * 
 * @author László Urszuly
 * @param <T1>
 *        The domain object protocol (e.g. Podio Item, App etc).
 * @param <T2>
 *        The data transfer protocol (e.g. JSON, Cursor etc)
 */
public interface Parser<T1, T2> {

    /**
     * Takes the given source data (e.g. a JSON string or a Cursor object) and
     * creates a new domain object (e.g. a Podio Item or App) of the given type
     * from it.
     * 
     * @param source
     *        The source data.
     * @return The resulting domain object.
     */
    public T1 read(T2 source);

    /**
     * Takes the given domain data object (e.g. a Podio Item or App) and creates
     * a new data transfer domain object of (e.g. a JSON string or a Cursor
     * object).
     * 
     * @param target
     *        The domain object.
     * @return The resulting data transfer domain object.
     */
    public T2 write(Object target);
}
