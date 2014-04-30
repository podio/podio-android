package com.podio.sdk.client.delegate.mock;

import com.podio.sdk.client.delegate.ItemParser;

public class MockItemParser extends ItemParser<Object> {

    private final String mockJsonContent;
    private final Object mockItemContent;

    public MockItemParser(String mockJsonContent, Object mockItemContent) {
        super(Object.class);
        this.mockJsonContent = mockJsonContent;
        this.mockItemContent = mockItemContent;
    }

    @Override
    public String parseToJson(Object source) {
        return mockJsonContent;
    }

    @Override
    public Object parseToItem(String source) {
        return mockItemContent;
    }
}
