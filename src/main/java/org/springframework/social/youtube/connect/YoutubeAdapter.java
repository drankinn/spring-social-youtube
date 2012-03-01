package org.springframework.social.youtube.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.youtube.api.Youtube;

/**
 * Created by IntelliJ IDEA.
 * User: lance
 * Date: 3/1/12
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class YoutubeAdapter implements ApiAdapter<Youtube> {
    @Override
    public boolean test(Youtube api) {
        return false;
    }

    @Override
    public void setConnectionValues(Youtube api, ConnectionValues values) {

    }

    @Override
    public UserProfile fetchUserProfile(Youtube api) {
        return null;
    }

    @Override
    public void updateStatus(Youtube api, String message) {

    }
}
