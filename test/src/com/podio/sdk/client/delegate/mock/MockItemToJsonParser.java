package com.podio.sdk.client.delegate.mock;

import com.podio.sdk.parser.ItemToJsonParser;

public class MockItemToJsonParser extends ItemToJsonParser {

    private final String mockJsonContent;

    public MockItemToJsonParser(String mockJsonContent) {
        this.mockJsonContent = mockJsonContent;
    }

    @Override
    public String parse(Object source, Class<?> classOfTarget) {
        return mockJsonContent;
    }
}
