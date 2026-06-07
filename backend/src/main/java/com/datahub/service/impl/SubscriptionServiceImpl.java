package com.datahub.service.impl;

import com.datahub.entity.DigitalAsset;
import com.datahub.entity.Subscription;
import com.datahub.repository.DigitalAssetRepository;
import com.datahub.repository.SubscriptionRepository;
import com.datahub.service.SubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subRepo;
    private final DigitalAssetRepository assetRepo;

    public SubscriptionServiceImpl(SubscriptionRepository subRepo, DigitalAssetRepository assetRepo) {
        this.subRepo = subRepo;
        this.assetRepo = assetRepo;
    }

    @Override
    @Transactional
    public Subscription subscribe(Long subscriberId, Long assetId) {
        DigitalAsset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new RuntimeException("资产不存在"));

        if (asset.getProviderId().equals(subscriberId)) {
            throw new RuntimeException("不能订阅自己的资产");
        }

        if (subRepo.existsBySubscriberIdAndAssetIdAndStatus(subscriberId, assetId, Subscription.SubStatus.ACTIVE)) {
            throw new RuntimeException("已订阅该资产，请勿重复订阅");
        }

        Subscription sub = new Subscription();
        sub.setSubscriberId(subscriberId);
        sub.setAssetId(assetId);
        sub.setQuotaLimit(1000);
        sub.setQuotaUsed(0);
        sub.setStartDate(LocalDate.now());
        sub.setEndDate(LocalDate.now().plusMonths(1));
        sub.setStatus(Subscription.SubStatus.ACTIVE);

        return subRepo.save(sub);
    }

    @Override
    @Transactional
    public void unsubscribe(Long subscriberId, Long subId) {
        Subscription sub = subRepo.findById(subId)
                .orElseThrow(() -> new RuntimeException("订阅记录不存在"));

        if (!sub.getSubscriberId().equals(subscriberId)) {
            throw new RuntimeException("无权操作此订阅");
        }

        sub.setStatus(Subscription.SubStatus.CANCELLED);
        subRepo.save(sub);
    }

    @Override
    public List<Subscription> getMySubscriptions(Long subscriberId) {
        return subRepo.findBySubscriberIdWithAsset(subscriberId);
    }

    @Override
    public long getSubscriptionCount(Long subscriberId) {
        return subRepo.countBySubscriberId(subscriberId);
    }

    @Override
    public long getAssetSubscriberCount(Long assetId) {
        return subRepo.countByAssetId(assetId);
    }
}
