package com.podio.sdk.domain;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class UserEmailTest extends AndroidTestCase {

    public void testUserEmailCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("disabled:true,")
                .append("primary:true,")
                .append("verified:true,")
                .append("mail:'MAIL'")
                .append("}").toString();

        Gson gson = new Gson();
        User.Email email = gson.fromJson(json, User.Email.class);

        assertNotNull(email);
        assertEquals(true, email.isDisabled());
        assertEquals(true, email.isPrimary());
        assertEquals(true, email.isVerified());
        assertEquals("MAIL", email.getAddress());
    }

}
