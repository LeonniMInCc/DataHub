package com.datahub.service.impl;

import com.datahub.dto.AssetRequest;
import com.datahub.entity.DigitalAsset;
import com.datahub.repository.DigitalAssetRepository;
import com.datahub.service.AssetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AssetServiceImpl implements AssetService {

    private final DigitalAssetRepository assetRepo;

    public AssetServiceImpl(DigitalAssetRepository assetRepo) {
        this.assetRepo = assetRepo;
    }

    @Override
    public Page<DigitalAsset> listAssets(int page, int size, String keyword, String assetType) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        DigitalAsset.AssetType type = null;
        if (assetType != null && !assetType.isEmpty()) {
            try {
                type = DigitalAsset.AssetType.valueOf(assetType.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }
        if ((keyword == null || keyword.isEmpty()) && type == null) {
            return assetRepo.findByStatus(DigitalAsset.Status.ACTIVE, pr);
        }
        return assetRepo.search(keyword, type, pr);
    }

    @Override
    public DigitalAsset getAsset(Long id) {
        return assetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("资产不存在"));
    }

    @Override
    public DigitalAsset createAsset(Long providerId, AssetRequest request) {
        DigitalAsset asset = new DigitalAsset();
        asset.setProviderId(providerId);
        asset.setTitle(request.getTitle());
        asset.setAssetType(DigitalAsset.AssetType.valueOf(request.getAssetType().toUpperCase()));
        asset.setDescription(request.getDescription());
        asset.setEndpointUrl(request.getEndpointUrl());
        asset.setPricePerMonth(request.getPricePerMonth());
        asset.setTags(request.getTags());
        asset.setStatus(DigitalAsset.Status.ACTIVE);
        return assetRepo.save(asset);
    }

    @Override
    public DigitalAsset updateAsset(Long assetId, Long providerId, AssetRequest request) {
        DigitalAsset asset = getAsset(assetId);
        if (!asset.getProviderId().equals(providerId)) {
            throw new RuntimeException("无权操作此资产");
        }
        asset.setTitle(request.getTitle());
        asset.setAssetType(DigitalAsset.AssetType.valueOf(request.getAssetType().toUpperCase()));
        asset.setDescription(request.getDescription());
        asset.setEndpointUrl(request.getEndpointUrl());
        asset.setPricePerMonth(request.getPricePerMonth());
        asset.setTags(request.getTags());
        return assetRepo.save(asset);
    }

    @Override
    public void deleteAsset(Long assetId, Long providerId) {
        DigitalAsset asset = getAsset(assetId);
        if (!asset.getProviderId().equals(providerId)) {
            throw new RuntimeException("无权操作此资产");
        }
        asset.setStatus(DigitalAsset.Status.DELETED);
        assetRepo.save(asset);
    }

    @Override
    public Page<DigitalAsset> getMyAssets(Long providerId, int page, int size) {
        return assetRepo.findByStatus(DigitalAsset.Status.ACTIVE,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }
}
