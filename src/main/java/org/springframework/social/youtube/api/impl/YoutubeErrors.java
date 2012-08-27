package org.springframework.social.youtube.api.impl;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * User: lancea10
 * Date: 8/27/12
 * Time: 3:13 PM
 */
@XmlRootElement(name = "errors")
public class YoutubeErrors {

    private List<YoutubeError> error;

    public List<YoutubeError> getError() {
        return error;
    }

    public void setError(List<YoutubeError> error) {
        this.error = error;
    }
}
