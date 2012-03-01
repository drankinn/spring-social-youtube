/**
 * User: lance
 * Date: 3/1/12
 * Time: 4:16 PM
 */
package org.springframework.social.youtube.api.impl;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * @author lance
 *         <p/>
 *         <p>
 *         ${CLASSNAME}
 *         </p>
 */
public class YoutubeErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

    }
}
