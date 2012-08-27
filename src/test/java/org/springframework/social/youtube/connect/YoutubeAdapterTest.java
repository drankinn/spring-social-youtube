package org.springframework.social.youtube.connect;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.youtube.api.ProfileOperations;
import org.springframework.social.youtube.api.Youtube;
import org.springframework.social.youtube.api.impl.YoutubeProfile;
import org.springframework.social.youtube.connect.util.YoutubeAdapter;

import static org.junit.Assert.assertEquals;

/**
 * User: lancea10
 * Date: 8/14/12
 * Time: 2:29 PM
 */
public class YoutubeAdapterTest {

    private YoutubeAdapter apiAdapter = new YoutubeAdapter();

    private Youtube youtube = Mockito.mock(Youtube.class);

    @Test
    public void fetchProfile(){
        ProfileOperations profileOperations = Mockito.mock(ProfileOperations.class);
        Mockito.when(youtube.profileOPerations()).thenReturn(profileOperations);
        Mockito.when(profileOperations.getProfile()).thenReturn(new YoutubeProfile("123", "Lance", "Andersen", "drankinn", 32, "techlance@gmail.com", "testImg.png"));

        UserProfile profile = apiAdapter.fetchUserProfile(youtube);
        assertEquals("Lance Andersen", profile.getName());
        assertEquals("Lance", profile.getFirstName());
        assertEquals("Andersen", profile.getLastName());
        assertEquals("techlance@gmail.com", profile.getEmail());
        assertEquals("drankinn", profile.getUsername());
    }
}
