package com.podio.sdk.provider;

import android.net.Uri;
import android.test.AndroidTestCase;

public class OrganizationFilterTest extends AndroidTestCase {

    public void testOrganizationPathIsIncludedInResultUri() {
        Uri reference = Uri.parse("content://test.uri/org");

        Uri result = new OrganizationProvider.Path()
                .buildUri("content", "test.uri");

        assertEquals(reference, result);
    }

}
