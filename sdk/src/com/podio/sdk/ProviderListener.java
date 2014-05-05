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

import com.podio.sdk.domain.Session;

/**
 * Defines the {@link Provider} callback interface as seen by the third party
 * client application.
 * 
 * @author László Urszuly
 */
public interface ProviderListener {

    /**
     * Notifies the calling implementation that the {@link Provider} has
     * successfully performed a request. The result of the call and the ticket
     * identifying which call the result belongs to, are delivered through the
     * method arguments.
     * 
     * @param ticket
     *            The ticket used to identify the request.
     * @param content
     *            The result of the previously made fetch request.
     */
    public void onRequestComplete(Object ticket, Object content);

    /**
     * Notifies the calling implementation that a request couldn't be performed.
     * 
     * @param ticket
     *            The ticket used to identify the request.
     * @param message
     *            A message describing what went wrong.
     */
    public void onRequestFailure(Object ticket, String message);

    /**
     * Notifies the calling implementation that a request caused the session
     * tokens to change.
     * 
     * @param ticket
     *            The ticket used to identify the request that caused the
     *            session tokens to change.
     * @param session
     *            The new session tokens.
     */
    public void onSessionChange(Object ticket, Session session);

}
