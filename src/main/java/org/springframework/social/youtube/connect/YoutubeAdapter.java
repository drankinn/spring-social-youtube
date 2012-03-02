/**
 * User: lance
 * Date: 3/1/12
 * Time: 3:12 PM
 */
package org.springframework.social.youtube.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.youtube.api.Youtube;

public class YoutubeAdapter implements ApiAdapter<Youtube> {
    @Override
    public boolean test(Youtube api) {
        try{
            api.profileOPerations().getProfile();
            return true;
        }catch(ApiException e){
            return false;
        }

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
