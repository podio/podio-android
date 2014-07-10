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
public class ClientTests extends AndroidTestCase {

    /**
     * Verifies that the members of the {@link Client} class can be initialized
     * by parsing JSON.
     * 
     * <pre>
     * 
     * 1. Describe a Client domain object as a JSON string.
     * 
     * 2. Create an instance from the JSON string.
     * 
     * 3. Verify that the parsed object has the expected values for
     *      its members.
     * 
     * </pre>
     */
    public void testClientCanBeCreatedFromJson() {
        String json = new StringBuilder("{")
                .append("display:true,")
                .append("auth_client_id:1,")
                .append("id:1,")
                .append("name:'NAME',")
                .append("url:'URL'")
                .append("}").toString();

        Gson gson = new Gson();
        Client client = gson.fromJson(json, Client.class);

        assertNotNull(client);
        assertEquals(true, client.doDisplay());
        assertEquals(1, client.getAuthClientId());
        assertEquals(1, client.getId());
        assertEquals("NAME", client.getName());
        assertEquals("URL", client.getUrl());
    }

    /**
     * Verifies that the members of the {@link Client} class can be initialized
     * to default by instantiation.
     * 
     * <pre>
     * 
     * 1. Create a new Client object instance.
     * 
     * 2. Verify that the members have the default values.
     * 
     * </pre>
     */
    public void testEmbedCanBeCreatedFromInstantiation() {
        Client client = new Client();
        assertNotNull(client);
        assertEquals(false, client.doDisplay());
        assertEquals(-1, client.getAuthClientId());
        assertEquals(-1, client.getId());
        assertEquals(null, client.getName());
        assertEquals(null, client.getUrl());
    }

}
