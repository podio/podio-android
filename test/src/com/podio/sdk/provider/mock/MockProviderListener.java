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
