package com.datahub.service.impl;

import com.datahub.dto.AssetRequest;
import com.datahub.entity.Asset;
import com.datahub.entity.User;
import com.datahub.repository.AssetRepository;
import com.datahub.repository.UserRepository;
import com.datahub.service.AssetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepo;
    private final UserRepository userRepo;

    public AssetServiceImpl(AssetRepository assetRepo, UserRepository userRepo) {
        this.assetRepo = assetRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Page<Asset> listAssets(int page, int size, String keyword, String assetType) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Asset.AssetType type = null;
        if (assetType != null && !assetType.isEmpty()) {
            try {
                type = Asset.AssetType.valueOf(assetType.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }
        if ((keyword == null || keyword.isEmpty()) && type == null) {
            return assetRepo.findByStatus(Asset.Status.ACTIVE, pr);
        }
        return assetRepo.search(keyword, type, pr);
    }

    @Override
    public Asset getAsset(Long id) {
        return assetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("资产不存在"));
    }

    @Override
    public Asset createAsset(Long providerId, AssetRequest request) {
        User provider = userRepo.findById(providerId)
                .orElseThrow(() -> new RuntimeException("当前用户不存在，无法发布资产"));
        if (provider.getRole() != User.Role.PROVIDER) {
            throw new RuntimeException("只有 Provider 可以发布资产");
        }

        Asset asset = new Asset();
        asset.setProvider(provider);
        asset.setTitle(request.getTitle());
        asset.setAssetType(Asset.AssetType.valueOf(request.getAssetType().toUpperCase()));
        asset.setDescription(request.getDescription());
        asset.setEndpointUrl(request.getEndpointUrl());
        asset.setPricePerMonth(request.getPricePerMonth());
        asset.setTags(request.getTags());
        asset.setStatus(Asset.Status.ACTIVE);
        return assetRepo.save(asset);
    }

    @Override
    public Asset updateAsset(Long assetId, Long providerId, AssetRequest request) {
        Asset asset = getAsset(assetId);
        if (!asset.getProviderId().equals(providerId)) {
            throw new RuntimeException("无权操作此资产");
        }
        asset.setTitle(request.getTitle());
        asset.setAssetType(Asset.AssetType.valueOf(request.getAssetType().toUpperCase()));
        asset.setDescription(request.getDescription());
        asset.setEndpointUrl(request.getEndpointUrl());
        asset.setPricePerMonth(request.getPricePerMonth());
        asset.setTags(request.getTags());
        return assetRepo.save(asset);
    }

    @Override
    public void deleteAsset(Long assetId, Long providerId) {
        Asset asset = getAsset(assetId);
        if (!asset.getProviderId().equals(providerId)) {
            throw new RuntimeException("无权操作此资产");
        }
        asset.setStatus(Asset.Status.DELETED);
        assetRepo.save(asset);
    }

    @Override
    public Page<Asset> getMyAssets(Long providerId, int page, int size) {
        return assetRepo.findByProviderIdAndStatus(providerId, Asset.Status.ACTIVE,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }
}
