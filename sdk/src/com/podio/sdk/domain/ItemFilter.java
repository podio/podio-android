package com.podio.sdk.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.podio.sdk.Filter;

public abstract class ItemFilter implements Filter {
    private final Map<String, List<String>> query = new HashMap<String, List<String>>();

    @Override
    public Map<String, List<String>> getQueryParameters() {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        result.putAll(query);
        return result;
    }

    @Override
    public void addQueryParameter(String key, String value) {
        if (key != null && key.length() > 0 && value != null) {
            List<String> values;

            if (query.containsKey(key)) {
                values = query.get(key);
            } else {
                values = new ArrayList<String>();
                query.put(key, values);
            }

            values.add(value);
        }
    }
}
