package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public class CursorParserStringMockItem extends CursorParserMockItem<String> {

    // Existing Members
    public String existingPublicPlain;

    @StructuredName("existingPublicAnnotated")
    public String _existingPublicAnnotated;

    private String existingPrivatePlain;

    @StructuredName("existingPrivateAnnotated")
    private String _existingPrivateAnnotated;

    // Non Existing Members
    public String nonExistingPublicPlain;

    @StructuredName("nonExistingPublicAnnotated")
    public String _nonExistingPublicAnnotated;

    private String nonExistingPrivatePlain;

    @StructuredName("nonExistingPrivateAnnotated")
    private String _nonExistingPrivateAnnotated;

    // Getters for Existing Members
    @Override
    public String getExistingPublicPlain() {
        return existingPublicPlain;
    }

    @Override
    public String getExistingPublicAnnotated() {
        return _existingPublicAnnotated;
    }

    @Override
    public String getExistingPrivatePlain() {
        return existingPrivatePlain;
    }

    @Override
    public String getExistingPrivateAnnotated() {
        return _existingPrivateAnnotated;
    }

    // Getters for Non Existing Members
    public String getNonExistingPublicPlain() {
        return nonExistingPublicPlain;
    }

    public String getNonExistingPublicAnnotated() {
        return _nonExistingPublicAnnotated;
    }

    public String getNonExistingPrivatePlain() {
        return nonExistingPrivatePlain;
    }

    public String getNonExistingPrivateAnnotated() {
        return _nonExistingPrivateAnnotated;
    }
}
