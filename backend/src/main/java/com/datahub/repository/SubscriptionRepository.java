package com.datahub.repository;

import com.datahub.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findBySubscriberId(Long subscriberId);

    List<Subscription> findByAssetId(Long assetId);

    Optional<Subscription> findBySubscriberIdAndAssetId(Long subscriberId, Long assetId);

    long countBySubscriberId(Long subscriberId);

    long countByAssetId(Long assetId);

    long countBySubscriberIdAndStatus(Long subscriberId, Subscription.SubStatus status);

    long countByAssetIdAndStatus(Long assetId, Subscription.SubStatus status);

    boolean existsBySubscriberIdAndAssetId(Long subscriberId, Long assetId);

    boolean existsBySubscriberIdAndAssetIdAndStatus(Long subscriberId, Long assetId, Subscription.SubStatus status);

    @Query("SELECT s FROM Subscription s JOIN FETCH s.asset WHERE s.subscriberId = :subscriberId")
    List<Subscription> findBySubscriberIdWithAsset(@Param("subscriberId") Long subscriberId);

    @Query("SELECT s.assetId, COUNT(s) FROM Subscription s WHERE s.status = 'ACTIVE' GROUP BY s.assetId ORDER BY COUNT(s) DESC")
    List<Object[]> findTopSubscribedAssets();
}
