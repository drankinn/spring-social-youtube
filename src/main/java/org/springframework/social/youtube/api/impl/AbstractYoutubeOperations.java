package org.springframework.social.youtube.api.impl;

import org.springframework.social.MissingAuthorizationException;

public class AbstractYoutubeOperations {

    private final boolean isAuthorized;

    public AbstractYoutubeOperations(boolean isAuthorized){
        this.isAuthorized= isAuthorized;
    }

    protected void requireAuthorization(){
        if(!isAuthorized){
            throw new MissingAuthorizationException();
        }
    }
}
