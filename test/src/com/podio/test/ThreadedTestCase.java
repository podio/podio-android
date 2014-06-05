package com.podio.test;

import android.test.InstrumentationTestCase;

public class ThreadedTestCase extends InstrumentationTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		TestUtils.reset();
	}
	
}
