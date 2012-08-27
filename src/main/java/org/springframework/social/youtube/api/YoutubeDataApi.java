package org.springframework.social.youtube.api;

import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lance
 * Date: 3/1/12
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public interface YoutubeDataApi {


    /**
     * Fetches an object, extracting it into the given Java type
     * Requires appropriate permission to fetch the object.
     * @param objectId the Youtube object's ID
     * @param type the Java type to fetch
     * @return an Java object representing the requested Youtube object.
     */
    <T extends YoutubeDataEntry> T get(String objectId, Class<T> type) throws IOException, ParseException;
    <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, Double apiVersion) throws IOException, ParseException;
    <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, YoutubeDataFormat format) throws IOException, ParseException;
    <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, Double apiVersion, YoutubeDataFormat format) throws IOException, ParseException;

    <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type) throws IOException, ParseException;
    <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, Double apiVersion) throws IOException, ParseException;
    <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, YoutubeDataFormat format) throws IOException, ParseException;
    <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, Double apiVersion, YoutubeDataFormat format) throws IOException, ParseException;
    /**
     * Fetches an object, extracting it into the given Java type
     * Requires appropriate permission to fetch the object.
     * @param objectId the Youtube object's ID
     * @param type the Java type to fetch
     * @param queryParameters query parameters to include in the request
     * @return an Java object representing the requested Youtube object.
     */
    <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters) throws IOException, ParseException;
    <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, Double apiVersion) throws IOException, ParseException;
    <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, YoutubeDataFormat format) throws IOException, ParseException;
    <T extends YoutubeDataEntry> T get(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, Double apiVersion, YoutubeDataFormat format) throws IOException, ParseException;

    <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters) throws IOException, ParseException;
    <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, Double apiVersion) throws IOException, ParseException;
    <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, YoutubeDataFormat format) throws IOException, ParseException;
    <T extends YoutubeDataEntry> List<T> getList(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters, Double apiVersion, YoutubeDataFormat format) throws IOException, ParseException;



    /**
     * Fetches connections, extracting them into a collection of the given Java type
     * Requires appropriate permission to fetch the object connection.
     * @param objectId the ID of the object to retrieve the connections for.
     * @param connectionName the connection name.
     * @param type the Java type of each connection.
     * @return a list of Java objects representing the Youtube objects in the connections.
     */
    <T> List<T> fetchConnections(String objectId, String connectionName, Class<T> type, String... fields);
    <T> List<T> fetchConnections(String objectId, String connectionName, Class<T> type, MultiValueMap<String, String> queryParameters);

    /**
     * Publishes data to an object's connection.
     * Requires appropriate permission to publish to the object connection.
     * @param objectId the object ID to publish to.
     * @param connectionName the connection name to publish to.
     * @param data the data to publish to the connection.
     * @return the ID of the newly published object.
     */
    String publish(String objectId, String connectionName, MultiValueMap<String, Object> data);

    /**
     * Publishes data to an object's connection.
     * Requires appropriate permission to publish to the object connection.
     * This differs from publish() only in that it doesn't attempt to extract the ID from the response.
     * This is because some publish operations do not return an ID in the response.
     * @param objectId the object ID to publish to.
     * @param connectionName the connection name to publish to.
     * @param data the data to publish to the connection.
     */
    void post(String objectId, String connectionName, MultiValueMap<String, String> data);
    void post(String objectId, String connectionName, MultiValueMap<String, String> data, Double apiVersion);
    void post(String objectId, String connectionName, MultiValueMap<String, String> data, YoutubeDataFormat format);
    void post(String objectId, String connectionName, MultiValueMap<String, String> data, Double apiVersion, YoutubeDataFormat format);

    /**
     * Deletes an object.
     * Requires appropriate permission to delete the object.
     * @param objectId the object ID
     */
    void delete(String objectId);
    //void delete(String objectId, Double apiVersion);
    //void delete(String objectId, YoutubeDataFormat format);
    //void delete(String objectId, Double apiVersion, YoutubeDataFormat format);

    /**
     * Deletes an object connection.
     * Requires appropriate permission to delete the object connection.
     * @param objectId the object ID
     * @param connectionName the connection name
     */
    void delete(String objectId, String connectionName);

    static final String GDATA_API_URL = "http://gdata.youtube.com";
}
