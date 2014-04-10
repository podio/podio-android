package com.podio.sdk.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.podio.sdk.Parser;

public class ItemToJsonParser implements Parser<Object> {

    @Override
    public String parse(Object source, Class<?> classOfTarget) {
        String result = null;

        if (source != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.disableHtmlEscaping();
            Gson gson = builder.create();
            result = gson.toJson(source);
        }

        return result != null ? result : "{}";
    }

}
