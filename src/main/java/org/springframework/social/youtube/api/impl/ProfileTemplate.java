/**
 * User: lance
 * Date: 3/1/12
 * Time: 5:37 PM
 */
package org.springframework.social.youtube.api.impl;

import org.springframework.social.youtube.api.ProfileOperations;
import org.springframework.social.youtube.api.YoutubeDataApi;
import org.springframework.social.youtube.api.YoutubeDataFormat;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author lance
 *         <p/>
 *         <p>
 *         ${CLASSNAME}
 *         </p>
 */
public class ProfileTemplate extends AbstractYoutubeOperations implements ProfileOperations{

    private YoutubeDataApi api;

    public ProfileTemplate(YoutubeDataApi api, RestTemplate restTemplate, boolean isAuthorized) {
        super(isAuthorized);
        this.api = api;
    }

    @Override
    public YoutubeProfile getProfile() {
        requireAuthorization();
        try {
            return api.get("/feeds/api/users/default", YoutubeProfile.class, 2.1, YoutubeDataFormat.JSON);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public YoutubeProfile getProfile(String userId) {
        requireAuthorization();
        try {
            return api.get("/feeds/api/users/" + userId, YoutubeProfile.class, 2.1, YoutubeDataFormat.JSON);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<YoutubeProfile> search(String query) {
        requireAuthorization();
        MultiValueMap<String, String> queryMap = new LinkedMultiValueMap<String, String>();
        queryMap.add("q", query);
        queryMap.add("type", "user");
        return api.fetchConnections("search", null, YoutubeProfile.class, queryMap);
    }

    @Override
    public List<UsernameSuggestion> getUsernameSuggestions(String desiredName) {
        requireAuthorization();
        try {
            MultiValueMap<String, String> queryMap = new LinkedMultiValueMap<String, String>();
            queryMap.add("hint", desiredName);
            return api.getList("/feeds/api/suggest/username", UsernameSuggestion.class, queryMap, 2.1, YoutubeDataFormat.JSON);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
