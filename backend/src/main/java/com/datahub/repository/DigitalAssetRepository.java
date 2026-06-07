package com.datahub.repository;

import com.datahub.entity.DigitalAsset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DigitalAssetRepository extends JpaRepository<DigitalAsset, Long> {

    Page<DigitalAsset> findByStatus(DigitalAsset.Status status, Pageable pageable);

    @Query("SELECT a FROM DigitalAsset a WHERE a.status = 'ACTIVE' " +
           "AND (:keyword IS NULL OR a.title LIKE %:keyword% OR a.description LIKE %:keyword% OR a.tags LIKE %:keyword%) " +
           "AND (:type IS NULL OR a.assetType = :type)")
    Page<DigitalAsset> search(@Param("keyword") String keyword,
                              @Param("type") DigitalAsset.AssetType type,
                              Pageable pageable);

    List<DigitalAsset> findByProviderId(Long providerId);

    long countByProviderId(Long providerId);

    long countByStatus(DigitalAsset.Status status);

    @Query("SELECT a.assetType, COUNT(a) FROM DigitalAsset a WHERE a.status = 'ACTIVE' GROUP BY a.assetType")
    List<Object[]> countByType();
}
