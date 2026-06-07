package com.datahub.service;

import com.datahub.entity.Subscription;

import java.util.List;

public interface SubscriptionService {
    Subscription subscribe(Long subscriberId, Long assetId);
    void unsubscribe(Long subscriberId, Long subId);
    List<Subscription> getMySubscriptions(Long subscriberId);
    long getSubscriptionCount(Long subscriberId);
    long getAssetSubscriberCount(Long assetId);
}
