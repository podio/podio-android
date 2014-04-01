package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public class CursorParserFloatMockItem extends CursorParserMockItem<Float> {

    // Existing Members
    public float existingPublicPlain;

    @StructuredName("existingPublicAnnotated")
    public float _existingPublicAnnotated;

    private float existingPrivatePlain;

    @StructuredName("existingPrivateAnnotated")
    private float _existingPrivateAnnotated;

    // Non Existing Members
    public float nonExistingPublicPlain;

    @StructuredName("nonExistingPublicAnnotated")
    public float _nonExistingPublicAnnotated;

    private float nonExistingPrivatePlain;

    @StructuredName("nonExistingPrivateAnnotated")
    private float _nonExistingPrivateAnnotated;

    // Getters for Existing Members
    public Float getExistingPublicPlain() {
        return existingPublicPlain;
    }

    public Float getExistingPublicAnnotated() {
        return _existingPublicAnnotated;
    }

    public Float getExistingPrivatePlain() {
        return existingPrivatePlain;
    }

    public Float getExistingPrivateAnnotated() {
        return _existingPrivateAnnotated;
    }

    // Getters for Non Existing Members
    public Float getNonExistingPublicPlain() {
        return nonExistingPublicPlain;
    }

    public Float getNonExistingPublicAnnotated() {
        return _nonExistingPublicAnnotated;
    }

    public Float getNonExistingPrivatePlain() {
        return nonExistingPrivatePlain;
    }

    public Float getNonExistingPrivateAnnotated() {
        return _nonExistingPrivateAnnotated;
    }
}
