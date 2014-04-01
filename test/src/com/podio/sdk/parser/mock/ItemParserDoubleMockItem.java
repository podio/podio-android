package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public final class ItemParserDoubleMockItem {

    @StructuredName("real1")
    public final Double phoney1 = 1.2345D;

    public final Double real2 = 2.3456D;

    public final Double real3 = null;

    @StructuredName("real4")
    public final double phoney4 = 3.4567D;

    public final double real5 = 4.5678D;

}
