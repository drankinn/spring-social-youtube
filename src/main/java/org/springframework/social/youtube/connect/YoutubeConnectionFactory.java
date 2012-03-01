package org.springframework.social.youtube.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * Created by IntelliJ IDEA.
 * User: lance
 * Date: 3/1/12
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class YoutubeConnectionFactory extends OAuth2ConnectionFactory {


    public YoutubeConnectionFactory(String clientId, String clientSecret) {
        super("youtube", new YoutubeServiceProvider(clientId, clientSecret), new YoutubeAdapter());
    }
}
