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
