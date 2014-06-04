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

package com.podio.sdk;

import android.test.AndroidTestCase;

import com.podio.sdk.domain.Item;
import com.podio.sdk.domain.field.ApplicationReference;
import com.podio.sdk.domain.field.CalculationField;
import com.podio.sdk.domain.field.Category;
import com.podio.sdk.domain.field.ContactField;
import com.podio.sdk.domain.field.DateField;
import com.podio.sdk.domain.field.DurationField;
import com.podio.sdk.domain.field.EmbedField;
import com.podio.sdk.domain.field.EmptyField;
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.field.ImageField;
import com.podio.sdk.domain.field.LocationField;
import com.podio.sdk.domain.field.MoneyField;
import com.podio.sdk.domain.field.NumberField;
import com.podio.sdk.domain.field.ProgressField;
import com.podio.sdk.domain.field.Text;
import com.podio.sdk.domain.field.TitleField;

public class PodioParserTest extends AndroidTestCase {

    /**
     * Verifies that the {@link PodioParser} doesn't crash when trying to parse
     * an empty domain object to a JSON string, but rather returns a null
     * pointer result.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the parser.
     * 
     * 2. Try to parse a null pointer object and verify that null is returned.
     * 
     * </pre>
     */
    public void testParseEmptyDomainObjectToJsonStringDoesNotCrashParser() {
        PodioParser<Object> parser = new PodioParser<Object>(Object.class);

        assertNull(parser.parseToJson(null));
    }

    /**
     * Verifies that the {@link PodioParser} doesn't crash when trying to parse
     * an empty JSON string to a domain object, but rather returns a null
     * pointer result.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the parser.
     * 
     * 2. Try to parse a null pointer String and verify that null is returned.
     * 
     * 3. Try to parse a zero-length String and verify that null is returned.
     * 
     * 4. Try to parse a non-zero length String with white spaces and verify
     *      that null is returned.
     * 
     * </pre>
     */
    public void testParseEmptyJsonStringToDomainObjectDoesNotCrashParser() {
        PodioParser<Object> parser = new PodioParser<Object>(Object.class);

        assertNull(parser.parseToItem(null));
        assertNull(parser.parseToItem(""));
        assertNull(parser.parseToItem(" "));
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link ApplicationReference} field
     *      as a JSON string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToApplicationReferenceField() {
        String json = "{fields:[{type:'app'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof ApplicationReference);
        assertEquals(Field.Type.app, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link CalculationField} field as a
     *      JSON string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToCalculationField() {
        String json = "{fields:[{type:'calculation'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof CalculationField);
        assertEquals(Field.Type.calculation, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link Category} field as a JSON
     *      string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToCategoryField() {
        String json = "{fields:[{type:'category'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof Category);
        assertEquals(Field.Type.category, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link ContactField} field as a JSON
     *      string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToContactField() {
        String json = "{fields:[{type:'contact'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof ContactField);
        assertEquals(Field.Type.contact, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link DateField} field as a JSON
     *      string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToDateField() {
        String json = "{fields:[{type:'date'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof DateField);
        assertEquals(Field.Type.date, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link DurationField} field as a
     *      JSON string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToDurationField() {
        String json = "{fields:[{type:'duration'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof DurationField);
        assertEquals(Field.Type.duration, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link EmbedField} field as a JSON
     *      string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToEmbedField() {
        String json = "{fields:[{type:'embed'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof EmbedField);
        assertEquals(Field.Type.embed, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns a {@link Text} when no type is
     * defined.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with a null pointer field as a JSON string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToEmptyFieldWhenNullPointerType() {
        String json = "{fields:[{type:null}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof EmptyField);
        assertEquals(Field.Type.undefined, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns a {@link Text} when no type is
     * defined.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an undefined field type as a JSON
     *      string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToEmptyFieldWhenUndefinedType() {
        String json = "{fields:[{field_id:1}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof EmptyField);
        assertEquals(Field.Type.undefined, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an unknown field type as a JSON string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToEmptyFieldWhenUnknownType() {
        String json = "{fields:[{type:'bla'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof EmptyField);
        assertEquals(Field.Type.undefined, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link ImageField} field as a JSON
     *      string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToImageField() {
        String json = "{fields:[{type:'image'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof ImageField);
        assertEquals(Field.Type.image, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link LocationField} field as a
     *      JSON string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToLocationField() {
        String json = "{fields:[{type:'location'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof LocationField);
        assertEquals(Field.Type.location, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link MoneyField} field as a JSON
     *      string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToMoneyField() {
        String json = "{fields:[{type:'money'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof MoneyField);
        assertEquals(Field.Type.money, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link NumberField} field as a JSON
     *      string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToNumberField() {
        String json = "{fields:[{type:'number'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof NumberField);
        assertEquals(Field.Type.number, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link ProgressField} field as a
     *      JSON string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToProgressField() {
        String json = "{fields:[{type:'progress'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof ProgressField);
        assertEquals(Field.Type.progress, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link Text} field as a JSON
     *      string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToTextField() {
        String json = "{fields:[{type:'text'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof Text);
        assertEquals(Field.Type.text, field.type);
    }

    /**
     * Verifies that the {@link PodioParser} performs a pre-validation of the
     * "type" Field JSON attribute and returns the corresponding type of field.
     * 
     * <pre>
     * 
     * 1. Describe an {@link Item} with an {@link TitleField} field as a JSON
     *      string.
     * 
     * 2. Try to parse the JSON.
     * 
     * 3. Verify that the correct domain object field type is returned for the
     *      parsed item.
     * 
     * </pre>
     */
    public void testParseJsonStringToTitleField() {
        String json = "{fields:[{type:'title'}]}";
        PodioParser<Item> parser = new PodioParser<Item>(Item.class);
        Item item = parser.parseToItem(json);

        assertNotNull(item);
        assertNotNull(item.fields);
        assertEquals(1, item.fields.size());

        Field field = item.fields.get(0);
        assertNotNull(field);
        assertTrue(field instanceof TitleField);
        assertEquals(Field.Type.title, field.type);
    }
}
