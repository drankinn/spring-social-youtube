/**
 * User: lance
 * Date: 3/1/12
 * Time: 3:42 PM
 */
package org.springframework.social.youtube.api.impl;

import com.google.gson.internal.Pair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.social.support.URIBuilder;
import org.springframework.social.youtube.api.*;
import org.springframework.social.youtube.api.parser.YoutubeParser;
import org.springframework.social.youtube.api.parser.json.ProfileParser;
import org.springframework.social.youtube.api.parser.json.UsernameSuggestionParser;
import org.springframework.social.youtube.connect.YoutubeRequestInterceptor;
import org.springframework.social.youtube.connect.util.GdataErrorXmlMessageConverter;
import org.springframework.social.youtube.connect.util.YoutubeModule;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private ProfileOperations profileOperations;
    private QueryOperations queryOperations;
    private ChannelOperations channelOperations;
    private EventOperations eventOperations;
    private InboxOperations inboxOperations;
    private ObjectMapper objectMapper;

    private Map<Pair<YoutubeDataFormat, Class<YoutubeDataEntry>>, YoutubeParser> parserMap = new HashMap<Pair<YoutubeDataFormat, Class<YoutubeDataEntry>>, YoutubeParser>();

    /**
     * youtube api version.  defaults to 2.1, may be overridden per api request.
     */
    private Double apiVersion;

    /**
     * youtube api format.  defaults to jsonc. may be overrided per api request
     */
    private YoutubeDataFormat format;

    /**
     * Required to use any google api.  See {@linktourl https://code.google.com/apis/youtube/dashboard/gwt/index.html} to register for a key
     */
    private String developerKey;

    private String accessToken;


    public YoutubeTemplate(String accessToken, String developerKey, YoutubeDataFormat format, Double apiVersion){
        super(accessToken);
        this.accessToken = (null != accessToken) ? accessToken : "";
        this.format = (null != format) ? format : YoutubeDataFormat.JSONC;
        this.developerKey = (null != developerKey) ? developerKey : "";
        this.apiVersion = (null != apiVersion) ? apiVersion : 2.1;
        initialize();
    }

    /**
     *
     *  version default set to 2.1
     *
     * @param accessToken
     * @param developerKey
     * @param format
     */
    public YoutubeTemplate(String accessToken, String developerKey, YoutubeDataFormat format){
       this(accessToken, developerKey, format, 2.1);
    }

    /**
     *
     * Minimal requirements for using all of youtube's apis.  format will default to jsonc
     *
     * @param accessToken
     * @param developerKey
     */
    public YoutubeTemplate(String accessToken, String developerKey){
        this(accessToken, developerKey, YoutubeDataFormat.JSONC);
    }

    /**
     *  <p>
     *      Creates a new instance of YoutubeTemplate
     *      This constructor creates a new YoutubeTemplate capable of performing unauthenticated operations against Youtube's Data API
     *  </p>
     */
    public YoutubeTemplate(String accessToken){
        this(accessToken, null);
    }

    // private helpers
    private void initialize() {
        // Wrap the request factory with a BufferingClientHttpRequestFactory so that the error handler can do repeat reads on the response.getBody()
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactorySelector.bufferRequests(getRestTemplate().getRequestFactory());
        super.setRequestFactory(requestFactory);
        configureRestTemplateInterceptors(getRestTemplate());
        initSubApis();
        initParsers();
    }

    private void initSubApis() {

        profileOperations = new ProfileTemplate(this, getRestTemplate(), isAuthorized());
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

    private void initParsers(){
        initParser(new ProfileParser(objectMapper));
        initParser(new UsernameSuggestionParser(objectMapper));



    }
    private void initParser(YoutubeParser parser){
        List<YoutubeDataFormat> formats = parser.supportedFormats();
        for( YoutubeDataFormat supportedFormat: formats){
            parserMap.put(new Pair<YoutubeDataFormat, Class<YoutubeDataEntry>>(supportedFormat, parser.supportedClass()), parser);
        }
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
    public ChannelOperations channelOperations() {
        return channelOperations;
    }

    @Override
    public EventOperations eventOperations() {
        return eventOperations;
    }

    @Override
    public InboxOperations inboxOperations() {
        return inboxOperations;
    }

    // low-level Graph API operations
    @Override
    public <T extends YoutubeDataEntry> T get(String objectId, Class<T> type) throws IOException, ParseException {
        return get(objectId, type, this.apiVersion, this.format);
    }

    @Override
    public <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, Double apiVersion) throws IOException, ParseException {
        return get(objectId, type, apiVersion, this.format);
    }

    @Override
    public <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, YoutubeDataFormat format) throws IOException, ParseException {
        return get(objectId, type, this.apiVersion, format);
    }

    @Override
    public <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, Double apiVersion, YoutubeDataFormat format) throws IOException, ParseException {
        MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<String, String>();
        return get(objectId, type, queryParameters, apiVersion, format);
    }

    @Override
    public <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters) throws IOException, ParseException {
        return get(objectId, type, queryParameters, this.apiVersion);
    }

    @Override
    public <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, Double apiVersion) throws IOException, ParseException {
        return get(objectId, type, queryParameters, apiVersion, this.format);
    }
    @Override
    public <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, YoutubeDataFormat format) throws IOException, ParseException {
        return get(objectId, type, queryParameters, apiVersion, format);
    }

    @Override
    public <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, Double apiVersion, YoutubeDataFormat format) throws IOException, ParseException {
        queryParameters.add("alt", format.toString());
        URI uri = URIBuilder.fromUri(GDATA_API_URL + objectId).queryParams(queryParameters).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("GDATA-VERSION", apiVersion.toString());
        HttpEntity<T> entity = new HttpEntity<T>(headers);
        YoutubeParser<T, Object> parser = parserMap.get(new Pair<YoutubeDataFormat, Class<T>>(format, type));
        if(null == parser){
            throw new UncategorizedApiException("Unable to deserialize type: "+type.getName()+ " with a format:  "+format, new Exception());
        }
        T result = null;
        switch(format){
            case JSON:
                JsonNode node = getRestTemplate().exchange(uri, HttpMethod.GET, entity, JsonNode.class).getBody();
                result =  (T) parser.parse(node);
                break;
        }
        return result;
    }

    @Override
    public <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type) throws IOException, ParseException {
        return getList(objectId, type, apiVersion, format);
    }
    @Override
    public <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type,  Double apiVersion) throws IOException, ParseException {
        return getList(objectId, type, apiVersion, format);
    }
    @Override
    public <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, YoutubeDataFormat format) throws IOException, ParseException {
        return getList(objectId, type,  apiVersion, format);

    }
    @Override
    public <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, Double apiVersion, YoutubeDataFormat format) throws IOException, ParseException {
        MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<String, String>();
        return getList(objectId, type, queryParameters, apiVersion, format);
    }




    @Override
    public <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters) throws IOException, ParseException {
        return getList(objectId, type, queryParameters, apiVersion, format);
    }
    @Override
    public <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, Double apiVersion) throws IOException, ParseException {
        return getList(objectId, type, queryParameters, apiVersion, format);
    }
    @Override
    public <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, YoutubeDataFormat format) throws IOException, ParseException {
        return getList(objectId, type, queryParameters, apiVersion, format);

    }
    @Override
    public <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, Double apiVersion, YoutubeDataFormat format) throws IOException, ParseException {
        queryParameters.add("alt", format.toString());
        URI uri = URIBuilder.fromUri(GDATA_API_URL + objectId).queryParams(queryParameters).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("GDATA-VERSION", apiVersion.toString());
        HttpEntity<T> entity = new HttpEntity<T>(headers);
        YoutubeParser<T, Object> parser = parserMap.get(new Pair<YoutubeDataFormat, Class<T>>(format, type));
        if(null == parser){
            throw new UncategorizedApiException("Unable to deserialize type: "+type.getName()+ " with a format:  "+format, new Exception());
        }
        List<T> result = null;
        JsonNode node=null;
        switch (format) {
            case JSON:
                node = getRestTemplate().exchange(uri, HttpMethod.GET, entity, JsonNode.class).getBody();
                break;
            case ATOM:
                break;
            case RSS:
                break;
            case JSONC:
                break;
        }
        if(null != node){
            result = (List<T>) parser.parseList(node);
        }
        return result;

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
    public void post(String objectId, String connectionName, MultiValueMap<String, String> data, Double apiVersion) {
    }

    @Override
    public void post(String objectId, String connectionName, MultiValueMap<String, String> data, YoutubeDataFormat format) {
    }

    @Override
    public void post(String objectId, String connectionName, MultiValueMap<String, String> data, Double apiVersion, YoutubeDataFormat format) {
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
        return OAuth2Version.BEARER;
    }

    protected void configureRestTemplateInterceptors(RestTemplate restTemplate) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        ClientHttpRequestInterceptor interceptor = new YoutubeRequestInterceptor(developerKey, apiVersion.toString(), accessToken);
        interceptors.add(interceptor);
        restTemplate.setInterceptors(interceptors);
        restTemplate.setErrorHandler(new YoutubeErrorHandler());
        restTemplate.getMessageConverters().add(new GdataErrorXmlMessageConverter());
    }

    @Override
    protected MappingJacksonHttpMessageConverter getJsonMessageConverter() {
        MappingJacksonHttpMessageConverter converter = super.getJsonMessageConverter();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new YoutubeModule());
        converter.setObjectMapper(objectMapper);
        return converter;
    }


    private <T> T deserializeEntryDataJson(JsonNode jsonNode, final Class<T> type){
        try{
            return objectMapper.readValue(jsonNode.get("entry"), type);
        }catch(IOException e){
            e.printStackTrace();
            throw new UncategorizedApiException("Error deserializing data from Youtube: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> deserializeDataList(JsonNode jsonNode, final Class<T> elementType) {
        try {
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, elementType);
            return (List<T>) objectMapper.readValue(jsonNode, listType);
        } catch (IOException e) {
            throw new UncategorizedApiException("Error deserializing data from Youtube: " + e.getMessage(), e);
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

    public boolean isAuthorized(){
        return !accessToken.isEmpty() && !developerKey.isEmpty();
    }

    public String  baseQueryString(){
        return "";
    }

    private HttpEntity getAuthEntity(String developerKey, Double apiVersion){
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("X-GData-Key", developerKey);
        headers.add("Authorization", "Bearer "+ accessToken);
        headers.add("Gdata-Version", apiVersion.toString().trim());
        return  new HttpEntity(headers);
    }
}
