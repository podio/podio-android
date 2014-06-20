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

package com.podio.sdk;

import android.net.Uri;

import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Session;

public interface RestClientDelegate {

    public RestResult<Session> authorize(Uri uri) throws PodioException;

    public <T> RestResult<T> delete(Uri uri, PodioParser<? extends T> parser) throws PodioException;

    public <T> RestResult<T> get(Uri uri, PodioParser<? extends T> parser) throws PodioException;

    public <T> RestResult<T> post(Uri uri, Object item, PodioParser<? extends T> parser) throws PodioException;

    public <T> RestResult<T> put(Uri uri, Object item, PodioParser<? extends T> parser) throws PodioException;

}
