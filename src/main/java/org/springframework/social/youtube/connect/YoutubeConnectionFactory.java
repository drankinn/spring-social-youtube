package org.springframework.social.youtube.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.youtube.api.Youtube;
import org.springframework.social.youtube.connect.util.YoutubeAdapter;

/**
 * Created by IntelliJ IDEA.
 * User: lance
 * Date: 3/1/12
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class YoutubeConnectionFactory extends OAuth2ConnectionFactory<Youtube> {


    public YoutubeConnectionFactory(String clientId, String clientSecret, String developerKey) {
        super("youtube", new YoutubeServiceProvider(clientId, clientSecret, developerKey), new YoutubeAdapter());
    }
}
