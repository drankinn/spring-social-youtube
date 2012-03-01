/**
 * User: lance
 * Date: 3/1/12
 * Time: 3:42 PM
 */
package org.springframework.social.youtube.api.impl;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.social.support.URIBuilder;
import org.springframework.social.youtube.api.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author lance
 *         <p/>
 *         <p>
 *         ${CLASSNAME}
 *         </p>
 */
public class YoutubeTemplate extends AbstractOAuth2ApiBinding implements Youtube {

    private PlaylistOperations playlistOperations;
    private VideoOperations videoOperations;
    private FeedOperations feedOperations;
    private ProfileOperations profileOperations;
    private QueryOperations queryOperations;
    private ObjectMapper objectMapper;


    /**
     *  <p>
     *      Creates a new instance of YoutubeTemplate
     *      This constructor creates a new YoutubeTemplate capable of performing unauthenticated operations against Youtube's Data API
     *  </p>
     */
    public YoutubeTemplate(){
        initialize();
    }
    
    public YoutubeTemplate(String accessToken){
        super(accessToken);
        initialize();
    }

    @Override
    public void setRequestFactory(ClientHttpRequestFactory requestFactory){
          // Wrap the request factory with a BufferingClientHttpRequestFactory so that the error handler can do repeat reads on the response.getBody()
		super.setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(requestFactory));
    }


    // private helpers
    private void initialize() {
        // Wrap the request factory with a BufferingClientHttpRequestFactory so that the error handler can do repeat reads on the response.getBody()
        super.setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(getRestTemplate().getRequestFactory()));
        initSubApis();
    }

    private void initSubApis() {


        /**userOperations = new UserTemplate(this, getRestTemplate(), isAuthorized());
        placesOperations = new PlacesTemplate(this, isAuthorized());
        friendOperations = new FriendTemplate(this, getRestTemplate(), isAuthorized());
        feedOperations = new FeedTemplate(this, getRestTemplate(), objectMapper, isAuthorized());
        commentOperations = new CommentTemplate(this, isAuthorized());
        likeOperations = new LikeTemplate(this, isAuthorized());
        eventOperations = new EventTemplate(this, isAuthorized());
        mediaOperations = new MediaTemplate(this, getRestTemplate(), isAuthorized());
        groupOperations = new GroupTemplate(this, isAuthorized());
        pageOperations = new PageTemplate(this, isAuthorized());
        fqlOperations = new FqlTemplate(this, isAuthorized());
        questionOperations = new QuestionTemplate(this, isAuthorized());
         */
    }

    @Override
    public PlaylistOperations playlistOperations() {
        return playlistOperations;
    }

    @Override
    public VideoOperations videoOperations() {
        return videoOperations;
    }

    @Override
    public ProfileOperations profileOPerations() {
        return profileOperations;
    }

    @Override
    public QueryOperations queryOperations() {
        return queryOperations;
    }

    @Override
    public FeedOperations feedOperations() {
        return feedOperations;
    }


    // low-level Graph API operations
    @Override
    public <T> T fetchObject(String objectId, Class<T> type) {
        URI uri = URIBuilder.fromUri(GDATA_API_URL + objectId).build();
        return getRestTemplate().getForObject(uri, type);
    }

    @Override
    public <T> T fetchObject(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters) {
        URI uri = URIBuilder.fromUri(GDATA_API_URL + objectId).queryParams(queryParameters).build();
        return getRestTemplate().getForObject(uri, type);
    }

    @Override
    public <T> List<T> fetchConnections(String objectId, String connectionType, Class<T> type, String... fields) {
        MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<String, String>();
        if(fields.length > 0) {
            String joinedFields = join(fields);
            queryParameters.set("fields", joinedFields);
        }
        return fetchConnections(objectId, connectionType, type, queryParameters);
    }

    @Override
    public <T> List<T> fetchConnections(String objectId, String connectionType, Class<T> type, MultiValueMap<String, String> queryParameters) {
        String connectionPath = connectionType != null && connectionType.length() > 0 ? "/" + connectionType : "";
        URIBuilder uriBuilder = URIBuilder.fromUri(GDATA_API_URL + objectId + connectionPath).queryParams(queryParameters);
        JsonNode dataNode = getRestTemplate().getForObject(uriBuilder.build(), JsonNode.class);
        return deserializeDataList(dataNode.get("data"), type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String publish(String objectId, String connectionType, MultiValueMap<String, Object> data) {
        MultiValueMap<String, Object> requestData = new LinkedMultiValueMap<String, Object>(data);
        URI uri = URIBuilder.fromUri(GDATA_API_URL + objectId + "/" + connectionType).build();
        Map<String, Object> response = getRestTemplate().postForObject(uri, requestData, Map.class);
        return (String) response.get("id");
    }

    @Override
    public void post(String objectId, String connectionType, MultiValueMap<String, String> data) {
        URI uri = URIBuilder.fromUri(GDATA_API_URL + objectId + "/" + connectionType).build();
        getRestTemplate().postForObject(uri, new LinkedMultiValueMap<String, String>(data), String.class);
    }

    @Override
    public void delete(String objectId) {
        LinkedMultiValueMap<String, String> deleteRequest = new LinkedMultiValueMap<String, String>();
        deleteRequest.set("method", "delete");
        URI uri = URIBuilder.fromUri(GDATA_API_URL + objectId).build();
        getRestTemplate().postForObject(uri, deleteRequest, String.class);
    }

    @Override
    public void delete(String objectId, String connectionType) {
        LinkedMultiValueMap<String, String> deleteRequest = new LinkedMultiValueMap<String, String>();
        deleteRequest.set("method", "delete");
        URI uri = URIBuilder.fromUri(GDATA_API_URL + objectId + "/" + connectionType).build();
        getRestTemplate().postForObject(uri, deleteRequest, String.class);
    }

    @Override
    protected OAuth2Version getOAuth2Version() {
        return OAuth2Version.DRAFT_10;
    }

    @Override
    protected void configureRestTemplate(RestTemplate restTemplate) {
        restTemplate.setErrorHandler(new YoutubeErrorHandler());
    }

    @Override
    protected MappingJacksonHttpMessageConverter getJsonMessageConverter() {
        MappingJacksonHttpMessageConverter converter = super.getJsonMessageConverter();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new YoutubeModule());
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> deserializeDataList(JsonNode jsonNode, final Class<T> elementType) {
        try {
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, elementType);
            return (List<T>) objectMapper.readValue(jsonNode, listType);
        } catch (IOException e) {
            throw new UncategorizedApiException("Error deserializing data from Facebook: " + e.getMessage(), e);
        }
    }


    private String join(String[] strings) {
        StringBuilder builder = new StringBuilder();
        if(strings.length > 0) {
            builder.append(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                builder.append("," + strings[i]);
            }
        }
        return builder.toString();
    }

}
