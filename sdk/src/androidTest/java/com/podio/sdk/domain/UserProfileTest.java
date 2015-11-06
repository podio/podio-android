package com.podio.sdk.domain;

import java.util.Calendar;
import java.util.TimeZone;

import android.test.AndroidTestCase;

import com.google.gson.Gson;


public class UserProfileTest extends AndroidTestCase {

    public void testUserProfileCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("image:{},")
                .append("avatar:1,")
                .append("org_id:1,")
                .append("profile_id:1,")
                .append("space_id:1,")
                .append("user_id:1,")
                .append("location:['LOCATION'],")
                .append("mail:['MAIL'],")
                .append("phone:['PHONE'],")
                .append("rights:['view'],")
                .append("title:['TITLE'],")
                .append("about:'ABOUT',")
                .append("external_id:'EXTERNALID',")
                .append("link:'LINK',")
                .append("last_seen_on:'2014-07-10 14:13:39',")
                .append("name:'NAME'")
                .append("}").toString();

        Gson gson = new Gson();
        Profile profile = gson.fromJson(json, Profile.class);

        assertNotNull(profile);
        assertNotNull(profile.getImage());
        assertEquals(1L, profile.getOrganizationId());
        assertEquals(1L, profile.getId());
        assertEquals(1L, profile.getWorkspaceId());
        assertEquals(1L, profile.getUserId());

        String[] locations = profile.getLocations();
        assertNotNull(locations);
        assertEquals(1, locations.length);
        assertEquals("LOCATION", locations[0]);

        String[] emails = profile.getEmailAddresses();
        assertNotNull(emails);
        assertEquals(1, emails.length);
        assertEquals("MAIL", emails[0]);

        String[] phones = profile.getPhoneNumbers();
        assertNotNull(phones);
        assertEquals(1, phones.length);
        assertEquals("PHONE", phones[0]);

        String[] titles = profile.getTitles();
        assertNotNull(titles);
        assertEquals(1, titles.length);
        assertEquals("TITLE", titles[0]);

        assertEquals("ABOUT", profile.getAbout());
        assertEquals("EXTERNALID", profile.getExternalId());
        assertEquals("LINK", profile.getLink());
        assertEquals("2014-07-10 14:13:39", profile.getLastSeenDateString());

        Calendar lastSeenCalendar = Calendar.getInstance();
        lastSeenCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        lastSeenCalendar.setTime(profile.getLastSeenDate());

        assertEquals(2014, lastSeenCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, lastSeenCalendar.get(Calendar.MONTH));
        assertEquals(10, lastSeenCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, lastSeenCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(13, lastSeenCalendar.get(Calendar.MINUTE));
        assertEquals(39, lastSeenCalendar.get(Calendar.SECOND));

        assertEquals("NAME", profile.getName());
    }

    public void testListsNeverNull() {
        Gson gson = new Gson();
        Profile profile = gson.fromJson("{}", Profile.class);

        assertEquals(false, profile.hasRight(Right.view));
        assertEquals(0, profile.getLocations().length);
        assertEquals(0, profile.getEmailAddresses().length);
        assertEquals(0, profile.getPhoneNumbers().length);
        assertEquals(0, profile.getTitles().length);
    }
}
