package com.podio.sdk.parser;

import com.google.gson.Gson;
import com.podio.sdk.Parser;
import com.podio.sdk.internal.utils.Utils;

public class JsonToItemParser implements Parser<String> {

    @Override
    public Object parse(String source, Class<?> classOfTarget) {
        Object result = null;

        if (Utils.notEmpty(source)) {
            Gson gson = new Gson();
            result = gson.fromJson(source, classOfTarget);
        }

        return result;
    }

}
