package com.datahub.repository;

import com.datahub.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Page<Asset> findByStatus(Asset.Status status, Pageable pageable);

    Page<Asset> findByProviderIdAndStatus(Long providerId, Asset.Status status, Pageable pageable);

    @Query("SELECT a FROM Asset a WHERE a.status = 'ACTIVE' " +
           "AND (:keyword IS NULL OR :keyword = '' OR a.title LIKE %:keyword% OR a.description LIKE %:keyword% OR a.tags LIKE %:keyword%) " +
           "AND (:type IS NULL OR a.assetType = :type)")
    Page<Asset> search(@Param("keyword") String keyword,
                       @Param("type") Asset.AssetType type,
                       Pageable pageable);

    List<Asset> findByProviderId(Long providerId);

    long countByProviderId(Long providerId);

    long countByStatus(Asset.Status status);

    @Query("SELECT a.assetType, COUNT(a) FROM Asset a WHERE a.status = 'ACTIVE' GROUP BY a.assetType")
    List<Object[]> countByType();
}
