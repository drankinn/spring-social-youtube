package org.springframework.social.youtube.connect;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.youtube.api.Youtube;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * User: lancea10
 * Date: 8/22/12
 * Time: 11:20 AM
 */

public class YoutubeConnectionFactoryTest {

    private String clientId="845679014791.apps.googleusercontent.com";

    private String clientSecret="IpAprSrZdRwhMpV0VPbNg7yc";

    private String developerKey="AI39si7_cJEiCV0w4yaM0EyARllt-wVLcNdrYOUjVij89iQn35mWLGq0-i7Z9XgIvnsRoinDN9EhyfUXBf8nP5F18Qx6S8B8-A";

    private Youtube youtube = Mockito.mock(Youtube.class);


    @Test
    public void ConnectionFactoryRegistryTest(){

        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new YoutubeConnectionFactory(clientId, clientSecret, developerKey));
        ConnectionFactory<?> youtube = registry.getConnectionFactory("youtube");
        assertEquals("youtube", youtube.getProviderId());
        ConnectionFactory<Youtube> youtube2 = registry.getConnectionFactory(Youtube.class);
        assertEquals("youtube", youtube2.getProviderId());
    }

    @Test
    public void ConnectionFactorySetupTest(){

        OAuth2Connection<Youtube> oAuth2Connection= mock(OAuth2Connection.class);
        YoutubeConnectionFactory factory = new YoutubeConnectionFactory(clientId, clientSecret, developerKey);
        YoutubeConnectionFactory factorySpy = spy(factory);


        AccessGrant accessGrant = new AccessGrant("test token", "test scope", "test refresh token", 1);

        doReturn(oAuth2Connection).when(factorySpy).createConnection(accessGrant);
        Connection<Youtube> connection = factorySpy.createConnection(accessGrant);
        verify(factorySpy).createConnection(accessGrant);

    }
}
