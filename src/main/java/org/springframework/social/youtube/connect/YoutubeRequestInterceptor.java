package org.springframework.social.youtube.connect;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.support.HttpRequestDecorator;

import java.io.IOException;

/**
 * User: lancea10
 * Date: 8/16/12
 * Time: 3:04 PM
 */
public class YoutubeRequestInterceptor implements ClientHttpRequestInterceptor {

    private String key;
    private String version;
    private String accessToken;

    public YoutubeRequestInterceptor(String developerKey, String version, String accessToken){
        this.key = developerKey;
        this.version = version;
        this.accessToken = accessToken;
    }

    public YoutubeRequestInterceptor(String developerKey, String version){
        this(developerKey, version, null);
    }

    /**
     * default version set to 2
     *
     * @param developerKey
     */
    public YoutubeRequestInterceptor(String developerKey){
        this(developerKey, "2", null);

    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequest protectedRequest = new HttpRequestDecorator(request);
        protectedRequest.getHeaders().set("X-GDATA-KEY", "key="+key);
        protectedRequest.getHeaders().set("GDATA-VERSION", version);
        if(null != accessToken && !accessToken.isEmpty()){
            protectedRequest.getHeaders().set("Authorization", "Bearer "+accessToken);
        }
        return execution.execute(protectedRequest, body);
    }
}
