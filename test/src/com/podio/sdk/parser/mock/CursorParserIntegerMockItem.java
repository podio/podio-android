package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public class CursorParserIntegerMockItem extends CursorParserMockItem<Integer> {

    // Existing Members
    public int existingPublicPlain;

    @StructuredName("existingPublicAnnotated")
    public int _existingPublicAnnotated;

    private int existingPrivatePlain;

    @StructuredName("existingPrivateAnnotated")
    private int _existingPrivateAnnotated;

    // Non Existing Members
    public int nonExistingPublicPlain;

    @StructuredName("nonExistingPublicAnnotated")
    public int _nonExistingPublicAnnotated;

    private int nonExistingPrivatePlain;

    @StructuredName("nonExistingPrivateAnnotated")
    private int _nonExistingPrivateAnnotated;

    // Getters for Existing Members
    public Integer getExistingPublicPlain() {
        return existingPublicPlain;
    }

    public Integer getExistingPublicAnnotated() {
        return _existingPublicAnnotated;
    }

    public Integer getExistingPrivatePlain() {
        return existingPrivatePlain;
    }

    public Integer getExistingPrivateAnnotated() {
        return _existingPrivateAnnotated;
    }

    // Getters for Non Existing Members
    public Integer getNonExistingPublicPlain() {
        return nonExistingPublicPlain;
    }

    public Integer getNonExistingPublicAnnotated() {
        return _nonExistingPublicAnnotated;
    }

    public Integer getNonExistingPrivatePlain() {
        return nonExistingPrivatePlain;
    }

    public Integer getNonExistingPrivateAnnotated() {
        return _nonExistingPrivateAnnotated;
    }
}
