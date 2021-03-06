package org.springframework.social.youtube.api;

import org.springframework.social.youtube.api.impl.YoutubeProfile;

import java.util.List;

/**
 * User: lance
 * Date: 3/1/12
 * Time: 3:57 PM
 */
public interface ProfileOperations {
    
    YoutubeProfile getProfile();
    
    YoutubeProfile getProfile(String userId);

    List<YoutubeProfile> search(String query);

    List getUsernameSuggestions(String desiredName);
}
