package org.springframework.social.youtube.api.impl;

import org.springframework.social.youtube.api.YoutubeDataEntry;

import java.io.Serializable;

/**
 * User: lancea10
 * Date: 8/27/12
 * Time: 10:14 AM
 */
public class UsernameSuggestion implements YoutubeDataEntry, Serializable {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
