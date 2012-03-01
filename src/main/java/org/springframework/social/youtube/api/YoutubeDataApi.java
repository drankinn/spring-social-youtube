package org.springframework.social.youtube.api;

import org.springframework.util.MultiValueMap;

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
    <T> T fetchObject(String objectId, Class<T> type);

    /**
     * Fetches an object, extracting it into the given Java type
     * Requires appropriate permission to fetch the object.
     * @param objectId the Youtube object's ID
     * @param type the Java type to fetch
     * @param queryParameters query parameters to include in the request
     * @return an Java object representing the requested Youtube object.
     */
    <T> T fetchObject(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters);

    /**
     * Fetches connections, extracting them into a collection of the given Java type
     * Requires appropriate permission to fetch the object connection.
     * @param objectId the ID of the object to retrieve the connections for.
     * @param connectionName the connection name.
     * @param type the Java type of each connection.
     * @param fields the fields to include in the response.
     * @return a list of Java objects representing the Youtube objects in the connections.
     */
    <T> List<T> fetchConnections(String objectId, String connectionName, Class<T> type, String... fields);

    /**
     * Fetches connections, extracting them into a collection of the given Java type
     * Requires appropriate permission to fetch the object connection.
     * @param objectId the ID of the object to retrieve the connections for.
     * @param connectionName the connection name.
     * @param type the Java type of each connection.
     * @param queryParameters query parameters to include in the request
     * @return a list of Java objects representing the Youtube objects in the connections.
     */
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

    /**
     * Deletes an object.
     * Requires appropriate permission to delete the object.
     * @param objectId the object ID
     */
    void delete(String objectId);

    /**
     * Deletes an object connection.
     * Requires appropriate permission to delete the object connection.
     * @param objectId the object ID
     * @param connectionName the connection name
     */
    void delete(String objectId, String connectionName);

    static final String GDATA_API_URL = "https://gdata.youtube.com/";
}
