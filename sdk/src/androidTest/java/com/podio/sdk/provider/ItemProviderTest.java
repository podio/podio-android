
package com.podio.sdk.provider;

import java.util.concurrent.ExecutionException;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import com.podio.sdk.Request;
import com.podio.sdk.domain.Item;
import com.podio.sdk.volley.MockRestClient;
import com.podio.sdk.provider.ItemProvider.ItemFilterProvider;

public class ItemProviderTest extends InstrumentationTestCase {

    @Mock
    Request.ResultListener<Item> resultListener;

    @Mock
    Request.ResultListener<Item.CreateResult> createResultListener;

    @Mock
    Request.ResultListener<Item.FilterResult> filterResultListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    public void testCreateItem() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ItemProvider provider = new ItemProvider();
        provider.setClient(mockClient);
        provider.create(2, new Item()).withResultListener(createResultListener);

        Mockito.verify(createResultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/item/app/2"), mockClient.uri);
    }

    public void testCreateItemWithNullPointer() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ItemProvider provider = new ItemProvider();
        provider.setClient(mockClient);

        try {
            provider.create(7, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {}
    }

    public void testFilterItems() throws InterruptedException, ExecutionException {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ItemProvider provider = new ItemProvider();
        provider.setClient(mockClient);

        provider.filter()
                .onConstraint("test-key", "test-value")
                .onDoRemember(true)
                .onSortOrder("test-column", true)
                .onSpan(100, 1000)
                .get(4)
                .withResultListener(filterResultListener);

        Mockito.verify(filterResultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);

        String f = mockClient.data;
        String expected = "{\"filters\":{\"test-key\":\"test-value\"},\"sort_by\":\"test-column\",\"limit\":100,\"offset\":1000,\"remember\":true,\"sort_desc\":true,\"sort_nulls_last\":false}";
        assertEquals(f, expected);

        assertEquals(Uri.parse("https://test/item/app/4/filter"), mockClient.uri);
    }

    public void testGetItem() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ItemProvider provider = new ItemProvider();
        provider.setClient(mockClient);

        provider.get(3).withResultListener(resultListener);

        Mockito.verify(resultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);
        assertEquals(Uri.parse("https://test/item/3"), mockClient.uri);
    }


    public void testItemFilterProviderNotNull() {
        ItemProvider provider = new ItemProvider();
        provider.setClient(null);
        ItemFilterProvider p = provider.filter();
        assertNotNull(p);
    }


    public void testUpdateItem() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ItemProvider provider = new ItemProvider();
        provider.setClient(mockClient);

        provider.update(5, new Item()).withResultListener(createResultListener);

        Mockito.verify(createResultListener, Mockito.timeout(100).times(0)).onRequestPerformed(null);

        assertEquals(Uri.parse("https://test/item/5"), mockClient.uri);
    }


    public void testUpdateItemWithNullPointer() {
        MockRestClient mockClient = new MockRestClient(getInstrumentation().getTargetContext());
        ItemProvider provider = new ItemProvider();
        provider.setClient(mockClient);

        try {
            provider.update(7, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) { }
    }
}
