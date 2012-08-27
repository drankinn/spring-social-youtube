package org.springframework.social.youtube.api.parser.json;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.social.youtube.api.impl.YoutubeProfile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * User: lancea10
 * Date: 8/24/12
 * Time: 11:38 AM
 */
public class ProfileParser extends YoutubeJsonParser<YoutubeProfile> {


    public ProfileParser(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Class<YoutubeProfile> supportedClass() {
        return YoutubeProfile.class;
    }

    public YoutubeProfile parseInternal(JsonNode node) throws IOException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd'T'hh:mm:ss.SSS'Z'");

        YoutubeProfile profile = new YoutubeProfile();
        profile.setId(parseT("yt$userId", node));
        try{
        profile.setAge(Integer.parseInt(parseT("yt$age", node)));
        }catch(NumberFormatException ex){

        }
        profile.setEmail("");
        profile.setFirstname(parseArray("yt$username", "display", node));
        profile.setLastname("");
        profile.setPublished(format.parse(parseT("published", node)));
        profile.setThumbnail(parseArray("media$thumbnail", "url", node));
        profile.setUpdated(format.parse(parseT("updated", node)));
        profile.setUsername(parseT("yt$username", node));
        return profile;
    }



}
