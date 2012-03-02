/**
 * User: lance
 * Date: 3/1/12
 * Time: 5:37 PM
 */
package org.springframework.social.youtube.api.impl;

import org.springframework.social.youtube.api.ProfileOperations;
import org.springframework.social.youtube.api.YoutubeDataApi;
import org.springframework.social.youtube.api.YoutubeProfile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author lance
 *         <p/>
 *         <p>
 *         ${CLASSNAME}
 *         </p>
 */
public class ProfileTemplate extends AbstractYoutubeOperations implements ProfileOperations{

    private RestTemplate restTemplate;
    private YoutubeDataApi api;

    public ProfileTemplate(YoutubeDataApi api, RestTemplate restTemplate, boolean isAuthorized) {
        super(isAuthorized);
        this.api = api;
        this.restTemplate = restTemplate;
    }

    @Override
    public YoutubeProfile getProfile() {
        return null;
    }

    @Override
    public YoutubeProfile getProfile(String userId) {
        return api.fetchObject(userId, YoutubeProfile.class);
    }

    @Override
    public List<YoutubeProfile> search(String query) {
        requireAuthorization();
        MultiValueMap<String, String> queryMap = new LinkedMultiValueMap<String, String>();
        queryMap.add("q", query);
        queryMap.add("type", "user");
        return api.fetchConnections("search", null, YoutubeProfile.class, queryMap);
    }
}
