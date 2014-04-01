package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public final class ItemParserIntegerMockItem {

    @StructuredName("real1")
    public final Integer phoney1 = 10;

    public final Integer real2 = 20;

    public final Integer real3 = null;

    @StructuredName("real4")
    public final int phoney4 = 30;

    public final int real5 = 40;

}
