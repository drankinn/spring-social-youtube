package org.springframework.social.youtube.api;

import org.springframework.social.ApiBinding;

public interface Youtube extends YoutubeDataApi, ApiBinding {

    PlaylistOperations playlistOperations();

    VideoOperations videoOperations();

    ProfileOperations profileOPerations();

    QueryOperations queryOperations();

    FeedOperations feedOperations();
}
