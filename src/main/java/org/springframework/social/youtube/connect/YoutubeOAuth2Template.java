package org.springframework.social.youtube.connect;

import org.springframework.http.*;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: lance
 * Date: 3/1/12
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class YoutubeOAuth2Template extends OAuth2Template {
    private String authorizeUrl;

    public YoutubeOAuth2Template(String clientId, String clientSecret) {
        super(clientId, clientSecret,
                "https://accounts.google.com/o/oauth2/auth",
                "https://accounts.google.com/o/oauth2/token");

        String clientInfo = "?client_id=" + formEncode(clientId);
        this.authorizeUrl = "https://accounts.google.com/o/oauth2/auth" + clientInfo;
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

    public String buildAuthorizeUrl(GrantType grantType, OAuth2Parameters parameters) {
        return buildAuthUrl(authorizeUrl, grantType, parameters);
    }

    private String buildAuthUrl(String baseAuthUrl, GrantType grantType, OAuth2Parameters parameters) {
        StringBuilder authUrl = new StringBuilder(baseAuthUrl);
        if (grantType == GrantType.AUTHORIZATION_CODE) {
            authUrl.append('&').append("response_type").append('=').append("code");
            authUrl.append('&').append("access_type").append('=').append("offline");
            authUrl.append('&').append("approval_prompt").append('=').append("force");
        } else if (grantType == GrantType.IMPLICIT_GRANT) {
            authUrl.append('&').append("response_type").append('=').append("token");
        }
        for (Iterator<Map.Entry<String, List<String>>> additionalParams = parameters.entrySet().iterator(); additionalParams.hasNext();) {
            Map.Entry<String, List<String>> param = additionalParams.next();
            String name = formEncode(param.getKey());
            for (Iterator<String> values = param.getValue().iterator(); values.hasNext();) {
                authUrl.append('&').append(name).append('=').append(formEncode(values.next()));
            }
        }
        return authUrl.toString();
    }
    private String formEncode(String data) {
        try {
            return URLEncoder.encode(data, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            // should not happen, UTF-8 is always supported
            throw new IllegalStateException(ex);
        }
    }
}
