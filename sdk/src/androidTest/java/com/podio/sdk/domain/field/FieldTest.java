package com.podio.sdk.domain.field;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class FieldTest extends AndroidTestCase {

    public void testStatusEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        TextField field;

        field = gson.fromJson("{status:'active'}", TextField.class);
        assertEquals(Field.Status.active, field.getStatus());

        field = gson.fromJson("{}", TextField.class);
        assertEquals(Field.Status.undefined, field.getStatus());

        field = gson.fromJson("{status:'fdafadsfads'}", TextField.class);
        assertEquals(Field.Status.undefined, field.getStatus());
    }

    public void testValueOfStatus() {
        assertEquals(Field.Status.active, Field.Status.valueOf("active"));
        assertEquals(Field.Status.deleted, Field.Status.valueOf("deleted"));
        assertEquals(Field.Status.undefined, Field.Status.valueOf("undefined"));

        assertEquals(Field.Status.active, Enum.valueOf(Field.Status.class, "active"));
        assertEquals(Field.Status.deleted, Enum.valueOf(Field.Status.class, "deleted"));
        assertEquals(Field.Status.undefined, Enum.valueOf(Field.Status.class, "undefined"));
    }

}
