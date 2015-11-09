package com.podio.sdk.provider;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.test.InstrumentationTestCase;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.volley.MockRestClient;
import com.podio.sdk.volley.VolleyRequest;


public class BasicPodioProviderTest extends InstrumentationTestCase {

    static class EmptyProvider extends Provider {

        static class Path extends Filter {
            Path() {
                super("empty");
            }
        }

        protected <T> Request<T> delete(Filter filter) {
            return super.delete(filter);
        }

        protected <T> Request<T> get(Filter filter, Class<T> classOfResult) {
            return super.get(filter, classOfResult);
        }

        protected <T> Request<T> post(Filter filter, Object item, Class<T> classOfItem) {
            return super.post(filter, item, classOfItem);
        }

        protected <T> Request<T> put(Filter filter, Object item, Class<T> classOfItem) {
            return super.put(filter, item, classOfItem);
        }
    }

    EmptyProvider testProvider;
    Filter emptyFilter;

    @Mock
    Request.ResultListener<Object> resultListener;

    @Mock
    Request.SessionListener sessionListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        testProvider = new EmptyProvider();
        emptyFilter = new EmptyProvider.Path();

        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        testProvider.setClient(mockClient);

    }

    private void assertCommonAttributes(VolleyRequest r, boolean hasBody) {
        assertEquals(r.getBodyContentType(), "application/json; charset=UTF-8");
        assertEquals(r.getPriority(), com.android.volley.Request.Priority.NORMAL);
        assertEquals(r.getRetryPolicy().getClass(), DefaultRetryPolicy.class);
        try {
            if (hasBody)
                assertNotNull(r.getBody());
            else
                assertNull(r.getBody());
            assertEquals(r.getHeaders().size(), 1);
            assertNotNull(r.getHeaders().get("X-Time-Zone"));
        } catch (AuthFailureError authFailureError) {
            fail();
        }
    }

    public void testCorrectDeleteRestRequestProduced() {
        VolleyRequest r = (VolleyRequest) testProvider.delete(emptyFilter);

        assertEquals(r.getMethod(), com.android.volley.Request.Method.DELETE);
        assertCommonAttributes(r, false);
    }

    public void testCorrectGetRestRequestProduced() {
        VolleyRequest r = (VolleyRequest) testProvider.get(emptyFilter, Object.class);

        assertEquals(r.getMethod(), com.android.volley.Request.Method.GET);
        assertCommonAttributes(r, false);
    }

    public void testCorrectPostRestRequestProduced() {
        Object obj = new Object();
        VolleyRequest r = (VolleyRequest) testProvider.post(emptyFilter, obj, Object.class);

        assertEquals(r.getMethod(), com.android.volley.Request.Method.POST);
        assertCommonAttributes(r, true);
    }

    public void testCorrectPutRestRequestProduced() {
        Object obj = new Object();
        VolleyRequest r = (VolleyRequest) testProvider.put(emptyFilter, obj, Object.class);

        assertEquals(r.getMethod(), com.android.volley.Request.Method.PUT);
        assertCommonAttributes(r, true);
    }
}
