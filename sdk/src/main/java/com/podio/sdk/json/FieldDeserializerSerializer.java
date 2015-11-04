
package com.podio.sdk.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.podio.sdk.domain.field.CalculationField;
import com.podio.sdk.domain.field.DateField;
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.field.NumberField;
import com.podio.sdk.domain.field.TextField;
import com.podio.sdk.internal.DefaultHashMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO: Investigate the possibilities offered by Gson TypeAdapter instead.
// According to Google documentation it's the preferred way of
// (de)serializing JSON as it's more efficient and has a smaller memory
// footprint.
class FieldDeserializerSerializer implements JsonDeserializer<Field>, JsonSerializer<Field> {

    private Map<CalculationField.ReturnType, Class<? extends Field.Value>> CalculationValueClassesMap;
    private Gson gson;

    public FieldDeserializerSerializer() {
        CalculationValueClassesMap = new DefaultHashMap<CalculationField.ReturnType, Class<? extends Field.Value>>(TextField.Value.class);
        CalculationValueClassesMap.put(CalculationField.ReturnType.text, TextField.Value.class);
        CalculationValueClassesMap.put(CalculationField.ReturnType.date, DateField.Value.class);
        CalculationValueClassesMap.put(CalculationField.ReturnType.number, NumberField.Value.class);

        gson = new Gson();
    }

    @Override
    public Field deserialize(JsonElement element, Type type, JsonDeserializationContext gsonContext) throws JsonParseException {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = element.getAsJsonObject();

        // Ensure that we always have a "values" array, even if it's empty,
        // as this is needed when creating new items.
        if (!jsonObject.has("values")) {
            jsonObject.add("values", new JsonArray());
        }

        Field.Type typeEnum = Field.Type.undefined;
        JsonElement fieldType = jsonObject.get("type");

        if (fieldType != null && !fieldType.isJsonNull()) {
            try {
                typeEnum = Field.Type.valueOf(fieldType.getAsString());
            } catch (IllegalArgumentException e) {
            }
        }

        if (typeEnum == Field.Type.undefined) {
            // Overwrite the type in the json so we get undefined instead of
            // null.
            jsonObject.addProperty("type", Field.Type.undefined.name());
        }

        if (typeEnum == Field.Type.calculation) {
            JsonArray fieldValues = jsonObject.remove("values").getAsJsonArray();
            jsonObject.add("values", new JsonArray());
            Field field = gsonContext.deserialize(jsonObject, typeEnum.getFieldClass());

            if (fieldValues.size() > 0) {
                field.setValues(deserializeCalculationFieldValues(gsonContext, fieldValues, jsonObject));
            }
            return field;
        }

        return gsonContext.deserialize(jsonObject, typeEnum.getFieldClass());
    }

    /**
     * This method will create Value objects of subclass type NumberField.Value, DateField.Value or
     * TextField.Value based on the return_type property of the given JSON object
     *
     * @param gsonContext
     * @param jsonFieldValues
     * @param jsonField
     *
     * @return
     */
    private ArrayList<Field.Value> deserializeCalculationFieldValues(JsonDeserializationContext gsonContext, JsonArray jsonFieldValues, JsonObject jsonField) {
        JsonObject jsonConfig = jsonField.get("config").getAsJsonObject();
        CalculationField.ReturnType returnType = CalculationField.ReturnType.undefined;

        if (jsonConfig != null && !jsonConfig.isJsonNull()) {
            JsonObject jsonSettings = jsonConfig.get("settings").getAsJsonObject();

            if (jsonSettings != null && !jsonSettings.isJsonNull()) {
                returnType = CalculationField.ReturnType.getReturnType(jsonSettings.get("return_type").getAsString());
            }
        }

        ArrayList<Field.Value> deserializedFieldValues = new ArrayList<Field.Value>();

        for (JsonElement jsonFieldValue : jsonFieldValues) {
            deserializedFieldValues.add(gson.fromJson(jsonFieldValue, returnType.getFieldValueClass()));
        }
        return deserializedFieldValues;
    }

    @Override
    public JsonElement serialize(Field field, Type type, JsonSerializationContext gsonContext) {

        if (field.getType() == Field.Type.calculation) {
            return serializeCalculationField(field, gsonContext);
        }

        return gsonContext.serialize(field, field.getType().getFieldClass());
    }

    private JsonElement serializeCalculationField(Field field, JsonSerializationContext gsonContext) {

        List<Field.Value> calculationValues = new ArrayList<>();
        CalculationField calculationField = (CalculationField) field;
        calculationValues.addAll(calculationField.getValues());
        calculationField.setValues(new ArrayList<Field.Value>());

        JsonObject jsonField = gsonContext.serialize(field, field.getType().getFieldClass()).getAsJsonObject();

        if (!calculationValues.isEmpty()) {
            JsonArray jsonCalculationArray = new JsonArray();
            for (Field.Value calculationValue : calculationValues) {
                jsonCalculationArray.add(gson.toJsonTree(calculationValue, calculationField.getConfiguration().getReturnType().getFieldValueClass()));
            }
            jsonField.add("values", jsonCalculationArray);
        }
        return jsonField;
    }

}
