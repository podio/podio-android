package com.podio.sdk.parser;

import java.util.List;

import android.test.AndroidTestCase;

import com.podio.sdk.domain.Item;
import com.podio.sdk.domain.field.CalculationField;
import com.podio.sdk.domain.field.CategoryField;
import com.podio.sdk.domain.field.ContactField;
import com.podio.sdk.domain.field.DateField;
import com.podio.sdk.domain.field.DurationField;
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.field.ImageField;
import com.podio.sdk.domain.field.LinkField;
import com.podio.sdk.domain.field.LocationField;
import com.podio.sdk.domain.field.MoneyField;
import com.podio.sdk.domain.field.NumberField;
import com.podio.sdk.domain.field.ProgressField;
import com.podio.sdk.domain.field.RelationshipField;
import com.podio.sdk.domain.field.TextField;
import com.podio.sdk.domain.field.UndefinedField;
import com.podio.sdk.json.JsonParser;

public class JsonParserTest extends AndroidTestCase {

    private void assertContent(Item item, Class cls, Field.Type typeEnum) {
        assertNotNull(item);

        List<Field> fields = item.getFields();
        assertNotNull(fields);
        assertEquals(1, fields.size());

        Field field = fields.get(0);
        assertNotNull(field);
        assertEquals(field.getClass(), cls);
        assertEquals(typeEnum, field.getType());


    }

    public void testParseJsonStringToApplicationReferenceField() {
        String json = "{fields:[{type:'app'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, RelationshipField.class, Field.Type.app);
    }

    public void testParseJsonStringToCalculationField() {
        String json = "{fields:[{type:'calculation'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, CalculationField.class, Field.Type.calculation);

    }

    public void testParseJsonStringToCategoryField() {
        String json = "{fields:[{type:'category'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, CategoryField.class, Field.Type.category);
    }

    public void testParseJsonStringToContactField() {
        String json = "{fields:[{type:'contact'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, ContactField.class, Field.Type.contact);
    }

    public void testParseJsonStringToDateField() {
        String json = "{fields:[{type:'date'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, DateField.class, Field.Type.date);
    }

    public void testParseJsonStringToDurationField() {
        String json = "{fields:[{type:'duration'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, DurationField.class, Field.Type.duration);
    }

    public void testParseJsonStringToLinkField() {
        String json = "{fields:[{type:'embed'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, LinkField.class, Field.Type.embed);
    }

    public void testParseJsonStringToEmptyFieldWhenNullPointerType() {
        String json = "{fields:[{type:null}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, UndefinedField.class, Field.Type.undefined);
    }

    public void testParseJsonStringToEmptyFieldWhenUndefinedType() {
        String json = "{fields:[{field_id:1}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, UndefinedField.class, Field.Type.undefined);
    }

    public void testParseJsonStringToEmptyFieldWhenUnknownType() {
        String json = "{fields:[{type:'bla'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, UndefinedField.class, Field.Type.undefined);
    }

    public void testParseJsonStringToImageField() {
        String json = "{fields:[{type:'image'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, ImageField.class, Field.Type.image);
    }

    public void testParseJsonStringToLocationField() {
        String json = "{fields:[{type:'location'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, LocationField.class, Field.Type.location);
    }

    public void testParseJsonStringToMoneyField() {
        String json = "{fields:[{type:'money'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, MoneyField.class, Field.Type.money);
    }

    public void testParseJsonStringToNumberField() {
        String json = "{fields:[{type:'number'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, NumberField.class, Field.Type.number);
    }

    public void testParseJsonStringToProgressField() {
        String json = "{fields:[{type:'progress'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, ProgressField.class, Field.Type.progress);
    }


    public void testParseJsonStringToTextField() {
        String json = "{fields:[{type:'text'}]}";
        Item item = JsonParser.fromJson(json, Item.class);

        assertContent(item, TextField.class, Field.Type.text);
    }

}
