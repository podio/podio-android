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
public class EmbedTest extends AndroidTestCase {

    /**
     * Verifies that the members of the {@link Embed} class can be initialized
     * by parsing JSON.
     * 
     * <pre>
     * 
     * 1. Describe a Embed domain object as a JSON string.
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
                .append("embed_height:1,")
                .append("embed_id:1,")
                .append("embed_width:1,")
                .append("description:'DESCRIPTION',")
                .append("embed_html:'EMBEDHTML',")
                .append("hostname:'HOSTNAME',")
                .append("original_url:'ORIGINALURL',")
                .append("resolved_url:'RESOLVEDURL',")
                .append("title:'TITLE',")
                .append("url:'URL'")
                .append("}").toString();

        Gson gson = new Gson();
        Embed embed = gson.fromJson(json, Embed.class);

        assertNotNull(embed);
        assertEquals(1, embed.getHeight());
        assertEquals(1, embed.getId());
        assertEquals(1, embed.getWidth());
        assertEquals("DESCRIPTION", embed.getDescription());
        assertEquals("EMBEDHTML", embed.getHtml());
        assertEquals("HOSTNAME", embed.getHostName());
        assertEquals("ORIGINALURL", embed.getOriginalUrl());
        assertEquals("RESOLVEDURL", embed.getResolvedUrl());
        assertEquals("TITLE", embed.getTitle());
        assertEquals("URL", embed.getUrl());
    }

    /**
     * Verifies that the members of the {@link Embed} class can be initialized
     * to default by instantiation.
     * 
     * <pre>
     * 
     * 1. Create a new Embed object instance.
     * 
     * 2. Verify that the members have the default values.
     * 
     * </pre>
     */
    public void testEmbedCanBeCreatedFromInstantiation() {
        Embed embed = new Embed("URL");
        assertNotNull(embed);
        assertEquals(0, embed.getHeight());
        assertEquals(-1, embed.getId());
        assertEquals(0, embed.getWidth());
        assertEquals(null, embed.getDescription());
        assertEquals(null, embed.getHtml());
        assertEquals(null, embed.getHostName());
        assertEquals(null, embed.getOriginalUrl());
        assertEquals(null, embed.getResolvedUrl());
        assertEquals(null, embed.getTitle());
        assertEquals("URL", embed.getUrl());
    }

    /**
     * Verifies that a {@link Embed.Type} enum can be parsed from a JSON string.
     * 
     * <pre>
     * 
     * 1. Describe a simple Embed object as a JSON string. Make sure it
     *      has a 'type' attribute.
     * 
     * 2. Parse the JSON string to a User instance.
     * 
     * 3. Verify that the 'type' attribute has been parsed successfully as an
     *      enum value.
     * 
     * </pre>
     */
    public void testTypeEnumCanBeParsedFromJson() {
        Gson gson = new Gson();
        Embed embed;

        embed = gson.fromJson("{type:'link'}", Embed.class);
        assertEquals(Embed.Type.link, embed.getType());

        embed = gson.fromJson("{}", Embed.class);
        assertEquals(Embed.Type.undefined, embed.getType());

        embed = gson.fromJson("{type:'fdafadsfads'}", Embed.class);
        assertEquals(Embed.Type.undefined, embed.getType());
    }

    /**
     * Verifies that the {@link Embed.Type} enum returns the correct value.
     * 
     * <pre>
     * 
     * 1. Just do it.
     * 
     * </pre>
     */
    public void testValueOfType() {
        assertEquals(Embed.Type.link, Embed.Type.valueOf("link"));
        assertEquals(Embed.Type.undefined, Embed.Type.valueOf("undefined"));

        assertEquals(Embed.Type.link, Enum.valueOf(Embed.Type.class, "link"));
        assertEquals(Embed.Type.undefined, Enum.valueOf(Embed.Type.class, "undefined"));
    }

}
