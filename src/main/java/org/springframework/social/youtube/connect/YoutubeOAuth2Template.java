package org.springframework.social.youtube.connect;

import org.springframework.http.*;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: lance
 * Date: 3/1/12
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class YoutubeOAuth2Template extends OAuth2Template {

    public YoutubeOAuth2Template(String clientId, String clientSecret) {
        super(clientId, clientSecret,
                "https://accounts.google.com/o/oauth2/auth",
                "https://accounts.google.com/o/oauth2/token");
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
        ResponseEntity<Map> responseEntity = getRestTemplate().exchange(accessTokenUrl, HttpMethod.POST, requestEntity, Map.class);
        Map<String, Object> responseMap = responseEntity.getBody();
        return extractAccessGrant(responseMap);
    }

    private AccessGrant extractAccessGrant(Map<String, Object> result) {
        String accessToken = (String) result.get("access_token");
        String scope = (String) result.get("scope");
        String refreshToken = (String) result.get("refresh_token");
        Integer expiresIn = (Integer) result.get("expires_in");
        return createAccessGrant(accessToken, scope, refreshToken, expiresIn, result);
    }
}
