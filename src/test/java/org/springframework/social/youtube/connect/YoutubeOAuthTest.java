package org.springframework.social.youtube.connect;

import org.junit.Test;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;

/**
 * User: lancea10
 * Date: 7/24/12
 * Time: 7:59 PM
 */

public class YoutubeOAuthTest {

    private String clientId="845679014791.apps.googleusercontent.com";

    private String clientSecret="IpAprSrZdRwhMpV0VPbNg7yc";



    @Test
    public void buildOAuth2AuthorizeUrl(){
        YoutubeOAuth2Template oAuth2Template = new YoutubeOAuth2Template(clientId, clientSecret);
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        OAuth2Parameters oAuth2Parameters = new OAuth2Parameters(parameters);
        oAuth2Parameters.setScope("test");
        oAuth2Parameters.setRedirectUri("https://spring-social-youtube.com/oauth2callback");
        String url = oAuth2Template.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
        assertEquals("https://accounts.google.com/o/oauth2/auth?client_id=845679014791.apps.googleusercontent.com&response_type=code&access_type=offline&approval_prompt=force&scope=test&redirect_uri=https%3A%2F%2Fspring-social-youtube.com%2Foauth2callback", url);

    }

}
