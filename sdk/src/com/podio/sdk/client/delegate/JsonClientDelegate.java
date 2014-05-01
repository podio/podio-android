package com.podio.sdk.client.delegate;

import com.podio.sdk.RestClientDelegate;

public abstract class JsonClientDelegate implements RestClientDelegate {
    private ItemParser<?> parser;

    public void setItemParser(ItemParser<?> parser) {
        this.parser = parser;
    }

    protected Object parseJson(String json) throws InvalidParserException {
        testParser();
        return parser.parseToItem(json);
    }

    protected String parseItem(Object item) throws InvalidParserException {
        testParser();
        return parser.parseToJson(item);
    }

    private void testParser() throws InvalidParserException {
        if (parser == null) {
            throw new InvalidParserException(
                    "Parser mustn't be null. Have you called the setItemParser method?");
        }
    }
}
