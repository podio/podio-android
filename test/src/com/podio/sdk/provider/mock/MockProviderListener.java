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

package com.podio.sdk.provider.mock;

import com.podio.sdk.PodioProviderListener;
import com.podio.sdk.domain.Session;

public class MockProviderListener implements PodioProviderListener {

    public boolean mock_isSessionChangeCalled = false;
    public boolean mock_isSuccessCalled = false;
    public boolean mock_isFailureCalled = false;
    public Session mock_session = null;
    public String mock_message = null;
    public Object mock_ticket = null;
    public Object mock_item = null;

    @Override
    public void onRequestComplete(Object ticket, Object content) {
        mock_isSuccessCalled = true;
        mock_ticket = ticket;
        mock_item = content;
    }

    @Override
    public void onRequestFailure(Object ticket, String message) {
        mock_isFailureCalled = true;
        mock_ticket = ticket;
        mock_message = message;
    }

    @Override
    public void onSessionChange(Object ticket, Session session) {
        mock_isSessionChangeCalled = true;
        mock_ticket = ticket;
        mock_session = session;
    }

}
