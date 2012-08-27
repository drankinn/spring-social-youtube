package org.springframework.social.youtube.api;

/**
 * User: lancea10
 * Date: 8/21/12
 * Time: 11:43 AM
 */
public enum YoutubeDataFormat {

    ATOM, RSS, JSON, JSONC;

    public String toString(){
        return this.name().toLowerCase();
    }
}
