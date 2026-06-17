package com.datahub.controller;

import com.datahub.dto.ApiResponse;
import com.datahub.dto.SubscriptionResponse;
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
    public ApiResponse<SubscriptionResponse> subscribe(@Valid @RequestBody SubscribeRequest request,
                                                       Authentication auth) {
        Long subscriberId = (Long) auth.getPrincipal();
        Subscription subscription = subscriptionService.subscribe(
                subscriberId,
                request.getAssetId(),
                request.getMockPaymentSuccess()
        );
        SubscriptionResponse response = SubscriptionResponse.from(subscription);
        if (subscription.getStatus() == Subscription.SubStatus.ACTIVE) {
            return ApiResponse.success("订阅成功！已开通该数据资产的使用权限。", response);
        }
        return new ApiResponse<>(402, "付款确认失败，请重新尝试支付！", response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> unsubscribe(@PathVariable Long id, Authentication auth) {
        Long subscriberId = (Long) auth.getPrincipal();
        subscriptionService.unsubscribe(subscriberId, id);
        return ApiResponse.success("已取消订阅", null);
    }

    @GetMapping("/my")
    public ApiResponse<List<SubscriptionResponse>> mySubscriptions(Authentication auth) {
        Long subscriberId = (Long) auth.getPrincipal();
        return ApiResponse.success(subscriptionService.getMySubscriptions(subscriberId)
                .stream()
                .map(SubscriptionResponse::from)
                .toList());
    }
}
