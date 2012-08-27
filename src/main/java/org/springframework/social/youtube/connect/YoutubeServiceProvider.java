package org.springframework.social.youtube.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.youtube.api.Youtube;
import org.springframework.social.youtube.api.impl.YoutubeTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: lance
 * Date: 3/1/12
 * Time: 12:12 PM
  */
public class YoutubeServiceProvider extends AbstractOAuth2ServiceProvider<Youtube> {

     private String developerKey;
    /**
     * Create a new {@link org.springframework.social.oauth2.OAuth2ServiceProvider}.
     *
     * @param clientId String containing the google developer account's client ID for this application
     * @param clientSecret String containing the google developer account's client Secret for this application
     * @see <a href="https://code.google.com/apis/console#access">Api Console</a> to create these parameters.
     */
    public YoutubeServiceProvider(String clientId, String clientSecret, String developerKey) {
        super(new YoutubeOAuth2Template(clientId, clientSecret));
        this.developerKey = developerKey;
    }

    @Override
    public Youtube getApi(String accessToken) {
        return new YoutubeTemplate(accessToken, developerKey);
    }
}
