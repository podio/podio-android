package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public class CursorParserShortMockItem extends CursorParserMockItem<Short> {

    // Existing Members
    public short existingPublicPlain;

    @StructuredName("existingPublicAnnotated")
    public short _existingPublicAnnotated;

    private short existingPrivatePlain;

    @StructuredName("existingPrivateAnnotated")
    private short _existingPrivateAnnotated;

    // Non Existing Members
    public short nonExistingPublicPlain;

    @StructuredName("nonExistingPublicAnnotated")
    public short _nonExistingPublicAnnotated;

    private short nonExistingPrivatePlain;

    @StructuredName("nonExistingPrivateAnnotated")
    private short _nonExistingPrivateAnnotated;

    // Getters for Existing Members
    public Short getExistingPublicPlain() {
        return existingPublicPlain;
    }

    public Short getExistingPublicAnnotated() {
        return _existingPublicAnnotated;
    }

    public Short getExistingPrivatePlain() {
        return existingPrivatePlain;
    }

    public Short getExistingPrivateAnnotated() {
        return _existingPrivateAnnotated;
    }

    // Getters for Non Existing Members
    public Short getNonExistingPublicPlain() {
        return nonExistingPublicPlain;
    }

    public Short getNonExistingPublicAnnotated() {
        return _nonExistingPublicAnnotated;
    }

    public Short getNonExistingPrivatePlain() {
        return nonExistingPrivatePlain;
    }

    public Short getNonExistingPrivateAnnotated() {
        return _nonExistingPrivateAnnotated;
    }
}
