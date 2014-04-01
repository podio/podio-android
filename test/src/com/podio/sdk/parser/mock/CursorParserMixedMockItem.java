package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public class CursorParserMixedMockItem extends CursorParserMockItem<Object> {

    // Existing Members
    public String existingPublicPlain;

    @StructuredName("existingPublicAnnotated")
    public long _existingPublicAnnotated;

    private float existingPrivatePlain;

    @StructuredName("existingPrivateAnnotated")
    private boolean _existingPrivateAnnotated;

    // Non Existing Members
    public String nonExistingPublicPlain;

    @StructuredName("nonExistingPublicAnnotated")
    public long _nonExistingPublicAnnotated;

    private float nonExistingPrivatePlain;

    @StructuredName("nonExistingPrivateAnnotated")
    private boolean _nonExistingPrivateAnnotated;

    // Getters for Existing Members
    public String getExistingPublicPlain() {
        return existingPublicPlain;
    }

    public Long getExistingPublicAnnotated() {
        return _existingPublicAnnotated;
    }

    public Float getExistingPrivatePlain() {
        return existingPrivatePlain;
    }

    public Boolean getExistingPrivateAnnotated() {
        return _existingPrivateAnnotated;
    }

    // Getters for Non Existing Members
    public String getNonExistingPublicPlain() {
        return nonExistingPublicPlain;
    }

    public Long getNonExistingPublicAnnotated() {
        return _nonExistingPublicAnnotated;
    }

    public Float getNonExistingPrivatePlain() {
        return nonExistingPrivatePlain;
    }

    public Boolean getNonExistingPrivateAnnotated() {
        return _nonExistingPrivateAnnotated;
    }
}
