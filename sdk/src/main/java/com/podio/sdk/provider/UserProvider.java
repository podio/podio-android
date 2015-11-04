
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Profile;
import com.podio.sdk.domain.User;
import com.podio.sdk.domain.UserStatus;

/**
 * Enables access to the User API end point.
 *
 */
public class UserProvider extends Provider {

    static class Path extends Filter {

        protected Path() {
            super("user");
        }

        Path withProfile() {
            addPathSegment("profile");
            return this;
        }

        Path withStatus() {
            addPathSegment("status");
            return this;
        }

        Path withProperty(String property) {
            addQueryParameter("name", property);
            return this;
        }

    }

    /**
     * Fetches the currently logged in user data.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<User> getUser() {
        Path filter = new Path();
        return get(filter, User.class);
    }

    /**
     * Fetches the currently logged in user profile.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Profile> getProfile() {
        Path filter = new Path().withProfile();
        return get(filter, Profile.class);
    }

    /**
     * Fetches the currently logged in user status.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<UserStatus> getUserStatus() {
        Path filter = new Path().withStatus();
        return get(filter, UserStatus.class);
    }

}
