package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public class CursorParserLongMockItem extends CursorParserMockItem<Long> {

    // Existing Members
    public long existingPublicPlain;

    @StructuredName("existingPublicAnnotated")
    public long _existingPublicAnnotated;

    private long existingPrivatePlain;

    @StructuredName("existingPrivateAnnotated")
    private long _existingPrivateAnnotated;

    // Non Existing Members
    public long nonExistingPublicPlain;

    @StructuredName("nonExistingPublicAnnotated")
    public long _nonExistingPublicAnnotated;

    private long nonExistingPrivatePlain;

    @StructuredName("nonExistingPrivateAnnotated")
    private long _nonExistingPrivateAnnotated;

    // Getters for Existing Members
    public Long getExistingPublicPlain() {
        return existingPublicPlain;
    }

    public Long getExistingPublicAnnotated() {
        return _existingPublicAnnotated;
    }

    public Long getExistingPrivatePlain() {
        return existingPrivatePlain;
    }

    public Long getExistingPrivateAnnotated() {
        return _existingPrivateAnnotated;
    }

    // Getters for Non Existing Members
    public Long getNonExistingPublicPlain() {
        return nonExistingPublicPlain;
    }

    public Long getNonExistingPublicAnnotated() {
        return _nonExistingPublicAnnotated;
    }

    public Long getNonExistingPrivatePlain() {
        return nonExistingPrivatePlain;
    }

    public Long getNonExistingPrivateAnnotated() {
        return _nonExistingPrivateAnnotated;
    }
}
