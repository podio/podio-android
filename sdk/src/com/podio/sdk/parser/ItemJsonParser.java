package com.podio.sdk.parser;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.podio.sdk.Parser;

public class ItemJsonParser implements Parser<Object> {

    @Override
    public List<?> parse(Object source, Class<?> classOfTarget) {
        List<String> result = new ArrayList<String>(1);

        if (source != null) {
            Gson gson = new Gson();
            String json = gson.toJson(source);
            result.add(json);
        }

        return result;
    }

}
