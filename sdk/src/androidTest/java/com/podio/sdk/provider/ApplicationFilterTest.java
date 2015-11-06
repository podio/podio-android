package com.podio.sdk.provider;

import android.net.Uri;
import android.test.AndroidTestCase;

public class ApplicationFilterTest extends AndroidTestCase {

    public void testAllPropertiesIncludedInResultUri() {
        Uri reference = Uri.parse("content://test.uri/app/space/2" //
                + "?include_inactive=true");

        Uri result = new ApplicationProvider.Path() //
                .withSpaceId(2L) //
                .withInactivesIncluded(true) //
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }


    public void testApplicationIdAddedAsPathSegment() {
        Uri reference = Uri.parse("content://test.uri/app/1");
        Uri result = new ApplicationProvider.Path().withApplicationId(1L).buildUri("content", "test.uri");

        assertEquals(reference, result);
    }


    public void testIncludeInactiveFlagAddedAsQueryParameter() {
        Uri reference = Uri.parse("content://test.uri/app?include_inactive=false");
        Uri result = new ApplicationProvider.Path().withInactivesIncluded(false).buildUri("content",
                "test.uri");

        assertEquals(reference, result);
    }


    public void testSpaceIdAddedAsPathSegment() {
        Uri reference = Uri.parse("content://test.uri/app/space/1");
        Uri result = new ApplicationProvider.Path().withSpaceId(1L).buildUri("content", "test.uri");

        assertEquals(reference, result);
    }


    public void testTypeFlagAddedAsQueryParameter() {
        Uri reference = Uri.parse("content://test.uri/app?type=test");
        Uri result = new ApplicationProvider.Path().withType("test").buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

}
