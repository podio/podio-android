package com.podio.sdk.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

import com.podio.sdk.Parser;
import com.podio.sdk.internal.utils.Utils;
import com.podio.sdk.parser.annotation.StructuredName;

public class ItemToContentValuesParser implements Parser<Object> {

    @Override
    public List<ContentValues> parse(Object content, Class<?> classOfItem) {
        List<Field> fields = getStructuredNames(classOfItem);
        List<ContentValues> result = parseItems(content, classOfItem, fields);
        return result;
    }

    private ArrayList<Field> getStructuredNames(Class<?> classOfTarget) {
        ArrayList<Field> result = new ArrayList<Field>();

        if (classOfTarget != null) {
            Field[] fields = classOfTarget.getDeclaredFields();

            if (Utils.notEmpty(fields)) {
                for (Field field : fields) {
                    if (!field.isEnumConstant() && !field.isSynthetic()) {
                        result.add(field);
                    }
                }
            }
        }

        return result;
    }

    private List<ContentValues> parseItems(Object content, Class<?> classOfItem, List<Field> fields) {
        List<ContentValues> result = new ArrayList<ContentValues>();

        if (content != null && classOfItem != null && classOfItem.isInstance(content) && Utils.notEmpty(fields)) {
            ContentValues contentValues = new ContentValues();
            setValues(contentValues, content, fields);
            result.add(contentValues);
        }

        return result;
    }

    private void setValues(ContentValues values, Object item, List<Field> fields) {
        if (values != null && item != null && Utils.notEmpty(fields)) {
            for (Field field : fields) {
                if (field != null) {
                    try {
                        Object value = field.get(item);

                        if (value != null) {
                            String name;

                            if (field.isAnnotationPresent(StructuredName.class)) {
                                StructuredName annotation = field.getAnnotation(StructuredName.class);
                                name = annotation != null ? annotation.value() : null;
                            } else {
                                name = field.getName();
                            }

                            Class<?> fieldClass = field.getType();

                            if (fieldClass.equals(boolean.class) || fieldClass.equals(Boolean.class)) {
                                values.put(name, (Boolean) value);
                            } else if (fieldClass.equals(double.class) || fieldClass.equals(Double.class)) {
                                values.put(name, (Double) value);
                            } else if (fieldClass.equals(float.class) || fieldClass.equals(Float.class)) {
                                values.put(name, (Float) value);
                            } else if (fieldClass.equals(int.class) || fieldClass.equals(Integer.class)) {
                                values.put(name, (Integer) value);
                            } else if (fieldClass.equals(long.class) || fieldClass.equals(Long.class)) {
                                values.put(name, (Long) value);
                            } else if (fieldClass.equals(short.class) || fieldClass.equals(Short.class)) {
                                values.put(name, (Short) value);
                            } else if (fieldClass.equals(String.class)) {
                                values.put(name, (String) value);
                            }
                        }
                    } catch (IllegalAccessException e) {
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        }
    }
}
