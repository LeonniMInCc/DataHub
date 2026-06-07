package com.datahub.service;

import com.datahub.dto.AssetRequest;
import com.datahub.entity.DigitalAsset;
import org.springframework.data.domain.Page;

public interface AssetService {
    Page<DigitalAsset> listAssets(int page, int size, String keyword, String assetType);
    DigitalAsset getAsset(Long id);
    DigitalAsset createAsset(Long providerId, AssetRequest request);
    DigitalAsset updateAsset(Long assetId, Long providerId, AssetRequest request);
    void deleteAsset(Long assetId, Long providerId);
    Page<DigitalAsset> getMyAssets(Long providerId, int page, int size);
}
