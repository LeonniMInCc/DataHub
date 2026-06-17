package com.datahub.service;

import com.datahub.dto.AssetRequest;
import com.datahub.entity.Asset;
import org.springframework.data.domain.Page;

public interface AssetService {
    Page<Asset> listAssets(int page, int size, String keyword, String assetType);
    Asset getAsset(Long id);
    Asset createAsset(Long providerId, AssetRequest request);
    Asset updateAsset(Long assetId, Long providerId, AssetRequest request);
    void deleteAsset(Long assetId, Long providerId);
    Page<Asset> getMyAssets(Long providerId, int page, int size);
}
