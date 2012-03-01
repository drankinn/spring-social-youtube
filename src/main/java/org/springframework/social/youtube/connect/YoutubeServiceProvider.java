package org.springframework.social.youtube.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.youtube.Youtube;

/**
 * Created by IntelliJ IDEA.
 * User: lance
 * Date: 3/1/12
 * Time: 12:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class YoutubeServiceProvider extends AbstractOAuth2ServiceProvider<Youtube> {


    /**
     * Create a new {@link org.springframework.social.oauth2.OAuth2ServiceProvider}.
     *
     * @param clientId String containing the google developer account's client ID for this application
     * @param clientSecret String containing the google developer account's client Secret for this application
     * @see <a href="https://code.google.com/apis/console#access">Api Console</a> to create these parameters.
     */
    public YoutubeServiceProvider(String clientId, String clientSecret) {
        super(new YoutubeOAuth2Template(clientId, clientSecret));
    }

    @Override
    public Youtube getApi(String accessToken) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
