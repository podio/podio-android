package com.podio.sdk.client.delegate.mock;

import com.podio.sdk.parser.JsonToItemParser;

public class MockJsonToItemParser extends JsonToItemParser {

    private final Object mockDomainObject;

    public MockJsonToItemParser(Object mockDomainObject) {
        this.mockDomainObject = mockDomainObject;
    }

    @Override
    public Object parse(String source, Class<?> classOfTarget) {
        return mockDomainObject;
    }

}
