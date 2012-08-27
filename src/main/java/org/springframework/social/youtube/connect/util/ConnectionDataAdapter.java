package org.springframework.social.youtube.connect.util;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.youtube.model.entity.OAuth2UserProviderConnection;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * User: lancea10
 * Date: 8/22/12
 * Time: 5:06 PM
 */
public class ConnectionDataAdapter {

    ConnectionFactoryLocator factoryLocator;

    public ConnectionDataAdapter(ConnectionFactoryLocator factoryLocator){
        this.factoryLocator = factoryLocator;
    }

    public OAuth2UserProviderConnection convert(String userId, ConnectionData data){
        if(userId.isEmpty()){
            return null;
        }
        OAuth2UserProviderConnection jpaConnection = new OAuth2UserProviderConnection();
        if(null == data){
            return jpaConnection;
        }
        jpaConnection.setUserId(userId);
        jpaConnection.setSecret(data.getSecret());
        jpaConnection.setAccessToken(data.getAccessToken());
        jpaConnection.setRefreshToken(data.getRefreshToken());
        jpaConnection.setExpireTime(data.getExpireTime());
        jpaConnection.setProviderId(data.getProviderId());
        jpaConnection.setProviderUserId(data.getProviderUserId());
        jpaConnection.setProfileUrl(data.getProfileUrl());
        jpaConnection.setDisplayName(data.getDisplayName());
        jpaConnection.setImageUrl(data.getImageUrl());
        jpaConnection.setRank(1);
        return jpaConnection;
    }

    public ConnectionData convert(OAuth2UserProviderConnection jpaConnection){
        if(null == jpaConnection){
            return null;
        }
        return  new ConnectionData(jpaConnection.getProviderId(), jpaConnection.getProviderUserId(), jpaConnection.getDisplayName(), jpaConnection.getProfileUrl(), jpaConnection.getImageUrl(),
                jpaConnection.getAccessToken(), jpaConnection.getSecret(), jpaConnection.getRefreshToken(), jpaConnection.getExpireTime());
    }

    public Connection<?> convertAsConnection(OAuth2UserProviderConnection jpaConnection){
        if(null == jpaConnection){
            return null;
        }
        ConnectionData data = new ConnectionData(jpaConnection.getProviderId(), jpaConnection.getProviderUserId(), jpaConnection.getDisplayName(), jpaConnection.getProfileUrl(), jpaConnection.getImageUrl(),
                jpaConnection.getAccessToken(), jpaConnection.getSecret(), jpaConnection.getRefreshToken(), jpaConnection.getExpireTime());
        return factoryLocator.getConnectionFactory(data.getProviderId()).createConnection(data);
    }



    public MultiValueMap<String, Connection<?>> convert(List<OAuth2UserProviderConnection> jpaConnections){
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        if(jpaConnections.isEmpty()){
            return connections;
        }
        ListIterator<OAuth2UserProviderConnection> iterator = jpaConnections.listIterator();
        Set<String> registeredProviderIds = factoryLocator.registeredProviderIds();
        ConnectionData data = null;
        while (iterator.hasNext()){
           data = convert(iterator.next());
           if(registeredProviderIds.contains(data.getProviderId())){
                ConnectionFactory factory = factoryLocator.getConnectionFactory(data.getProviderId());
                connections.add(data.getProviderId(),factory.createConnection(data));
           }
        }
        return connections;
    }

    public List<Connection<?>> convertAsList(List<OAuth2UserProviderConnection> jpaConnections){
        List<Connection<?>> connections = new ArrayList<Connection<?>>();
        if(jpaConnections.isEmpty()){
            return connections;
        }
        ListIterator<OAuth2UserProviderConnection> iterator = jpaConnections.listIterator();
        Set<String> registeredProviderIds = factoryLocator.registeredProviderIds();
        ConnectionData data = null;
        while (iterator.hasNext()){
            data = convert(iterator.next());
            if(registeredProviderIds.contains(data.getProviderId())){
                ConnectionFactory factory = factoryLocator.getConnectionFactory(data.getProviderId());
                connections.add(factory.createConnection(data));
            }
        }
        return connections;
    }
}
