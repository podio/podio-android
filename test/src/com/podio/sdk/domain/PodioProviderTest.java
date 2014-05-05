package com.podio.sdk.domain;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.PodioProviderListener;
import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.domain.mock.MockRestClient;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.provider.BasicPodioFilter;
import com.podio.sdk.provider.BasicPodioProvider;

public class PodioProviderTest extends AndroidTestCase {

    private static final class ConcurrentResult {
        private boolean isSessionChangeCalled = false;
        private boolean isSuccessCalled = false;
        private boolean isFailureCalled = false;
        private Object ticket = null;
        private Object item = null;
        private String message = null;
    }

    /**
     * Verify that the abstract {@link BasicPodioProvider} implementation builds the
     * correct {@link RestRequest} for a DELETE request.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around.
     * 
     * 2. Call a delete operation on the ItemProvider object.
     * 
     * 3. Pick up the produced RestRequest from the mocked RestClient and
     *      verify that it conforms to expectations.
     * 
     * </pre>
     */
    public void testCorrectDeleteRestRequestProduced() {
        final MockRestClient client = new MockRestClient();
        final PodioFilter filter = new BasicPodioFilter();

        // Perform the delete request.
        BasicPodioProvider target = new BasicPodioProvider();
        target.setRestClient(client);
        target.deleteRequest(filter);

        // Verify that the correct DELETE RestRequest is built.
        RestRequest request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.DELETE, null, filter, filter, request);
    }

    /**
     * Verify that the abstract {@link BasicPodioProvider} implementation builds the
     * correct {@link RestRequest} for a GET request.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around.
     * 
     * 2. Call a fetch operation on the ItemProvider object.
     * 
     * 3. Pick up the produced RestRequest from the mocked RestClient and
     *      verify that it conforms to expectations.
     * 
     * </pre>
     */
    public void testCorrectGetRestRequestProduced() {
        final MockRestClient client = new MockRestClient();
        final PodioFilter filter = new BasicPodioFilter();

        // Perform the fetch request.
        BasicPodioProvider target = new BasicPodioProvider();
        target.setRestClient(client);
        target.fetchRequest(filter);

        // Verify that the correct GET RestRequest is built.
        RestRequest request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.GET, null, filter, filter, request);
    }

    /**
     * Verify that the abstract {@link BasicPodioProvider} implementation builds the
     * correct {@link RestRequest} for a POST request.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around.
     * 
     * 2. Call a push operation on the ItemProvider object.
     * 
     * 3. Pick up the produced RestRequest from the mocked RestClient and
     *      verify that it conforms to expectations.
     * 
     * </pre>
     */
    public void testCorrectPostRestRequestProduced() {
        final MockRestClient client = new MockRestClient();
        final Object item = new Object();
        final PodioFilter filter = new BasicPodioFilter();

        // Perform the push request.
        BasicPodioProvider target = new BasicPodioProvider();
        target.setRestClient(client);
        target.pushRequest(filter, item);

        // Verify that the correct POST RestRequest is built.
        RestRequest request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.POST, item, filter, filter, request);
    }

    /**
     * Verify that the abstract {@link BasicPodioProvider} implementation builds the
     * correct {@link RestRequest} for a PUT request.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around.
     * 
     * 2. Call a change operation on the ItemProvider object.
     * 
     * 3. Pick up the produced RestRequest from the mocked RestClient and
     *      verify that it conforms to expectations.
     * 
     * </pre>
     */
    public void testCorrectPutRestRequestProduced() {
        final MockRestClient client = new MockRestClient();
        final PodioFilter filter = new BasicPodioFilter();
        final Object item = new Object();

        // Perform the change request.
        BasicPodioProvider target = new BasicPodioProvider();
        target.setRestClient(client);
        target.changeRequest(filter, item);

        // Verify that the correct PUT RestRequest is built.
        RestRequest request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.PUT, item, filter, filter, request);
    }

    /**
     * Verifies that the failure method on the {@link PodioProviderListener} callback
     * is called if the (mocked) {@link RestClient} fails to finish properly.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around. Also assign a custom {@link PodioProviderListener} to the
     *      ItemProvider.
     * 
     * 2. Perform a request (any rest request) and simulate the mock RestClient
     *      working on it and failing (the {@link MockRestClient} holds the
     *      details).
     * 
     * 3. Verify that the failure is propagated properly to your custom callback.
     * 
     * </pre>
     */
    public void testProviderCallbackFailureCalledProperly() {
        final PodioFilter itemFilter = new BasicPodioFilter();
        final Object itemObject = new Object();
        final String errorMessage = "ohno";
        final List<Object> resultList = new ArrayList<Object>();
        resultList.add(itemObject);

        final MockRestClient client = new MockRestClient();
        final ConcurrentResult result = new ConcurrentResult();
        final PodioProviderListener listener = new PodioProviderListener() {
            @Override
            public void onRequestComplete(Object ticket, Object item) {
                result.isSuccessCalled = true;
                result.ticket = ticket;
                result.item = item;
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
                result.isSessionChangeCalled = true;
                result.ticket = ticket;
                result.item = null;
            }

            @Override
            public void onRequestFailure(Object ticket, String message) {
                result.isFailureCalled = true;
                result.ticket = ticket;
                result.message = message;
            }
        };

        // Simulate an update request.
        BasicPodioProvider target = new BasicPodioProvider();
        target.setRestClient(client);
        target.setProviderListener(listener);
        target.changeRequest(itemFilter, itemObject);

        // Allow the mock client to "process" the request (basically allow the
        // callbacks to execute).
        client.mock_processLastPushedRestRequest(false, errorMessage, resultList);

        assertEquals(false, result.isSessionChangeCalled);
        assertEquals(false, result.isSuccessCalled);
        assertEquals(true, result.isFailureCalled);
        assertEquals(itemFilter, result.ticket);
        assertEquals(errorMessage, result.message);
    }

    /**
     * Verifies that the success method on the {@link PodioProviderListener} callback
     * is called if the (mocked) {@link RestClient} finishes successfully.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around. Also assign a custom {@link PodioProviderListener} to the
     *      ItemProvider.
     * 
     * 2. Perform a request (any rest request) and simulate the mock RestClient
     *      working successfully on it (the {@link MockRestClient} holds the
     *      details).
     * 
     * 3. Verify that the success is propagated properly to your custom callback.
     * 
     * </pre>
     */
    public void testProviderCallbackSuccessCalledProperly() {
        final PodioFilter itemFilter = new BasicPodioFilter();
        final Object itemObject = new Object();
        final String errorMessage = "ohno";

        final MockRestClient client = new MockRestClient();
        final ConcurrentResult result = new ConcurrentResult();
        final PodioProviderListener listener = new PodioProviderListener() {
            @Override
            public void onRequestComplete(Object ticket, Object item) {
                result.isSuccessCalled = true;
                result.ticket = ticket;
                result.item = item;
            }

            @Override
            public void onSessionChange(Object ticket, Session session) {
                result.isSessionChangeCalled = true;
                result.ticket = ticket;
                result.item = null;
            }

            @Override
            public void onRequestFailure(Object ticket, String message) {
                result.isFailureCalled = true;
                result.ticket = ticket;
                result.message = message;
            }
        };

        // Simulate an update request.
        BasicPodioProvider target = new BasicPodioProvider();
        target.setRestClient(client);
        target.setProviderListener(listener);
        target.changeRequest(itemFilter, itemObject);

        // Allow the mock client to "process" the request (basically allow the
        // callbacks to execute).
        client.mock_processLastPushedRestRequest(true, errorMessage, itemObject);

        assertEquals(false, result.isSessionChangeCalled);
        assertEquals(true, result.isSuccessCalled);
        assertEquals(false, result.isFailureCalled);
        assertEquals(itemFilter, result.ticket);
        assertEquals(itemObject, result.item);
    }

    /**
     * Performs tests on the given {@link RestRequest} and verifies they meet
     * the provided expectations.
     * 
     * @param expectedOperation
     *            The expected {@link RestOperation}.
     * @param expectedContent
     *            The expected target item.
     * @param expectedFilter
     *            The expected request filter.
     * @param expectedPath
     *            The expected path in the request URI.
     * @param target
     *            The {@link RestRequest} that needs to live up to the
     *            expectations.
     */
    private void validateRequest(RestOperation expectedOperation, Object expectedContent,
            Object expectedTicket, PodioFilter expectedFilter, RestRequest target) {

        assertNotNull(target);
        assertEquals(expectedContent, target.getContent());
        assertEquals(expectedOperation, target.getOperation());
        assertEquals(expectedFilter, target.getFilter());
        assertEquals(expectedTicket, target.getTicket());
    }
}
