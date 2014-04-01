package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public final class ItemParserBooleanMockItem {

    @StructuredName("real1")
    public final Boolean phoney1 = Boolean.TRUE;

    public final Boolean real2 = Boolean.FALSE;

    public final Boolean real3 = null;

    @StructuredName("real4")
    public final boolean phoney4 = true;

    public final boolean real5 = false;

}
