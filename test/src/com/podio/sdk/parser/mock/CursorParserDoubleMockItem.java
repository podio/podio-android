package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public class CursorParserDoubleMockItem extends CursorParserMockItem<Double> {

    // Members
    public double existingPublicPlain;

    @StructuredName("existingPublicAnnotated")
    public double _existingPublicAnnotated;

    private double existingPrivatePlain;

    @StructuredName("existingPrivateAnnotated")
    private double _existingPrivateAnnotated;

    // Non Existing Members
    public double nonExistingPublicPlain;

    @StructuredName("nonExistingPublicAnnotated")
    public double _nonExistingPublicAnnotated;

    private double nonExistingPrivatePlain;

    @StructuredName("nonExistingPrivateAnnotated")
    private double _nonExistingPrivateAnnotated;

    // Getters for Existing Members
    public Double getExistingPublicPlain() {
        return existingPublicPlain;
    }

    public Double getExistingPublicAnnotated() {
        return _existingPublicAnnotated;
    }

    public Double getExistingPrivatePlain() {
        return existingPrivatePlain;
    }

    public Double getExistingPrivateAnnotated() {
        return _existingPrivateAnnotated;
    }

    // Getters for Non Existing Members
    public Double getNonExistingPublicPlain() {
        return nonExistingPublicPlain;
    }

    public Double getNonExistingPublicAnnotated() {
        return _nonExistingPublicAnnotated;
    }

    public Double getNonExistingPrivatePlain() {
        return nonExistingPrivatePlain;
    }

    public Double getNonExistingPrivateAnnotated() {
        return _nonExistingPrivateAnnotated;
    }
}
