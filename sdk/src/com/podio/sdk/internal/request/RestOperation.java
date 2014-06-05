/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */

package com.podio.sdk.internal.request;

import android.net.Uri;

import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.RestResult;

public enum RestOperation {
	AUTHORIZE() {
		@Override
		public RestResult invoke(RestClientDelegate delegate, Uri uri,
				Object item, PodioParser<?> parser) {
			return delegate.authorize(uri, parser);
		}
	},

	DELETE {
		@Override
		public RestResult invoke(RestClientDelegate delegate, Uri uri,
				Object item, PodioParser<?> parser) {
			return delegate.delete(uri, parser);
		}
	},

	GET {
		@Override
		public RestResult invoke(RestClientDelegate delegate, Uri uri,
				Object item, PodioParser<?> parser) {
			return delegate.get(uri, parser);
		}
	},

	POST {
		@Override
		public RestResult invoke(RestClientDelegate delegate, Uri uri,
				Object item, PodioParser<?> parser) {
			return delegate.post(uri, item, parser);
		}
	},

	PUT {
		@Override
		public RestResult invoke(RestClientDelegate delegate, Uri uri,
				Object item, PodioParser<?> parser) {
			return delegate.put(uri, item, parser);
		}
	};

	public abstract RestResult invoke(RestClientDelegate delegate, Uri uri,
			Object item, PodioParser<?> parser);
}
