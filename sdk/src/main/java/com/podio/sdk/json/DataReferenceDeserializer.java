package com.podio.sdk.json;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.podio.sdk.domain.Application;
import com.podio.sdk.domain.DataReference;
import com.podio.sdk.domain.File;
import com.podio.sdk.domain.Item;
import com.podio.sdk.domain.ReferenceType;
import com.podio.sdk.domain.Space;
import com.podio.sdk.domain.Status;
import com.podio.sdk.domain.Task;
import com.podio.sdk.domain.data.Data;
import com.podio.sdk.domain.data.UnknownData;
import com.podio.sdk.internal.DefaultHashMap;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Resolves the reference object received in a URL resolver API call in the references API area
 *
 * @author rabie
 */
public class DataReferenceDeserializer implements JsonDeserializer<DataReference> {

    private Map<ReferenceType, Class<? extends Data>> dataClassesMap;

    public DataReferenceDeserializer() {
        dataClassesMap = new DefaultHashMap<ReferenceType, Class<? extends Data>>(UnknownData.class);
        dataClassesMap.put(ReferenceType.item, Item.class);
        dataClassesMap.put(ReferenceType.space, Space.class);
        dataClassesMap.put(ReferenceType.status, Status.class);
        dataClassesMap.put(ReferenceType.app, Application.class);
        dataClassesMap.put(ReferenceType.task, Task.class);
        dataClassesMap.put(ReferenceType.file, File.class);
    }

    @Override
    public DataReference deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();
        ReferenceType referenceType = ReferenceType.getType(jsonObject.get("type").getAsString());
        String title = jsonObject.get("title").getAsString();
        long id = jsonObject.get("id").getAsLong();
        Gson gson = new Gson();
        Data data = gson.fromJson(jsonObject.get("data"), dataClassesMap.get(referenceType));

        DataReference reference = new DataReference(title, data, referenceType.name(), id);

        return reference;
    }
}
