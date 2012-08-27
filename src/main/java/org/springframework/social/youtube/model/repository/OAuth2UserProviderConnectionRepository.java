package org.springframework.social.youtube.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.social.youtube.model.entity.OAuth2UserProviderConnection;

import java.util.List;
import java.util.Set;

/**
 * User: lancea10
 * Date: 8/22/12
 * Time: 4:10 PM
 */
public interface OAuth2UserProviderConnectionRepository extends JpaRepository<OAuth2UserProviderConnection, Long> {

    public List<OAuth2UserProviderConnection> findAllByProviderId(String providerId);

    public List<OAuth2UserProviderConnection> findAllByUserId(String userId);

    @Query(value = "SELECT c FROM OAuth2UserProviderConnection c where c.userId = :userId AND c.providerId IN :providerIds")
    public List<OAuth2UserProviderConnection> findAllByProviderIdsAndUserId(@Param("userId")String userId, @Param("providerIds")List<String> providerIds);

    @Query(value = "SELECT c FROM OAuth2UserProviderConnection c where c.userId = :userId AND c.providerId = :providerId")
    public List<OAuth2UserProviderConnection> findAllByUserIdAndProviderId(@Param("userId")String userId, @Param("providerId") String providerId);

    @Query(value = "SELECT c.userId FROM OAuth2UserProviderConnection c where c.providerId = :providerId AND c.providerUserId IN :providerUserIds")
    public List<String> findAllByProviderIdAndProviderUserIds(@Param("providerId") String providerId, @Param("providerUserIds") List<String> providerUserIds);

    @Query(value = "SELECT c FROM OAuth2UserProviderConnection c where c.userId = :userId AND c.providerId = :providerId AND c.providerUserId = :providerUserId")
    public OAuth2UserProviderConnection findOneByUserIdAndProviderIdAndProviderUserId(@Param("userId") String userId, @Param("providerId") String providerId, @Param("providerUserId") String providerUserId);

    @Query(value = "SELECT c FROM OAuth2UserProviderConnection c where c.userId = :userId AND c.providerId = :providerId AND c.rank = :rank")
    public OAuth2UserProviderConnection findOneByUserIdAndProviderIdAndRank(@Param("userId") String userId, @Param("providerId") String providerId, @Param("rank") Integer rank);

    @Query(value="SELECT c FROM OAuth2UserProviderConnection c WHERE c.providerId = :providerId AND c.providerUserId = :providerUserId")
    public List<String> getUserIdsByProviderIdAndProviderUserId(@Param("providerId")String providerId, @Param("providerUserId")String providerUserId);

    @Modifying
    @Query(value="DELETE FROM OAuth2UserProviderConnection c where c.userId = :userId AND c.providerId = :providerId")
    public void deleteInBatchByUserIdAndProviderId(@Param("userId")String userId, @Param("providerId")String providerId);

    @Modifying
    @Query(value = "DELETE FROM OAuth2UserProviderConnection c where c.userId = :userId AND c.providerId = :providerId AND c.providerUserId = :providerUserId")
    public void deleteOneByUserIdAndProviderIdAndProviderUserId(@Param("userId") String userId, @Param("providerId") String providerId, @Param("providerUserId") String providerUserId);
}
