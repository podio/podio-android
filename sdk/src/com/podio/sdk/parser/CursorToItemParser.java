package com.podio.sdk.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.podio.sdk.Parser;
import com.podio.sdk.internal.utils.Utils;
import com.podio.sdk.parser.annotation.StructuredName;

public class CursorToItemParser implements Parser<Cursor> {

    private static final class FieldData {
        private int columnIndex;
        private String fieldName;
        private String columnName;
        private Class<?> type;
    }

    @Override
    public List<?> parse(Cursor source, Class<?> classOfTarget) {
        List<FieldData> fields = getStructuredNames(classOfTarget, source);
        List<?> items = parseCursor(source, classOfTarget, fields);
        return items;
    }

    private <E> ArrayList<FieldData> getStructuredNames(Class<E> classOfTarget, Cursor source) {
        ArrayList<FieldData> result = new ArrayList<FieldData>();

        if (classOfTarget != null && source != null) {
            Field[] fields = classOfTarget.getDeclaredFields();

            if (Utils.notEmpty(fields)) {
                for (Field field : fields) {

                    if (!field.isEnumConstant() && !field.isSynthetic()) {
                        FieldData data = new FieldData();
                        data.fieldName = field.getName();

                        if (field.isAnnotationPresent(StructuredName.class)) {
                            StructuredName annotation = field.getAnnotation(StructuredName.class);
                            data.columnName = annotation.value();
                        } else {
                            data.columnName = data.fieldName;
                        }

                        data.columnIndex = source.getColumnIndex(data.columnName);
                        data.type = field.getType();

                        result.add(data);
                    }
                }
            }
        }

        return result;
    }

    private <E> List<E> parseCursor(Cursor cursor, Class<E> classOfTarget, List<FieldData> fields) {
        List<E> result = new ArrayList<E>();

        if (cursor != null && classOfTarget != null && Utils.notEmpty(fields)) {
            int reset = cursor.getPosition();

            if (cursor.moveToFirst()) {
                do {
                    try {
                        E item = classOfTarget.newInstance();
                        setValues(item, cursor, fields);
                        result.add(item);
                    } catch (InstantiationException e) {
                    } catch (IllegalAccessException e) {
                    }
                } while (cursor.moveToNext());

                cursor.moveToPosition(reset);
            }
        }

        return result;
    }

    private <E> void setValues(E item, Cursor cursor, List<FieldData> fieldData) {
        if (item != null && cursor != null && Utils.notEmpty(fieldData)) {
            for (FieldData data : fieldData) {

                try {
                    int index = data.columnIndex;
                    Class<?> itemClass = item.getClass();
                    Field field = itemClass.getDeclaredField(data.fieldName);
                    field.setAccessible(true);
                    Class<?> fieldClass = data.type;

                    if (index != -1) {
                        if (fieldClass.equals(boolean.class) || fieldClass.equals(Boolean.class)) {
                            String value = cursor.getString(index);
                            boolean isTrue = "true".equalsIgnoreCase(value);
                            field.setBoolean(item, isTrue);
                        } else if (fieldClass.equals(double.class) || fieldClass.equals(Double.class)) {
                            double value = cursor.getDouble(index);
                            field.setDouble(item, value);
                        } else if (fieldClass.equals(float.class) || fieldClass.equals(Float.class)) {
                            float value = cursor.getFloat(index);
                            field.setFloat(item, value);
                        } else if (fieldClass.equals(int.class) || fieldClass.equals(Integer.class)) {
                            int value = cursor.getInt(index);
                            field.setInt(item, value);
                        } else if (fieldClass.equals(long.class) || fieldClass.equals(Long.class)) {
                            long value = cursor.getLong(index);
                            field.setLong(item, value);
                        } else if (fieldClass.equals(short.class) || fieldClass.equals(Short.class)) {
                            short value = cursor.getShort(index);
                            field.setShort(item, value);
                        } else if (fieldClass.equals(String.class)) {
                            String value = cursor.getString(index);
                            field.set(item, value);
                        }
                    }
                } catch (IllegalAccessException e) {
                } catch (IllegalArgumentException e) {
                } catch (NoSuchFieldException e) {
                }
            }
        }
    }
}
