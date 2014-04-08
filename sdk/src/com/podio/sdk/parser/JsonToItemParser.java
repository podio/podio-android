package com.podio.sdk.parser;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.podio.sdk.Parser;
import com.podio.sdk.internal.utils.Utils;

public class JsonToItemParser implements Parser<String> {

    @Override
    public List<?> parse(String source, Class<?> classOfTarget) {
        List<Object> result = new ArrayList<Object>();

        if (Utils.notEmpty(source)) {
            Gson gson = new Gson();
            Object item = gson.fromJson(source, classOfTarget);
            result.add(item);
        }

        return result;
    }

}
