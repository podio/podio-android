///*
// *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
// *
// *  Permission is hereby granted, free of charge, to any person obtaining a copy of
// *  this software and associated documentation files (the "Software"), to deal in
// *  the Software without restriction, including without limitation the rights to
// *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
// *  of the Software, and to permit persons to whom the Software is furnished to
// *  do so, subject to the following conditions:
// *
// *  The above copyright notice and this permission notice shall be included in all
// *  copies or substantial portions of the Software.
// *
// *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// *  SOFTWARE.
// */
//
//package com.podio.sdk.domain;
//
//import java.util.concurrent.TimeUnit;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.test.AndroidTestCase;
//
//public class SessionTest extends AndroidTestCase {
//
//    /**
//     * Verifies that a {@link Session} object can be created from a valid JSON
//     * string with known attributes.
//     *
//     * <pre>
//     *
//     * 1. Define the test JSON string. Make sure it has the 'access_token',
//     *      'refresh_token' and 'expires' attributes.
//     *
//     * 2. Call the Session constructor with the test JSON.
//     *
//     * 3. Verify that the Session fields have been initialized according to the
//     *      JSON attributes.
//     *
//     * </pre>
//     */
//    public void testCanCreateSessionWithCustomValuesFromKnownJsonString() {
//        long now = currentTimeSeconds();
//        long time = now + 3600;
//        String json = "{\"access_token\": \"ACCESSTOKEN\", \"refresh_token\": \"REFRESHTOKEN\", \"expires\": " + time + "}";
//        Session session = new Session(json);
//
//        assertNotNull(session);
//        assertEquals("ACCESSTOKEN", session.accessToken);
//        assertEquals("REFRESHTOKEN", session.refreshToken);
//        assertEquals(time, session.expires);
//        assertEquals(3600, session.expiresIn);
//
//        json = "{\"access_token\": \"ACCESSTOKEN\", \"refresh_token\": \"REFRESHTOKEN\", \"expires_in\": 3600}";
//        session = new Session(json);
//
//        assertNotNull(session);
//        assertEquals("ACCESSTOKEN", session.accessToken);
//        assertEquals("REFRESHTOKEN", session.refreshToken);
//        assertEquals(time, session.expires);
//        assertEquals(3600, session.expiresIn);
//    }
//
//    /**
//     * Verifies that a {@link Session} object can be created from an invalid
//     * JSON string and that it's initialized with default values instead.
//     *
//     * <pre>
//     *
//     * 1. Define a syntactically invalid test JSON string.
//     *
//     * 2. Call the Session constructor with the test JSON.
//     *
//     * 3. Verify that the Session fields have been initialized with default
//     *      values.
//     *
//     * </pre>
//     */
//    public void testCanCreateSessionWithDefaultValuesFromInvalidJsonString() {
//        String json = "{\"missing_end_quot_string_attribute: UNKNOWNTYPE}";
//        Session session = new Session(json);
//
//        assertNotNull(session);
//        assertEquals(null, session.accessToken);
//        assertEquals(null, session.refreshToken);
//        assertEquals(0L, session.expires);
//        assertEquals(0L, session.expiresIn);
//    }
//
//    /**
//     * Verifies that a {@link Session} object can be created from a null pointer
//     * JSON string and that it's initialized with default values instead.
//     *
//     * <pre>
//     *
//     * 1. Define a null pointer test JSON string.
//     *
//     * 2. Call the Session constructor with the test JSON.
//     *
//     * 3. Verify that the Session fields have been initialized with default
//     *      values.
//     *
//     * </pre>
//     */
//    public void testCanCreateSessionWithDefaultValuesFromNullPointerJsonString() {
//        String json = null;
//        Session session = new Session(json);
//
//        assertNotNull(session);
//        assertEquals(null, session.accessToken);
//        assertEquals(null, session.refreshToken);
//        assertEquals(0L, session.expires);
//        assertEquals(0L, session.expiresIn);
//    }
//
//    /**
//     * Verifies that a {@link Session} object can be created from a valid JSON
//     * string with unknown attributes and that it's initialized with default
//     * values instead.
//     *
//     * <pre>
//     *
//     * 1. Define the test JSON string. Make sure it doesn't have any known
//     *      attributes.
//     *
//     * 2. Call the Session constructor with the test JSON.
//     *
//     * 3. Verify that the Session fields have been initialized with default
//     *      values.
//     *
//     * </pre>
//     */
//    public void testCanCreateSessionWithDefaultValuesFromUnknownJsonString() {
//        String json = "{\"unknown_attribute\": \"UNKNOWNVALUE\"}";
//        Session session = new Session(json);
//
//        assertNotNull(session);
//        assertEquals(null, session.accessToken);
//        assertEquals(null, session.refreshToken);
//        assertEquals(0L, session.expires);
//        assertEquals(0L, session.expiresIn);
//    }
//
//    /**
//     * Verifies that a {@link Session} object can be created from its
//     * constructor and that its initial values are not skewed.
//     *
//     * <pre>
//     *
//     * 1. Create a Session object with its constructor.
//     *
//     * 2. Verify that the assigned values haven't changed.
//     *
//     * 3. Verify that the calculated values are correct.
//     *
//     * </pre>
//     */
//    public void testCanCreateSessionFromConstructor() {
//        long now = currentTimeSeconds();
//        long time = now + 3600;
//        Session session = new Session("ACCESSTOKEN", "REFRESHTOKEN", 3600);
//
//        assertNotNull(session);
//        assertEquals("ACCESSTOKEN", session.accessToken);
//        assertEquals("REFRESHTOKEN", session.refreshToken);
//        assertEquals(time, session.expires);
//        assertEquals(3600, session.expiresIn);
//    }
//
//    /**
//     * Verifies that the {@link Session} object can be serialized to valid JSON.
//     *
//     * <pre>
//     *
//     * 1. Create a new Session object with known values.
//     *
//     * 2. Serialize it to JSON notation.
//     *
//     * 3. Verify the integrity of the JSON string.
//     *
//     * </pre>
//     *
//     * @throws JSONException
//     */
//    public void testCanSerializeToJsonString() throws JSONException {
//        long time = currentTimeSeconds() + 60;
//        String source1 = "{\"access_token\":\"ACCESSTOKEN\",\"expires_in\":60,\"refresh_token\":\"REFRESHTOKEN\"}";
//        Session session1 = new Session(source1);
//        verifySessionJson(session1.toJson(), "ACCESSTOKEN", "REFRESHTOKEN", time);
//
//        String source2 = "{\"access_token\":\"ACCESSTOKEN\",\"expires\":" + time + ",\"refresh_token\":\"REFRESHTOKEN\"}";
//        Session session2 = new Session(source2);
//        verifySessionJson(session2.toJson(), "ACCESSTOKEN", "REFRESHTOKEN", time);
//
//        Session session3 = new Session("ACCESSTOKEN", "REFRESHTOKEN", 60);
//        verifySessionJson(session3.toJson(), "ACCESSTOKEN", "REFRESHTOKEN", time);
//    }
//
//    /**
//     * Verifies that the overridden equals method of the {@link Session} object
//     * is intact.
//     *
//     * <pre>
//     *
//     * 1. Create different {@link Session} objects
//     *
//     * 2. Compare them and verify that the comparison returns the expected result.
//     *
//     * </pre>
//     */
//    public void testEquals() {
//        Session session1 = new Session(null, null, 0);
//        Session session2 = session1;
//
//        assertTrue(session1.equals(session2));
//        assertFalse(session1.equals(null));
//        assertFalse(session1.equals(new String("A")));
//
//        assertFalse(new Session(null, null, 0).equals(new Session("AB", null, 0)));
//        assertFalse(new Session("AB", null, 0).equals(new Session("EF", null, 0)));
//
//        assertFalse(new Session(null, null, 0).equals(new Session(null, "AB", 0)));
//        assertFalse(new Session(null, "AB", 0).equals(new Session(null, "EF", 0)));
//
//        assertFalse(new Session(null, null, 0).equals(new Session(null, null, 1)));
//        assertFalse(new Session("AB", "BA", 0).equals(new Session("EF", "FE", 1)));
//
//        assertTrue(new Session(null, null, 0).equals(new Session(null, null, 0)));
//        assertTrue(new Session(null, "AB", 0).equals(new Session(null, "AB", 0)));
//        assertTrue(new Session("AB", null, 0).equals(new Session("AB", null, 0)));
//        assertTrue(new Session("AB", "AB", 0).equals(new Session("AB", "AB", 0)));
//    }
//
//    /**
//     * Verifies that the overridden hashCode method of the {@link Session}
//     * object is intact.
//     *
//     * <pre>
//     *
//     * 1. Create different {@link Session} objects.
//     *
//     * 2. Compare the results from their corresponding hashCode method calls
//     *      and verify that the values are as expected.
//     *
//     * </pre>
//     */
//    public void testHashCode() {
//        assertTrue(new Session(null, null, 0).hashCode() == new Session(null, null, 0).hashCode());
//        assertTrue(new Session("AB", "CD", 1).hashCode() == new Session("AB", "CD", 1).hashCode());
//        assertFalse(new Session("AB", "CD", 1).hashCode() == new Session("EF", "GH", 2).hashCode());
//    }
//
//    /**
//     * Verifies that the {@link Session} claims itself to be authorized when it
//     * contains non-empty tokens and expires time stamp.
//     *
//     * <pre>
//     *
//     * 1. Create a Session object with non-empty access token and
//     *      refresh token + an expires time stamp in the future.
//     *
//     * 2. Ask it if it thinks it has grounds for assuming an authorized state.
//     *
//     * 3. Verify that it claims "yes".
//     *
//     * </pre>
//     */
//    public void testIsAuthorizedWithNonEmptyTokensAndTimeStamp() {
//        Session session1 = new Session("ACCESSTOKEN", "REFRESHTOKEN", 3600L);
//        assertTrue(session1.isAuthorized());
//
//        Session session2 = new Session("{access_token: 'ACCESSTOKEN', refresh_token:'REFRESHTOKEN', expires:3600}");
//        assertTrue(session2.isAuthorized());
//    }
//
//    /**
//     * Verifies that the {@link Session} doesn't claim itself to be authorized
//     * when it contains an empty auth token.
//     *
//     * <pre>
//     *
//     * 1. Create a Session object with an empty access token.
//     *
//     * 2. Ask it if it thinks it has grounds for assuming an authorized state.
//     *
//     * 3. Verify that it claims "no".
//     *
//     * </pre>
//     */
//    public void testIsNotAuthorizedWithEmptyAccessToken() {
//        Session session1 = new Session("", "REFRESHTOKEN", 3600L);
//        assertFalse(session1.isAuthorized());
//
//        Session session2 = new Session(null, "REFRESHTOKEN", 3600L);
//        assertFalse(session2.isAuthorized());
//
//        Session session3 = new Session("{access_token: '', refresh_token:'REFRESHTOKEN', expires:3600}");
//        assertFalse(session3.isAuthorized());
//
//        Session session4 = new Session("{refresh_token:'REFRESHTOKEN', expires:3600}");
//        assertFalse(session4.isAuthorized());
//    }
//
//    /**
//     * Verifies that the {@link Session} doesn't claim itself to be authorized
//     * when it contains an empty expires time stamp.
//     *
//     * <pre>
//     *
//     * 1. Create a Session object with an old time stamp.
//     *
//     * 2. Ask it if it thinks it has grounds for assuming an authorized state.
//     *
//     * 3. Verify that it claims "no".
//     *
//     * </pre>
//     */
//    public void testIsNotAuthorizedWithEmptyExpiresTimeStamp() {
//        long currentTimeStamp = currentTimeSeconds();
//        Session session1 = new Session("ACCESSTOKEN", "REFRESHTOKEN", -currentTimeStamp);
//        assertFalse(session1.isAuthorized());
//
//        Session session2 = new Session("{access_token: 'ACCESSTOKEN', refresh_token:'REFRESHTOKEN', expires:0}");
//        assertFalse(session2.isAuthorized());
//
//        Session session3 = new Session("{access_token: 'ACCESSTOKEN', refresh_token:'REFRESHTOKEN'}");
//        assertFalse(session3.isAuthorized());
//    }
//
//    /**
//     * Verifies that the {@link Session} doesn't claim itself to be authorized
//     * when it contains an empty refresh token.
//     *
//     * <pre>
//     *
//     * 1. Create a Session object with an empty refresh token.
//     *
//     * 2. Ask it if it thinks it has grounds for assuming an authorized state.
//     *
//     * 3. Verify that it claims "no".
//     *
//     * </pre>
//     */
//    public void testIsNotAuthorizedWithEmptyRefreshToken() {
//        Session session1 = new Session("ACCESSTOKEN", "", 3600L);
//        assertFalse(session1.isAuthorized());
//
//        Session session2 = new Session("ACCESSTOKEN", null, 3600L);
//        assertFalse(session2.isAuthorized());
//
//        Session session3 = new Session("{access_token: 'ACCESSTOKEN', refresh_token:'', expires:3600}");
//        assertFalse(session3.isAuthorized());
//
//        Session session4 = new Session("{access_token:'ACCESSTOKEN', expires:3600}");
//        assertFalse(session4.isAuthorized());
//    }
//
//    /**
//     * Verifies that the Session object doesn't recommend a refresh upon request
//     * when there is more than 10 minutes until it will expire.
//     *
//     * <pre>
//     *
//     * 1. Create a Session object with more than 10 minutes life time.
//     *
//     * 2. Request a refresh hint from it and verify it suggests a "no refresh".
//     *
//     * </pre>
//     */
//    public void testShouldNotRefresh() {
//        int time = 60 * 10 + 1; // ten minutes and one second (in seconds).
//        String json = "{\"access_token\":\"ACCESSTOKEN\",\"expires_in\":" + time + ",\"refresh_token\":\"REFRESHTOKEN\"}";
//        Session session = new Session(json);
//
//        assertEquals(false, session.shouldRefreshTokens());
//    }
//
//    /**
//     * Verifies that the Session object recommends a refresh upon request when
//     * there is less than 10 minutes until it will expire.
//     *
//     * <pre>
//     *
//     * 1. Create a Session object with less than 10 minutes life time.
//     *
//     * 2. Request a refresh hint from it and verify it suggests a "do refresh".
//     *
//     * </pre>
//     */
//    public void testShouldRefresh() {
//        int time = 60 * 9 + 59; // nine minutes and 59 second (in seconds).
//        String json = "{\"access_token\":\"ACCESSTOKEN\",\"expires_in\":" + time + ",\"refresh_token\":\"REFRESHTOKEN\"}";
//        Session session = new Session(json);
//
//        assertEquals(true, session.shouldRefreshTokens());
//    }
//
//    /**
//     * Verifies that the {@link Session#equals(Object)} method returns false on
//     * two different instances with different values. This test also verifies
//     * that the {@link Session#hashCode()} method returns different values from
//     * two different instances with different values.
//     *
//     * <pre>
//     *
//     * 1. Create two different instances of the Session class with different
//     *      content values.
//     *
//     * 2. Verify that two different instances have been created.
//     *
//     * 3. Verify that the equals method returns false when comparing the two.
//     *
//     * 4. Verify that the hash code method returns different values for the two.
//     *
//     * </pre>
//     */
//    public void testTwoDifferentInstancesWithDifferentValuesDontEqual() {
//        String sourceJson1 = "{\"access_token\":\"ACCESSTOKEN1\",\"expires\":3601,\"refresh_token\":\"REFRESHTOKEN1\"}";
//        String sourceJson2 = "{\"access_token\":\"ACCESSTOKEN2\",\"expires\":3602,\"refresh_token\":\"REFRESHTOKEN2\"}";
//        Session session1 = new Session(sourceJson1);
//        Session session2 = new Session(sourceJson2);
//
//        assertNotSame(session1, session2);
//        assertFalse(session1.equals(session2));
//        assertFalse(session2.equals(session1));
//        assertNotNull(session1);
//        assertNotNull(session2);
//        assertFalse(session1.hashCode() == session2.hashCode());
//    }
//
//    /**
//     * Verifies that the {@link Session#equals(Object)} method returns true on
//     * two different instances with default values. This test also verifies that
//     * the {@link Session#hashCode()} method returns the same value from two
//     * different instances with default values.
//     *
//     * <pre>
//     *
//     * 1. Create two different instances of the Session class with default
//     *      content values.
//     *
//     * 2. Verify that two different instances have been created.
//     *
//     * 3. Verify that the equals method returns true when comparing the two.
//     *
//     * 4. Verify that the hash code method returns the same value for the two.
//     *
//     * </pre>
//     */
//    public void testTwoDifferentInstancesWithDefaultValuesEquals() {
//        String sourceJson = "{}";
//        Session session1 = new Session(sourceJson);
//        Session session2 = new Session(sourceJson);
//
//        assertFalse(session1 == session2);
//        assertEquals(session1, session2);
//        assertEquals(session2, session1);
//        assertEquals(session1.hashCode(), session2.hashCode());
//    }
//
//    /**
//     * Verifies that the {@link Session#equals(Object)} method returns true on
//     * two different instances with the same values. This test also verifies
//     * that the {@link Session#hashCode()} method returns the same value from
//     * two different instances with the same values.
//     *
//     * <pre>
//     *
//     * 1. Create two different instances of the Session class with the same
//     *      content values.
//     *
//     * 2. Verify that two different instances have been created.
//     *
//     * 3. Verify that the equals method returns true when comparing the two.
//     *
//     * 4. Verify that the hash code method returns the same value for the two.
//     *
//     * </pre>
//     */
//    public void testTwoDifferentInstancesWithSameValuesEquals() {
//        String sourceJson = "{\"access_token\":\"ACCESSTOKEN\",\"expires\":3600,\"refresh_token\":\"REFRESHTOKEN\"}";
//        Session session1 = new Session(sourceJson);
//        Session session2 = new Session(sourceJson);
//
//        assertFalse(session1 == session2);
//        assertEquals(session1, session2);
//        assertEquals(session2, session1);
//        assertEquals(session1.hashCode(), session2.hashCode());
//    }
//
//    private void verifySessionJson(String json, String expectedAccessToken, String expectedRefreshToken, long expectedDeath) throws JSONException {
//        JSONObject jsonObject = new JSONObject(json);
//        assertEquals(true, jsonObject.has("access_token"));
//        assertEquals(expectedAccessToken, jsonObject.getString("access_token"));
//        assertEquals(true, jsonObject.has("refresh_token"));
//        assertEquals(expectedRefreshToken, jsonObject.getString("refresh_token"));
//        assertEquals(true, jsonObject.has("expires"));
//        assertEquals(expectedDeath, jsonObject.getLong("expires"));
//    }
//
//    private long currentTimeSeconds() {
//        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
//    }
//
//}
