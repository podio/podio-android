package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public final class ItemParserLongMockItem {

    @StructuredName("real1")
    public final Long phoney1 = 12345L;

    public final Long real2 = 23456L;

    public final Long real3 = null;

    @StructuredName("real4")
    public final long phoney4 = 34567L;

    public final long real5 = 45678L;

}
