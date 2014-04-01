package com.podio.sdk.internal.utils;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import android.util.ArrayMap;

public class UtilsTest extends AndroidTestCase {

    public void testCollectionIsEmpty() {
        ArrayList<Object> empty = new ArrayList<Object>();
        ArrayList<Object> nonEmpty = new ArrayList<Object>();
        nonEmpty.add(new Object());

        assertTrue(Utils.isEmpty(empty));
        assertTrue(Utils.isEmpty((ArrayList<Object>) null));
        assertFalse(Utils.isEmpty(nonEmpty));
    }

    public void testCollectionNotEmpty() {
        ArrayList<Object> empty = new ArrayList<Object>();
        ArrayList<Object> nonEmpty = new ArrayList<Object>();
        nonEmpty.add(new Object());

        assertFalse(Utils.notEmpty(empty));
        assertFalse(Utils.notEmpty((ArrayList<Object>) null));
        assertTrue(Utils.notEmpty(nonEmpty));
    }

    public void testMapIsEmpty() {
        ArrayMap<Object, Object> empty = new ArrayMap<Object, Object>();
        ArrayMap<Object, Object> nonEmpty = new ArrayMap<Object, Object>();
        nonEmpty.put(new Object(), new Object());

        assertTrue(Utils.isEmpty(empty));
        assertTrue(Utils.isEmpty((ArrayMap<Object, Object>) null));
        assertFalse(Utils.isEmpty(nonEmpty));
    }

    public void testIntArrayIsEmpty() {
        assertTrue(Utils.isEmpty(new int[] {}));
        assertTrue(Utils.isEmpty(new int[0]));
        assertTrue(Utils.isEmpty((int[]) null));
        assertFalse(Utils.isEmpty(new int[] { 0 }));
    }

    public void testIntArrayNotEmpty() {
        assertFalse(Utils.notEmpty(new int[] {}));
        assertFalse(Utils.notEmpty(new int[0]));
        assertFalse(Utils.notEmpty((int[]) null));
        assertTrue(Utils.notEmpty(new int[] { 0 }));
    }

    public void testLongArrayIsEmpty() {
        assertTrue(Utils.isEmpty(new long[] {}));
        assertTrue(Utils.isEmpty(new long[0]));
        assertTrue(Utils.isEmpty((long[]) null));
        assertFalse(Utils.isEmpty(new long[] { 0L }));
    }

    public void testLongArrayNotEmpty() {
        assertFalse(Utils.notEmpty(new long[] {}));
        assertFalse(Utils.notEmpty(new long[0]));
        assertFalse(Utils.notEmpty((long[]) null));
        assertTrue(Utils.notEmpty(new long[] { 0L }));
    }

    public void testMapNotEmpty() {
        ArrayMap<Object, Object> empty = new ArrayMap<Object, Object>();
        ArrayMap<Object, Object> nonEmpty = new ArrayMap<Object, Object>();
        nonEmpty.put(new Object(), new Object());

        assertFalse(Utils.notEmpty(empty));
        assertFalse(Utils.notEmpty((ArrayMap<Object, Object>) null));
        assertTrue(Utils.notEmpty(nonEmpty));
    }

    public void testObjectArrayIsEmpty() {
        assertTrue(Utils.isEmpty(new Object[] {}));
        assertTrue(Utils.isEmpty(new Object[0]));
        assertTrue(Utils.isEmpty((Object[]) null));
        assertFalse(Utils.isEmpty(new Object[] { new Object() }));
        assertFalse(Utils.isEmpty(new Object[] { null }));
    }

    public void testObjectArrayNotEmpty() {
        assertFalse(Utils.notEmpty(new Object[] {}));
        assertFalse(Utils.notEmpty(new Object[0]));
        assertFalse(Utils.notEmpty((Object[]) null));
        assertTrue(Utils.notEmpty(new Object[] { new Object() }));
        assertTrue(Utils.notEmpty(new Object[] { null }));
    }

    public void testStringIsEmpty() {
        assertTrue(Utils.isEmpty(""));
        assertTrue(Utils.isEmpty((String) null));
        assertFalse(Utils.isEmpty("test"));
        assertFalse(Utils.isEmpty(" "));
    }

    public void testStringNotEmpty() {
        assertFalse(Utils.notEmpty(""));
        assertFalse(Utils.notEmpty((String) null));
        assertTrue(Utils.notEmpty("test"));
        assertTrue(Utils.notEmpty(" "));
    }
}
