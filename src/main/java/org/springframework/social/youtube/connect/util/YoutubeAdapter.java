/**
 * User: lance
 * Date: 3/1/12
 * Time: 3:12 PM
 */
package org.springframework.social.youtube.connect.util;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.youtube.api.Youtube;
import org.springframework.social.youtube.api.impl.YoutubeProfile;

public class YoutubeAdapter implements ApiAdapter<Youtube> {

    @Override
    public boolean test(Youtube youtube) {
        try{
            youtube.profileOPerations().getProfile();
            return true;
        }catch(ApiException e){
            return false;
        }

    }

    @Override
    public void setConnectionValues(Youtube youtube, ConnectionValues values) {
        YoutubeProfile profile = youtube.profileOPerations().getProfile();
        values.setProviderUserId(profile.getId());
        values.setDisplayName(profile.getFirstname());
        values.setProfileUrl("http://youtube.com/"+profile.getUsername());
        values.setImageUrl(profile.getThumbnail());

    }

    @Override
    public UserProfile fetchUserProfile(Youtube youtube) {
        YoutubeProfile profile = youtube.profileOPerations().getProfile();
        return new UserProfileBuilder()
                   .setUsername(profile.getUsername())
                   .setFirstName(profile.getFirstname())
                   .setLastName(profile.getLastname())
                   .setEmail(profile.getEmail())
                   .setName(profile.getFirstname()+" "+ profile.getLastname())
                   .build();
    }

    @Override
    public void updateStatus(Youtube youtube, String message) {

    }
}
