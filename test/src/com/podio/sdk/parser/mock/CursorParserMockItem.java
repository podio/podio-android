package com.podio.sdk.parser.mock;


public abstract class CursorParserMockItem<T> {

    public abstract T getExistingPublicPlain();

    public abstract T getExistingPublicAnnotated();

    public abstract T getExistingPrivatePlain();

    public abstract T getExistingPrivateAnnotated();

    public abstract T getNonExistingPublicPlain();

    public abstract T getNonExistingPublicAnnotated();

    public abstract T getNonExistingPrivatePlain();

    public abstract T getNonExistingPrivateAnnotated();

}
