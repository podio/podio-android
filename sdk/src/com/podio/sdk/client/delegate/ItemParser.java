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
        String result = null;

        if (item != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.disableHtmlEscaping();
            Gson gson = builder.create();
            result = gson.toJson(item);
        }

        return result;
    }

}
