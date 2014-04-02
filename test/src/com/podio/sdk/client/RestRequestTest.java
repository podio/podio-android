package com.podio.sdk.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;

public class RestRequestTest extends AndroidTestCase {

    /**
     * Verifies that all setters of the {@link RestRequest} returns the
     * RestRequest itself (to enabled a chained design pattern).
     * 
     * <pre>
     * 
     * 1. Create a new RestRequest object.
     * 
     * 2. Verify that each setter returns that very same object.
     * 
     * </pre>
     */
    public void testRestRequestSettersReturnRestRequest() {
        RestRequest target = new RestRequest();

        assertEquals(target, target.setContent(null));
        assertEquals(target, target.setItemType(null));
        assertEquals(target, target.setOperation(null));
        assertEquals(target, target.setPath(null));
        assertEquals(target, target.setQueryParameters(null));
        assertEquals(target, target.setResultListener(null));
        assertEquals(target, target.setTicket(null));
    }

    /**
     * Verifies that the assigned fields are kept intact until they are
     * requested again.
     * 
     * <pre>
     * 
     * 1. Create a new RestRequest object.
     * 
     * 2. Initialize the object with some known values.
     * 
     * 3. Call the corresponding getters and verify the results haven't
     *      changed.
     * 
     * </pre>
     */
    public void testRestRequestGettersReturnCorrectValues() {
        RestRequest target = new RestRequest();

        RestOperation operation = RestOperation.GET;
        String path = "test";

        Object item = new Object();
        Object ticket = new Object();
        Class<?> itemType = item.getClass();

        List<String> idParameter = new ArrayList<String>(2);
        idParameter.add("1");
        idParameter.add("2");
        Map<String, List<String>> parameters = new HashMap<String, List<String>>();
        parameters.put("id", idParameter);

        ResultListener resultListener = new ResultListener() {
            @Override
            public void onFailure(Object ticket, String message) {
            }

            @Override
            public void onSuccess(Object ticket, List<?> items) {
            }
        };

        target.setContent(item) //
                .setItemType(itemType) //
                .setOperation(operation) //
                .setPath(path) //
                .setQueryParameters(parameters) //
                .setResultListener(resultListener) //
                .setTicket(ticket);

        Uri reference = Uri.parse("scheme://authority/test?id=1&id=2");

        assertEquals(item, target.getContent());
        assertEquals(itemType, target.getItemType());
        assertEquals(operation, target.getOperation());
        assertEquals(reference, target.buildUri("scheme", "authority"));
        assertEquals(resultListener, target.getResultListener());
        assertEquals(ticket, target.getTicket());
    }
}
