package com.podio.sdk.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import android.net.Uri;
import android.test.AndroidTestCase;

public class UtilsTest extends AndroidTestCase {

    public void testFormatDateTime() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = dateFormat.parse("2014-07-07");
        String dateString = Utils.formatDateTimeUtc(date);
        assertEquals("2014-07-07 00:00:00", dateString);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date time = timeFormat.parse("21:14:59");
        String timeString = Utils.formatDateTimeUtc(time);
        assertEquals("1970-01-01 21:14:59", timeString);
    }

    public void testGetNativeBoolean() {
        Boolean value = Boolean.TRUE;
        assertEquals(true, Utils.getNative(value, false));
        assertEquals(true, Utils.getNative(null, true));
    }

    public void testGetNativeInteger() {
        Integer value = Integer.valueOf(1);
        assertEquals(1, Utils.getNative(value, 0));
        assertEquals(1, Utils.getNative((Integer) null, 1));
    }

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

    public void testUriIsEmpty() {
        Uri empty = Uri.EMPTY;
        Uri nonEmpty = Uri.parse("test://uri");

        assertTrue(Utils.isEmpty(empty));
        assertTrue(Utils.isEmpty((Uri) null));
        assertFalse(Utils.isEmpty(nonEmpty));
    }

    public void testUriNotEmpty() {
        Uri empty = Uri.EMPTY;
        Uri nonEmpty = Uri.parse("test://uri");

        assertFalse(Utils.notEmpty(empty));
        assertFalse(Utils.notEmpty((Uri) null));
        assertTrue(Utils.notEmpty(nonEmpty));
    }

    public void testMapIsEmpty() {
        HashMap<Object, Object> empty = new HashMap<Object, Object>();
        HashMap<Object, Object> nonEmpty = new HashMap<Object, Object>();
        nonEmpty.put(new Object(), new Object());

        assertTrue(Utils.isEmpty(empty));
        assertTrue(Utils.isEmpty((HashMap<Object, Object>) null));
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
        HashMap<Object, Object> empty = new HashMap<Object, Object>();
        HashMap<Object, Object> nonEmpty = new HashMap<Object, Object>();
        nonEmpty.put(new Object(), new Object());

        assertFalse(Utils.notEmpty(empty));
        assertFalse(Utils.notEmpty((HashMap<Object, Object>) null));
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

    public void testParseDate() {
        assertNull(Utils.parseDateUtc("unparsable"));
        assertNull(Utils.parseDateUtc(null));

        Date date = Utils.parseDateUtc("2014-07-07 15:22:32");
        assertNotNull(date);

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.setTime(date);

        assertEquals(2014, c.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, c.get(Calendar.MONTH));
        assertEquals(7, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, c.get(Calendar.MINUTE));
        assertEquals(0, c.get(Calendar.SECOND));
    }

    public void testParseDateTime() {
        assertNull(Utils.parseDateTimeUtc("unparsable"));
        assertNull(Utils.parseDateTimeUtc(null));

        Date date = Utils.parseDateTimeUtc("2014-07-07 15:22:32");
        assertNotNull(date);

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.setTime(date);

        assertEquals(2014, c.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, c.get(Calendar.MONTH));
        assertEquals(7, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(22, c.get(Calendar.MINUTE));
        assertEquals(32, c.get(Calendar.SECOND));
    }

    public void testParseTime() {
        assertNull(Utils.parseTimeUtc("unparsable"));
        assertNull(Utils.parseTimeUtc(null));

        Date date = Utils.parseTimeUtc("15:22:32");
        assertNotNull(date);

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.setTime(date);

        assertEquals(1970, c.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, c.get(Calendar.MONTH));
        assertEquals(1, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(22, c.get(Calendar.MINUTE));
        assertEquals(32, c.get(Calendar.SECOND));
    }

    public void testStringIsAnyEmpty() {
        assertTrue(Utils.isAnyEmpty((String[]) null));
        assertTrue(Utils.isAnyEmpty(new String[] {}));

        assertTrue(Utils.isAnyEmpty(""));
        assertTrue(Utils.isAnyEmpty((String) null));

        assertTrue(Utils.isAnyEmpty("test1", "", "test3"));
        assertTrue(Utils.isAnyEmpty("test1", null, "test3"));

        assertFalse(Utils.isAnyEmpty(" "));
        assertFalse(Utils.isAnyEmpty("test1", "test2", "test3"));
        assertFalse(Utils.isAnyEmpty("test1", " ", "test3"));
    }

    public void testStringIsEmpty() {
        assertTrue(Utils.isEmpty(""));
        assertTrue(Utils.isEmpty((String) null));
        assertFalse(Utils.isEmpty("test"));
        assertFalse(Utils.isEmpty(" "));
    }

    public void testStringNotAnyEmpty() {
        assertFalse(Utils.notAnyEmpty((String[]) null));
        assertFalse(Utils.notAnyEmpty(new String[] {}));

        assertFalse(Utils.notAnyEmpty(""));
        assertFalse(Utils.notAnyEmpty((String) null));

        assertFalse(Utils.notAnyEmpty("test1", "", "test3"));
        assertFalse(Utils.notAnyEmpty("test1", null, "test3"));

        assertTrue(Utils.notAnyEmpty(" "));
        assertTrue(Utils.notAnyEmpty("test1", "test2", "test3"));
        assertTrue(Utils.notAnyEmpty("test1", " ", "test3"));
    }

    public void testStringNotEmpty() {
        assertFalse(Utils.notEmpty(""));
        assertFalse(Utils.notEmpty((String) null));
        assertTrue(Utils.notEmpty("test"));
        assertTrue(Utils.notEmpty(" "));
    }
}
