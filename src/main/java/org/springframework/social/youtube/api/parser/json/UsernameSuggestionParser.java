package org.springframework.social.youtube.api.parser.json;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.social.youtube.api.impl.UsernameSuggestion;

/**
 * User: lancea10
 * Date: 8/27/12
 * Time: 10:13 AM
 */
public class UsernameSuggestionParser extends YoutubeJsonParser<UsernameSuggestion> {
    public UsernameSuggestionParser(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Class<UsernameSuggestion> supportedClass() {
        return UsernameSuggestion.class;
    }

    public UsernameSuggestion parseInternal(JsonNode node){
        UsernameSuggestion usernameSuggestion = new UsernameSuggestion();
        usernameSuggestion.setTitle(parseT("title", node));
        return usernameSuggestion;
    }

}
