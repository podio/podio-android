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

package com.podio.sdk.client;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.cache.CacheClient;
import com.podio.sdk.client.delegate.HttpClientDelegate;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.BasicPodioFilter;
import com.podio.sdk.internal.request.RestOperation;

public class CachedRestClientTest extends InstrumentationTestCase {

    private static final String EXPECTED_CACHE_KEY = "content://test.authority/path";

    private static final Uri EXPECTED_NETWORK_URI = Uri //
    .parse("https://test.authority/path");

    private static final PodioParser<Object> parser = PodioParser
            .fromClass(Object.class);

    @Mock
    private Object mockContent;
    @Mock
    private CacheClient mockCacheClient;
    @Mock
    private RestClientDelegate mockClientDelegate;

    private CachedRestClient targetRestClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

        MockitoAnnotations.initMocks(this);

        targetRestClient = new CachedRestClient(getInstrumentation().getContext(), "test.authority", mockClientDelegate, mockCacheClient);
    }

    private void request(RestOperation operation) {
        BasicPodioFilter filter = new BasicPodioFilter("path");

        RestRequest<Object> request = new RestRequest<Object>() //
        .setParser(parser).setContent(mockContent) //
        .setOperation(operation) //
        .setFilter(filter);

        targetRestClient.enqueue(request);
    }

    /**
     * Verifies that an IllegalArgumentException is thrown when trying to create
     * a CachedRestClient with null pointer delegates.
     * 
     * <pre>
     * 
     * 1. Create a new CachedRestClient with a null pointer network delegate.
     * 
     * 2. Verify that an IllegalArgumentException was thrown.
     * 
     * 3. Create a new CachedRestClient with a null pointer cache delegate.
     * 
     * 4. Verify that an IllegalArgumentException was thrown.
     * 
     * </pre>
     */
    public void testConstructorThrowsIllegalArgumentExceptionOnInvalidDelegates() {
        // Verify exception for network delegate.
        try {
            new CachedRestClient(null, null, null, mockCacheClient);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
        }

        // Verify exception for cache delegate.
        RestClientDelegate networkDelegate = new HttpClientDelegate(
                getInstrumentation().getContext());
        try {
            new CachedRestClient(null, null, networkDelegate, null);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Verifies that the expected Uri is delegated to the
     * {@link MockHttpClientDelegate}, while there is no Uri delegated at all to
     * the {@link MockDatabaseClientDelegate} when performing a authorize
     * request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "authorize" request.
     * 
     * 5. Verify that there is no Uri delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testAuthorizeRequestDelegatesCorrectly() {
        Mockito.when(mockClientDelegate.authorize(EXPECTED_NETWORK_URI))
                .thenReturn(RestResult.<Session> success());

        request(RestOperation.AUTHORIZE);

        Mockito.verifyZeroInteractions(mockCacheClient);

        Mockito.verify(mockClientDelegate, Mockito.timeout(2000)).authorize(
                EXPECTED_NETWORK_URI);
        Mockito.verifyNoMoreInteractions(mockClientDelegate);
    }

    /**
     * Verifies that the expected Uri delegated to the
     * {@link MockHttpClientDelegate}, while there is no Uri delegated at all to
     * the {@link MockDatabaseClientDelegate} when performing a delete request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "delete" request.
     * 
     * 5. Verify that there is no Uri delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testDeleteRequestDelegatesCorrectUri() {
        Mockito.when(mockClientDelegate.delete(EXPECTED_NETWORK_URI, parser))
                .thenReturn(RestResult.<Object> success());

        request(RestOperation.DELETE);

        Mockito.verify(mockCacheClient, Mockito.timeout(2000)).delete(
                EXPECTED_CACHE_KEY);
        Mockito.verifyNoMoreInteractions(mockCacheClient);

        Mockito.verify(mockClientDelegate, Mockito.timeout(2000)).delete(
                EXPECTED_NETWORK_URI, parser);
        Mockito.verifyNoMoreInteractions(mockClientDelegate);
    }

    /**
     * Verifies that the expected Uri is delegated to both the
     * {@link MockHttpClientDelegate}, and the
     * {@link MockDatabaseClientDelegate} when performing a get request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "get" request.
     * 
     * 5. Verify that the expected Uri is delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testGetRequestDelegatesCorrectUri() {
        Mockito.when(mockClientDelegate.get(EXPECTED_NETWORK_URI, parser))
                .thenReturn(RestResult.<Object> success());

        request(RestOperation.GET);

        Mockito.verify(mockCacheClient, Mockito.timeout(2000)).load(
                EXPECTED_CACHE_KEY);
        Mockito.verify(mockCacheClient, Mockito.timeout(2000)).save(
                EXPECTED_CACHE_KEY, null);
        Mockito.verifyNoMoreInteractions(mockCacheClient);

        Mockito.verify(mockClientDelegate, Mockito.timeout(2000)).get(
                EXPECTED_NETWORK_URI, parser);
        Mockito.verifyNoMoreInteractions(mockClientDelegate);
    }

    /**
     * Verifies that the expected Uri is delegated to both the
     * {@link MockHttpClientDelegate}, and the
     * {@link MockDatabaseClientDelegate} when performing a post request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "post" request.
     * 
     * 5. Verify that the expected Uri is delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testPostRequestDelegatesCorrectUri() {
        Mockito.when(
                mockClientDelegate.post(EXPECTED_NETWORK_URI, mockContent,
                        parser)).thenReturn(RestResult.<Object> success());

        request(RestOperation.POST);

        Mockito.verify(mockCacheClient, Mockito.timeout(2000)).delete(
                EXPECTED_CACHE_KEY);
        Mockito.verifyNoMoreInteractions(mockCacheClient);

        Mockito.verify(mockClientDelegate, Mockito.timeout(2000)).post(
                EXPECTED_NETWORK_URI, mockContent, parser);
        Mockito.verifyNoMoreInteractions(mockClientDelegate);
    }

    /**
     * Verifies that the expected Uri delegated to the
     * {@link MockHttpClientDelegate}, while there is no Uri delegated at all to
     * the {@link MockDatabaseClientDelegate} when performing a put request.
     * 
     * <pre>
     * 
     * 2. Add a mocked {@link DatabaseClientDelegate} to the client.
     * 
     * 3. Add a mocked {@link NetworkClientDelegate} to the client.
     * 
     * 4. Perform a "put" request.
     * 
     * 5. Verify that there is no Uri delegated to the mocked database
     *      delegate.
     * 
     * 6. Verify that the expected Uri is delegated to the mocked network
     *      delegate.
     * 
     * </pre>
     */
    public void testPutRequestDelegatesCorrectUri() {
        Mockito.when(
                mockClientDelegate.put(EXPECTED_NETWORK_URI, mockContent,
                        parser)).thenReturn(RestResult.<Object> success());

        request(RestOperation.PUT);

        Mockito.verify(mockCacheClient, Mockito.timeout(2000)).delete(
                EXPECTED_CACHE_KEY);
        Mockito.verifyNoMoreInteractions(mockCacheClient);

        Mockito.verify(mockClientDelegate, Mockito.timeout(2000)).put(
                EXPECTED_NETWORK_URI, mockContent, parser);
        Mockito.verifyNoMoreInteractions(mockClientDelegate);
    }

}
