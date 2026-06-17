package com.datahub.controller;

import com.datahub.dto.ApiResponse;
import com.datahub.entity.Developer;
import com.datahub.repository.AssetRepository;
import com.datahub.repository.DeveloperRepository;
import com.datahub.service.SubscriptionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final DeveloperRepository devRepo;
    private final AssetRepository assetRepo;
    private final SubscriptionService subscriptionService;

    public UserController(DeveloperRepository devRepo, AssetRepository assetRepo,
                          SubscriptionService subscriptionService) {
        this.devRepo = devRepo;
        this.assetRepo = assetRepo;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/profile")
    public ApiResponse<Map<String, Object>> profile(Authentication auth) {
        Long devId = (Long) auth.getPrincipal();
        Developer dev = devRepo.findById(devId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        long publishedAssets = 0;
        long subscribedAssets = 0;
        if (dev.getRole() == Developer.Role.PROVIDER) {
            publishedAssets = assetRepo.countByProviderId(devId);
        }
        subscribedAssets = subscriptionService.getSubscriptionCount(devId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("devId", dev.getDevId());
        result.put("username", dev.getUsername());
        result.put("email", dev.getEmail());
        result.put("role", dev.getRole().name());
        result.put("avatar", dev.getAvatar());
        result.put("apiKey", dev.getApiKey());
        result.put("createdAt", dev.getCreatedAt());
        result.put("publishedAssets", publishedAssets);
        result.put("subscribedAssets", subscribedAssets);

        return ApiResponse.success(result);
    }

    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> stats(Authentication auth) {
        Long devId = (Long) auth.getPrincipal();
        Developer dev = devRepo.findById(devId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Map<String, Object> stats = new LinkedHashMap<>();
        if (dev.getRole() == Developer.Role.PROVIDER) {
            stats.put("totalSubscribers", 0);
        } else {
            stats.put("totalCalls", 0);
        }
        return ApiResponse.success(stats);
    }
}
