package com.podio.sdk.client.delegate;

public final class InvalidParserException extends RuntimeException {
    private static final long serialVersionUID = 7694618576542717191L;

    public InvalidParserException() {
        super();
    }

    public InvalidParserException(String detailMessage) {
        super(detailMessage);
    }

}
