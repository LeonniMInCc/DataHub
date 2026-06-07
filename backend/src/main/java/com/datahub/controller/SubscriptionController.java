package com.datahub.controller;

import com.datahub.dto.ApiResponse;
import com.datahub.dto.SubscribeRequest;
import com.datahub.entity.Subscription;
import com.datahub.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ApiResponse<Subscription> subscribe(@Valid @RequestBody SubscribeRequest request,
                                                Authentication auth) {
        Long subscriberId = (Long) auth.getPrincipal();
        return ApiResponse.success("订阅成功", subscriptionService.subscribe(subscriberId, request.getAssetId()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> unsubscribe(@PathVariable Long id, Authentication auth) {
        Long subscriberId = (Long) auth.getPrincipal();
        subscriptionService.unsubscribe(subscriberId, id);
        return ApiResponse.success("已取消订阅", null);
    }

    @GetMapping("/my")
    public ApiResponse<List<Subscription>> mySubscriptions(Authentication auth) {
        Long subscriberId = (Long) auth.getPrincipal();
        return ApiResponse.success(subscriptionService.getMySubscriptions(subscriberId));
    }
}
