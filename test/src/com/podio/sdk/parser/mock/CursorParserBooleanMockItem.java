package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public class CursorParserBooleanMockItem extends CursorParserMockItem<Boolean> {

    // Existing Members
    public boolean existingPublicPlain;

    @StructuredName("existingPublicAnnotated")
    public boolean _existingPublicAnnotated;

    private boolean existingPrivatePlain;

    @StructuredName("existingPrivateAnnotated")
    private boolean _existingPrivateAnnotated;

    // Non Existing Members
    public boolean nonExistingPublicPlain;

    @StructuredName("nonExistingPublicAnnotated")
    public boolean _nonExistingPublicAnnotated;

    private boolean nonExistingPrivatePlain;

    @StructuredName("nonExistingPrivateAnnotated")
    private boolean _nonExistingPrivateAnnotated;

    // Getters for Existing Members
    public Boolean getExistingPublicPlain() {
        return existingPublicPlain;
    }

    public Boolean getExistingPublicAnnotated() {
        return _existingPublicAnnotated;
    }

    public Boolean getExistingPrivatePlain() {
        return existingPrivatePlain;
    }

    public Boolean getExistingPrivateAnnotated() {
        return _existingPrivateAnnotated;
    }

    // Getters for Non Existing Members
    public Boolean getNonExistingPublicPlain() {
        return nonExistingPublicPlain;
    }

    public Boolean getNonExistingPublicAnnotated() {
        return _nonExistingPublicAnnotated;
    }

    public Boolean getNonExistingPrivatePlain() {
        return nonExistingPrivatePlain;
    }

    public Boolean getNonExistingPrivateAnnotated() {
        return _nonExistingPrivateAnnotated;
    }
}
