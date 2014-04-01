package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public final class ItemParserShortMockItem {

    @StructuredName("real1")
    public final Short phoney1 = 1;

    public final Short real2 = 2;

    public final Short real3 = null;

    @StructuredName("real4")
    public final short phoney4 = 3;

    public final short real5 = 4;

}
