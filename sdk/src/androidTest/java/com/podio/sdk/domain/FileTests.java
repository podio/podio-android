/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */

package com.podio.sdk.domain;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

/**
 * @author László Urszuly
 */
public class FileTests extends AndroidTestCase {

    /**
     * Verifies that the members of the {@link File} class can be initialized by
     * parsing JSON.
     * 
     * <pre>
     * 
     * 1. Describe a File domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON string.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testEmbedCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("file_id:1,")
                .append("size:1,")
                .append("description:'DESCRIPTION',")
                .append("hosted_by:'HOSTEDBY',")
                .append("hosted_by_humanized_name:'HOSTEDBYHUMANIZEDNAME',")
                .append("link:'LINK',")
                .append("link_target:'LINKTARGET',")
                .append("mimetype:'MIMETYPE',")
                .append("name:'NAME',")
                .append("perma_link:'PERMALINK',")
                .append("thumbnail_link:'THUMBNAILLINK'")
                .append("}").toString();

        Gson gson = new Gson();
        File file = gson.fromJson(json, File.class);

        assertNotNull(file);
        assertEquals(1, file.getId());
        assertEquals(1, file.getSize());
        assertEquals("DESCRIPTION", file.getDescription());
        assertEquals("HOSTEDBY", file.getHostName());
        assertEquals("HOSTEDBYHUMANIZEDNAME", file.getHumanizedHostName());
        assertEquals("LINK", file.getLink());
        assertEquals("LINKTARGET", file.getLinkTarget());
        assertEquals("MIMETYPE", file.getMimeType());
        assertEquals("NAME", file.getName());
        assertEquals("PERMALINK", file.getPermaLink());
        assertEquals("THUMBNAILLINK", file.getThumbnailLink());
    }

    /**
     * Verifies that the members of the {@link File} class can be initialized to
     * default by instantiation.
     * 
     * <pre>
     * 
     * 1. Create a new File object instance.
     * 
     * 2. Verify that the members have the default values.
     * 
     * </pre>
     */
    public void testEmbedCanBeCreatedFromInstantiation() {
        File file = new File(1);

        assertNotNull(file);
        assertEquals(1, file.getId());
        assertEquals(0, file.getSize());
        assertEquals(null, file.getDescription());
        assertEquals(null, file.getHostName());
        assertEquals(null, file.getHumanizedHostName());
        assertEquals(null, file.getLink());
        assertEquals(null, file.getLinkTarget());
        assertEquals(null, file.getMimeType());
        assertEquals(null, file.getName());
        assertEquals(null, file.getPermaLink());
        assertEquals(null, file.getThumbnailLink());
    }

}
