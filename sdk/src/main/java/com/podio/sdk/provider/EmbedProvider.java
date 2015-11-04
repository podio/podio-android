package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Embed;

/**
 * * This class provides methods to access {@link com.podio.sdk.domain.Embed} objects from the API.
 *
 */
public class EmbedProvider extends Provider {

    static class Path extends Filter {

        protected Path() {
            super("embed");
        }

    }

    /**
     * Fetches the currently logged in user data.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Embed> addEmbed(Embed.Create create) {
        Path filter = new Path();
        return post(filter, create, Embed.class);
    }
}
