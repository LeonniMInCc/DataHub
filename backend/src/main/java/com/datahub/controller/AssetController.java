package com.datahub.controller;

import com.datahub.dto.ApiResponse;
import com.datahub.dto.AssetRequest;
import com.datahub.dto.AssetResponse;
import com.datahub.entity.Asset;
import com.datahub.service.AssetService;
import com.datahub.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;
    private final SubscriptionService subscriptionService;

    public AssetController(AssetService assetService, SubscriptionService subscriptionService) {
        this.assetService = assetService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ApiResponse<Page<AssetResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String assetType) {
        return ApiResponse.success(assetService.listAssets(page, size, keyword, assetType).map(AssetResponse::from));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Long id) {
        Asset asset = assetService.getAsset(id);
        long subCount = subscriptionService.getAssetSubscriberCount(id);
        Map<String, Object> result = new HashMap<>();
        result.put("asset", AssetResponse.from(asset));
        result.put("subscriberCount", subCount);
        return ApiResponse.success(result);
    }

    @PostMapping
    public ApiResponse<AssetResponse> create(@Valid @RequestBody AssetRequest request,
                                             Authentication auth) {
        Long providerId = (Long) auth.getPrincipal();
        return ApiResponse.success("发布成功", AssetResponse.from(assetService.createAsset(providerId, request)));
    }

    @PutMapping("/{id}")
    public ApiResponse<AssetResponse> update(@PathVariable Long id,
                                             @Valid @RequestBody AssetRequest request,
                                             Authentication auth) {
        Long providerId = (Long) auth.getPrincipal();
        return ApiResponse.success("更新成功", AssetResponse.from(assetService.updateAsset(id, providerId, request)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, Authentication auth) {
        Long providerId = (Long) auth.getPrincipal();
        assetService.deleteAsset(id, providerId);
        return ApiResponse.success("删除成功", null);
    }

    @GetMapping("/my")
    public ApiResponse<Page<AssetResponse>> myAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Authentication auth) {
        Long providerId = (Long) auth.getPrincipal();
        return ApiResponse.success(assetService.getMyAssets(providerId, page, size).map(AssetResponse::from));
    }
}
