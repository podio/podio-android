package com.podio.sdk.domain;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.podio.sdk.Filter;
import com.podio.sdk.ProviderListener;
import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.domain.mock.MockRestClient;
import com.podio.sdk.internal.request.RestOperation;

public class ItemProviderTest extends AndroidTestCase {

    /**
     * Verify that the abstract {@link ItemProvider} implementation builds the
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
        final Filter filter = new ItemFilter();

        // Perform the delete request.
        ItemProvider<Object> target = new ItemProvider<Object>();
        target.setRestClient(client);
        target.deleteItems(filter);

        // Verify that the correct DELETE RestRequest is built.
        RestRequest request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.DELETE, null, filter, request);
    }

    /**
     * Verify that the abstract {@link ItemProvider} implementation builds the
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
        final Filter filter = new ItemFilter();

        // Perform the fetch request.
        ItemProvider<Object> target = new ItemProvider<Object>();
        target.setRestClient(client);
        target.fetchItems(filter);

        // Verify that the correct GET RestRequest is built.
        RestRequest request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.GET, null, filter, request);
    }

    /**
     * Verify that the abstract {@link ItemProvider} implementation builds the
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

        // Perform the push request.
        ItemProvider<Object> target = new ItemProvider<Object>();
        target.setRestClient(client);
        target.pushItem(item);

        // Verify that the correct POST RestRequest is built.
        RestRequest request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.POST, item, null, request);
    }

    /**
     * Verify that the abstract {@link ItemProvider} implementation builds the
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
        final Filter filter = new ItemFilter();
        final Object item = new Object();

        // Perform the change request.
        ItemProvider<Object> target = new ItemProvider<Object>();
        target.setRestClient(client);
        target.changeItem(filter, item);

        // Verify that the correct PUT RestRequest is built.
        RestRequest request = client.mock_getLastPushedRestRequest();
        validateRequest(RestOperation.PUT, item, filter, request);
    }

    /**
     * Verifies that the failure method on the {@link ProviderListener} callback
     * is called if the (mocked) {@link RestClient} fails to finish properly.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around. Also assign a custom {@link ProviderListener} to the
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
        final Filter itemFilter = new ItemFilter();
        final Object itemObject = new Object();
        final String errorMessage = "ohno";
        final List<Object> resultList = new ArrayList<Object>();
        resultList.add(itemObject);

        final MockRestClient client = new MockRestClient();
        final ProviderListener listener = new ProviderListener() {
            @Override
            public void onRequestCompleted(Object ticket, List<?> items) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }

            @Override
            public void onRequestFailed(Object ticket, String message) {
                assertEquals(itemFilter, ticket);
                assertEquals(message, errorMessage);
            }
        };

        // Simulate an update request.
        ItemProvider<Object> target = new ItemProvider<Object>();
        target.setRestClient(client);
        target.setProviderListener(listener);
        target.changeItem(itemFilter, itemObject);

        // Allow the mock client to "process" the request (basically allow the
        // callbacks to execute).
        client.mock_processLastPushedRestRequest(false, itemFilter, errorMessage, resultList);
    }

    /**
     * Verifies that the success method on the {@link ProviderListener} callback
     * is called if the (mocked) {@link RestClient} finishes successfully.
     * 
     * <pre>
     * 
     * 1. Create a new instance of the ItemProvider class and assign a mock
     *      RestClient to it, which basically has no logic but just shuffles
     *      data around. Also assign a custom {@link ProviderListener} to the
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
        final Filter itemFilter = new ItemFilter();
        final Object itemObject = new Object();
        final String errorMessage = "ohno";
        final List<Object> resultList = new ArrayList<Object>();
        resultList.add(itemObject);

        final MockRestClient client = new MockRestClient();
        final ProviderListener listener = new ProviderListener() {
            @Override
            public void onRequestCompleted(Object ticket, List<?> items) {
                assertEquals(itemFilter, ticket);
                assertEquals(resultList, items);
                assertEquals(resultList.size(), items.size());
                assertEquals(resultList.get(0), items.get(0));
            }

            @Override
            public void onRequestFailed(Object ticket, String message) {
                boolean isCalled = true;
                assertFalse(isCalled);
            }
        };

        // Simulate an update request.
        ItemProvider<Object> target = new ItemProvider<Object>();
        target.setRestClient(client);
        target.setProviderListener(listener);
        target.changeItem(itemFilter, itemObject);

        // Allow the mock client to "process" the request (basically allow the
        // callbacks to execute).
        client.mock_processLastPushedRestRequest(true, itemFilter, errorMessage, resultList);
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
            Filter expectedFilter, RestRequest target) {

        Class<?> expectedItemType = expectedContent != null ? expectedContent.getClass() : null;

        assertNotNull(target);
        assertEquals(expectedContent, target.getContent());
        assertEquals(expectedItemType, target.getItemType());
        assertEquals(expectedOperation, target.getOperation());
        assertEquals(expectedFilter, target.getFilter());
    }
}
