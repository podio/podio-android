
package com.podio.sdk.domain;

import java.util.Calendar;
import java.util.TimeZone;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class SpaceTest extends AndroidTestCase {


    public void testSpaceCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("auto_join:true,")
                .append("post_on_new_app:true,")
                .append("post_on_new_member:true,")
                .append("subscribed:true,")
                .append("premium:true,")
                .append("rank:1,")
                .append("space_id:1,")
                .append("rights:['view'],")
                .append("org:{},")
                .append("privacy:'closed',")
                .append("role:'light',")
                .append("created_on:'2014-07-10 10:27:37',")
                .append("description:'DESCRIPTION',")
                .append("name:'NAME',")
                .append("url:'URL',")
                .append("url_label:'URLLABEL',")
                .append("video:'VIDEO',")
                .append("type:'emp_network',")
                .append("created_by:{}")
                .append("}").toString();

        Gson gson = new Gson();
        Space space = gson.fromJson(json, Space.class);

        assertNotNull(space);
        assertEquals(true, space.doAutoJoin());
        assertEquals(true, space.doPostOnNewApp());
        assertEquals(true, space.doPostOnNewMember());
        assertEquals(true, space.isSubscribed());
        assertEquals(true, space.isPremium());
        assertEquals(1, space.getRank());
        assertEquals(1, space.getSpaceId());
        assertEquals(true, space.hasAllRights(Right.view));
        assertNotNull(space.getOrganization());
        assertEquals(Space.Privacy.closed, space.getPrivacy());
        assertEquals(Space.Role.light, space.getRole());
        assertEquals("2014-07-10 10:27:37", space.getCreatedDateString());

        Calendar createdCalendar = Calendar.getInstance();
        createdCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        createdCalendar.setTime(space.getCreatedDate());

        assertEquals(2014, createdCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, createdCalendar.get(Calendar.MONTH));
        assertEquals(10, createdCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(10, createdCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(27, createdCalendar.get(Calendar.MINUTE));
        assertEquals(37, createdCalendar.get(Calendar.SECOND));

        assertEquals("DESCRIPTION", space.getDescription());
        assertEquals("NAME", space.getName());
        assertEquals("URL", space.getUrl());
        assertEquals("URLLABEL", space.getUrlLabel());
        assertEquals("VIDEO", space.getVideoId());
        assertEquals(Space.Type.emp_network, space.getType());
        assertNotNull(space.getCreatedBy());
    }


    public void testSpaceCanBeCreatedFromInstantiation() {
        Space space = Space.newInstance();

        assertNotNull(space);
        assertEquals(false, space.doAutoJoin());
        assertEquals(false, space.doPostOnNewApp());
        assertEquals(false, space.doPostOnNewMember());
        assertEquals(false, space.isSubscribed());
        assertEquals(false, space.isPremium());
        assertEquals(-1, space.getRank());
        assertEquals(-1, space.getSpaceId());
        assertEquals(false, space.hasAllRights(Right.view));
        assertEquals(null, space.getOrganization());
        assertEquals(Space.Privacy.undefined, space.getPrivacy());
        assertEquals(Space.Role.undefined, space.getRole());
        assertEquals(null, space.getCreatedDateString());
        assertEquals(null, space.getCreatedDate());

        assertEquals(null, space.getDescription());
        assertEquals(null, space.getName());
        assertEquals(null, space.getUrl());
        assertEquals(null, space.getUrlLabel());
        assertEquals(null, space.getVideoId());
        assertEquals(Space.Type.undefined, space.getType());
        assertEquals(null, space.getCreatedBy());
    }


    public void testPrivacyEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        Space space;

        space = gson.fromJson("{privacy:'open'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Privacy.open, space.getPrivacy());

        space = gson.fromJson("{}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Privacy.undefined, space.getPrivacy());

        space = gson.fromJson("{status:'fdafadsfads'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Privacy.undefined, space.getPrivacy());
    }


    public void testRoleEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        Space space;

        space = gson.fromJson("{role:'admin'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Role.admin, space.getRole());

        space = gson.fromJson("{}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Role.undefined, space.getRole());

        space = gson.fromJson("{role:'fdafadsfads'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Role.undefined, space.getRole());
    }


    public void testTypeEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        Space space;

        space = gson.fromJson("{type:'regular'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Type.regular, space.getType());

        space = gson.fromJson("{}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Type.undefined, space.getType());

        space = gson.fromJson("{type:'fdafadsfads'}", Space.class);
        assertNotNull(space);
        assertEquals(Space.Type.undefined, space.getType());
    }


    public void testValueOfPrivacy() {
        assertEquals(Space.Privacy.closed, Space.Privacy.valueOf("closed"));
        assertEquals(Space.Privacy.open, Space.Privacy.valueOf("open"));
        assertEquals(Space.Privacy.undefined, Space.Privacy.valueOf("undefined"));

        assertEquals(Space.Privacy.closed, Enum.valueOf(Space.Privacy.class, "closed"));
        assertEquals(Space.Privacy.open, Enum.valueOf(Space.Privacy.class, "open"));
        assertEquals(Space.Privacy.undefined, Enum.valueOf(Space.Privacy.class, "undefined"));
    }


    public void testValueOfRole() {
        assertEquals(Space.Role.admin, Space.Role.valueOf("admin"));
        assertEquals(Space.Role.light, Space.Role.valueOf("light"));
        assertEquals(Space.Role.regular, Space.Role.valueOf("regular"));
        assertEquals(Space.Role.undefined, Space.Role.valueOf("undefined"));

        assertEquals(Space.Role.admin, Enum.valueOf(Space.Role.class, "admin"));
        assertEquals(Space.Role.light, Enum.valueOf(Space.Role.class, "light"));
        assertEquals(Space.Role.regular, Enum.valueOf(Space.Role.class, "regular"));
        assertEquals(Space.Role.undefined, Enum.valueOf(Space.Role.class, "undefined"));
    }


    public void testValueOfType() {
        assertEquals(Space.Type.demo, Space.Type.valueOf("demo"));
        assertEquals(Space.Type.emp_network, Space.Type.valueOf("emp_network"));
        assertEquals(Space.Type.regular, Space.Type.valueOf("regular"));
        assertEquals(Space.Type.undefined, Space.Type.valueOf("undefined"));

        assertEquals(Space.Type.demo, Enum.valueOf(Space.Type.class, "demo"));
        assertEquals(Space.Type.emp_network, Enum.valueOf(Space.Type.class, "emp_network"));
        assertEquals(Space.Type.regular, Enum.valueOf(Space.Type.class, "regular"));
        assertEquals(Space.Type.undefined, Enum.valueOf(Space.Type.class, "undefined"));
    }

}
