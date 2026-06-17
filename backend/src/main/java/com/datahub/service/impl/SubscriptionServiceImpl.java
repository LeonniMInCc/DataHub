package com.datahub.service.impl;

import com.datahub.entity.Asset;
import com.datahub.entity.Subscription;
import com.datahub.entity.User;
import com.datahub.exception.UserNotFoundException;
import com.datahub.repository.AssetRepository;
import com.datahub.repository.SubscriptionRepository;
import com.datahub.repository.UserRepository;
import com.datahub.service.SubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subRepo;
    private final AssetRepository assetRepo;
    private final UserRepository userRepo;

    public SubscriptionServiceImpl(SubscriptionRepository subRepo, AssetRepository assetRepo, UserRepository userRepo) {
        this.subRepo = subRepo;
        this.assetRepo = assetRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public Subscription subscribe(Long subscriberId, Long assetId, Boolean mockPaymentSuccess) {
        User subscriber = getValidSubscriber(subscriberId);

        Asset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new RuntimeException("资产不存在"));
        if (asset.getStatus() != Asset.Status.ACTIVE) {
            throw new RuntimeException("资产不可订阅");
        }

        if (asset.getProviderId().equals(subscriber.getUserId())) {
            throw new RuntimeException("不能订阅自己的资产");
        }

        Subscription existing = subRepo.findBySubscriberIdAndAssetId(subscriber.getUserId(), assetId).orElse(null);
        boolean paymentConfirmed = mockPaymentSuccess != null
                ? mockPaymentSuccess
                : ThreadLocalRandom.current().nextInt(100) < 85;

        if (existing != null) {
            if (existing.getStatus() == Subscription.SubStatus.ACTIVE) {
                throw new RuntimeException("已订阅该资产，请勿重复订阅");
            }
            existing.setQuotaLimit(1000);
            existing.setQuotaUsed(0);
            existing.setStartDate(LocalDate.now());
            existing.setEndDate(LocalDate.now().plusMonths(1));
            existing.setStatus(paymentConfirmed ? Subscription.SubStatus.ACTIVE : Subscription.SubStatus.FAILED);
            return subRepo.save(existing);
        }

        Subscription sub = new Subscription();
        sub.setSubscriberId(subscriber.getUserId());
        sub.setAssetId(assetId);
        sub.setQuotaLimit(1000);
        sub.setQuotaUsed(0);
        sub.setStartDate(LocalDate.now());
        sub.setEndDate(LocalDate.now().plusMonths(1));
        sub.setStatus(paymentConfirmed ? Subscription.SubStatus.ACTIVE : Subscription.SubStatus.FAILED);

        return subRepo.save(sub);
    }

    @Override
    @Transactional
    public void unsubscribe(Long subscriberId, Long subId) {
        User subscriber = getValidSubscriber(subscriberId);
        Subscription sub = subRepo.findById(subId)
                .orElseThrow(() -> new RuntimeException("订阅记录不存在"));

        if (!sub.getSubscriberId().equals(subscriber.getUserId())) {
            throw new RuntimeException("无权操作此订阅");
        }

        sub.setStatus(Subscription.SubStatus.CANCELLED);
        subRepo.save(sub);
    }

    @Override
    public List<Subscription> getMySubscriptions(Long subscriberId) {
        User subscriber = getValidSubscriber(subscriberId);
        return subRepo.findBySubscriberIdWithAsset(subscriber.getUserId());
    }

    @Override
    public long getSubscriptionCount(Long subscriberId) {
        User subscriber = getValidSubscriber(subscriberId);
        return subRepo.countBySubscriberIdAndStatus(subscriber.getUserId(), Subscription.SubStatus.ACTIVE);
    }

    @Override
    public long getAssetSubscriberCount(Long assetId) {
        return subRepo.countByAssetIdAndStatus(assetId, Subscription.SubStatus.ACTIVE);
    }

    private User getValidSubscriber(Long subscriberId) {
        return userRepo.findById(subscriberId)
                .orElseThrow(() -> new UserNotFoundException("当前登录用户不存在或已失效，请重新登录"));
    }
}
