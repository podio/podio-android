package com.podio.sdk.domain;

import java.util.Calendar;
import java.util.TimeZone;
import android.test.AndroidTestCase;
import com.google.gson.Gson;

public class UserTest extends AndroidTestCase {


    public void testUserCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("user_id:1,")
                .append("mails:[{}],")
                .append("status:'active',")
                .append("created_on:'2014-07-10 13:45:57',")
                .append("locale:'LOCALE',")
                .append("mail:'MAIL',")
                .append("timezone:'TIMEZONE'")
                .append("}").toString();

        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);

        assertNotNull(user);
        assertEquals(1, user.getUserId());

        User.Email[] emails = user.getEmails();
        assertNotNull(emails);
        assertEquals(1, emails.length);
        assertEquals(User.Status.active, user.getStatus());

        assertEquals("2014-07-10 13:45:57", user.getCreatedDateString());
        Calendar activatedCalendar = Calendar.getInstance();
        activatedCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        activatedCalendar.setTime(user.getCreatedDate());

        assertEquals(2014, activatedCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, activatedCalendar.get(Calendar.MONTH));
        assertEquals(10, activatedCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(13, activatedCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(45, activatedCalendar.get(Calendar.MINUTE));
        assertEquals(57, activatedCalendar.get(Calendar.SECOND));

        Calendar createdCalendar = Calendar.getInstance();
        createdCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        createdCalendar.setTime(user.getCreatedDate());

        assertEquals(2014, createdCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, createdCalendar.get(Calendar.MONTH));
        assertEquals(10, createdCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(13, createdCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(45, createdCalendar.get(Calendar.MINUTE));
        assertEquals(57, createdCalendar.get(Calendar.SECOND));

        assertEquals("LOCALE", user.getLocale());
        assertEquals("MAIL", user.getEmailAddress());
        assertEquals("TIMEZONE", user.getTimezone());
    }

    public void testStatusEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        User user;

        user = gson.fromJson("{status:'blocked'}", User.class);
        assertNotNull(user);
        assertEquals(User.Status.blocked, user.getStatus());

        user = gson.fromJson("{}", User.class);
        assertNotNull(user);
        assertEquals(User.Status.unknown, user.getStatus());

        user = gson.fromJson("{status:'fdafadsfads'}", User.class);
        assertNotNull(user);
        assertEquals(User.Status.unknown, user.getStatus());
    }

    public void testValueOfStatus() {
        assertEquals(User.Status.inactive, User.Status.valueOf("inactive"));
        assertEquals(User.Status.active, User.Status.valueOf("active"));
        assertEquals(User.Status.deleted, User.Status.valueOf("deleted"));
        assertEquals(User.Status.blocked, User.Status.valueOf("blocked"));
        assertEquals(User.Status.unknown, User.Status.valueOf("unknown"));

        assertEquals(User.Status.inactive, Enum.valueOf(User.Status.class, "inactive"));
        assertEquals(User.Status.active, Enum.valueOf(User.Status.class, "active"));
        assertEquals(User.Status.deleted, Enum.valueOf(User.Status.class, "deleted"));
        assertEquals(User.Status.blocked, Enum.valueOf(User.Status.class, "blocked"));
        assertEquals(User.Status.unknown, Enum.valueOf(User.Status.class, "unknown"));
    }

}
