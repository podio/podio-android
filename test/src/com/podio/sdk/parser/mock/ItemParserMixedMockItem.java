package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public final class ItemParserMixedMockItem {

    @StructuredName("real1")
    public final Boolean phoney1 = Boolean.TRUE;

    public final Float real2 = 2.34f;

    public final Long real3 = 23456L;

    @StructuredName("real4")
    public final String phoney4 = "Test";

    public final Object real5 = new Object();
}
