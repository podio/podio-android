package com.podio.sdk.parser.mock;

import com.podio.sdk.parser.annotation.StructuredName;

public final class ItemParserFloatMockItem {

    @StructuredName("real1")
    public final Float phoney1 = 1.23f;

    public final Float real2 = 2.34f;

    public final Float real3 = null;

    @StructuredName("real4")
    public final float phoney4 = 3.45f;

    public final float real5 = 4.56f;

}
