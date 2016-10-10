package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.LinkedAccount;

/**
 * Created by sai on 9/26/16.
 */

public class LinkedAccountProvider extends Provider {
    public static class GetLinkedAccountFilter extends Filter {

        private static final String CAPABILITY = "capability";
        private static final String PROVIDER ="provider";

        public GetLinkedAccountFilter() {
            super("linked_account/");
        }

        public GetLinkedAccountFilter capability(LinkedAccount.Capability capability) {
            this.addQueryParameter(CAPABILITY, capability.name());
            return this;
        }

        public GetLinkedAccountFilter provider(LinkedAccount.Provider provider) {
            this.addQueryParameter(PROVIDER, provider.name());
            return this;
        }
    }
    public Request<LinkedAccount[]> getLinkedAccounts(LinkedAccount.Capability capability) {
        return get(new GetLinkedAccountFilter().capability(capability), LinkedAccount[].class);
    }

    public Request<LinkedAccount[]> getLinkedAccounts(LinkedAccount.Provider provider) {
        return get(new GetLinkedAccountFilter().provider(provider), LinkedAccount[].class);
    }

    public Request<LinkedAccount[]> getLinkedAccounts(LinkedAccount.Capability capability, LinkedAccount.Provider provider) {
        return get(new GetLinkedAccountFilter().capability(capability).provider(provider), LinkedAccount[].class);
    }
}
