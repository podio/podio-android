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

package com.podio.sdk.client.delegate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.podio.sdk.internal.utils.Utils;

/**
 * Defines a parser class to convert json data to and from domain model data
 * structures.
 * 
 * @param <T>
 *            The type of the domain model data structure.
 * 
 * @author László Urszuly
 * 
 */
public class ItemParser<T> {

    private final Class<T> classOfItem;

    public ItemParser(Class<T> classOfItem) {
        this.classOfItem = classOfItem;
    }

    /**
     * Performs the parsing of the given json string to domain model
     * representation.
     * 
     * @param json
     *            The json string to parse.
     * @return A domain model representation of the given json string.
     */
    public T parseToItem(String source) {
        T result = null;

        if (Utils.notEmpty(source)) {
            Gson gson = new Gson();
            result = gson.fromJson(source, classOfItem);
        }

        return result;
    }

    /**
     * Performs the parsing of the given domain model object to a json string.
     * 
     * @param item
     *            The item to parse.
     * @return A json string representation of the given item.
     */
    public String parseToJson(Object item) {
        String result = "";

        if (item != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.disableHtmlEscaping();
            Gson gson = builder.create();
            result = gson.toJson(item);
        }

        return result;
    }

}
