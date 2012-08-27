package org.springframework.social.youtube.connect.service;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;
import org.springframework.social.youtube.model.entity.OAuth2UserProviderConnection;
import org.springframework.social.youtube.model.repository.OAuth2UserProviderConnectionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.social.youtube.connect.util.ConnectionDataAdapter;
import java.util.*;

/**
 * User: lancea10
 * Date: 8/13/12
 * Time: 6:26 PM
 */

public class ConnectionRepositoryService implements ConnectionRepository, UsersConnectionRepository {


    private String userId;

    OAuth2UserProviderConnectionRepository repository;

    ConnectionDataAdapter dataAdapter;

    private TextEncryptor textEncryptor;
    private ConnectionFactoryLocator factoryLocator;

    //null constructor for proxy instances
    public ConnectionRepositoryService(){

    }


    public  ConnectionRepositoryService(String userId, TextEncryptor textEncryptor, OAuth2UserProviderConnectionRepository repository, ConnectionDataAdapter dataAdapter, ConnectionFactoryLocator locator){
        this.textEncryptor = textEncryptor;
        this.userId = userId;
        this.dataAdapter = dataAdapter;
        this.repository = repository;
        this.factoryLocator = locator;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        return dataAdapter.convert(repository.findAllByUserId(this.userId));
    }

    @Override
    public List<Connection<?>> findConnections(String providerId) {
        List<OAuth2UserProviderConnection> connections = repository.findAllByUserIdAndProviderId(userId, providerId);
        return dataAdapter.convertAsList(connections);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
        if(providerUserIds.isEmpty()){
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }

        // The next 6 lines of stupidity are soley because someone used an over-architected data Structure
        List<String> providerList = new ArrayList<String>(providerUserIds.size());
        Iterator<Map.Entry<String, List<String>>> iterator = providerUserIds.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, List<String>> entry = iterator.next();
            providerList.add(entry.getKey());
        }
        return dataAdapter.convert(repository.findAllByProviderIdsAndUserId(this.userId, providerList));
    }

    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {
        OAuth2UserProviderConnection connection = repository.findOneByUserIdAndProviderIdAndProviderUserId(this.userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
        if(null == connection){
            connection = new OAuth2UserProviderConnection();
            connection.setProviderId(connectionKey.getProviderId());
            connection.setProviderUserId(connectionKey.getProviderUserId());
        }
        return dataAdapter.convertAsConnection(connection);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        return (Connection<A>) findPrimaryConnection(getProviderId(apiType));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        return (Connection<A>) findPrimaryConnection(getProviderId(apiType));
    }

    private Connection<?> findPrimaryConnection(String providerId){
        OAuth2UserProviderConnection connection = repository.findOneByUserIdAndProviderIdAndRank(this.userId, providerId, 1);
        if(null == connection){
            connection = new OAuth2UserProviderConnection();
            connection.setProviderId(providerId);
        }
        return dataAdapter.convertAsConnection(connection);
    }

    @Override
    public void addConnection(Connection<?> connection) {
        repository.save(dataAdapter.convert(this.userId, connection.createData()));
    }

    @Override
    @Transactional(readOnly = false)
    public void updateConnection(Connection<?> connection) {
        ConnectionData data = connection.createData();
        OAuth2UserProviderConnection jpaConnection = repository.findOneByUserIdAndProviderIdAndProviderUserId(this.userId, data.getProviderId(), data.getProviderUserId());
        jpaConnection.setSecret(data.getSecret());
        jpaConnection.setAccessToken(data.getAccessToken());
        jpaConnection.setRefreshToken(data.getRefreshToken());
        jpaConnection.setExpireTime(data.getExpireTime());
        jpaConnection.setProfileUrl(data.getProfileUrl());
        jpaConnection.setDisplayName(data.getDisplayName());
        jpaConnection.setImageUrl(data.getImageUrl());
        repository.save(jpaConnection);
    }

    @Override
    public void removeConnections(String providerId) {
        repository.deleteInBatchByUserIdAndProviderId(this.userId, providerId);
    }

    @Override
    public void removeConnection(ConnectionKey connectionKey) {
        repository.deleteOneByUserIdAndProviderIdAndProviderUserId(this.userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        return repository.getUserIdsByProviderIdAndProviderUserId(connection.getKey().getProviderId(), connection.getKey().getProviderUserId());
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        List<String> providerUserIdList = new ArrayList<String>(providerUserIds);
        return new HashSet<String>(repository.findAllByProviderIdAndProviderUserIds(providerId, providerUserIdList));
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        this.userId = userId;
        return this;
    }

    private String decrypt(String encryptedText) {
        return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
    }

    private String encrypt(String text) {
        return text != null ? textEncryptor.encrypt(text) : text;
    }
    private <A> String getProviderId(Class<A> apiType) {
        return factoryLocator.getConnectionFactory(apiType).getProviderId();
    }
}
