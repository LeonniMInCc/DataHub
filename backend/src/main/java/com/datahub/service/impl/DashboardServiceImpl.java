package com.datahub.service.impl;

import com.datahub.dto.DashboardDTO;
import com.datahub.entity.Asset;
import com.datahub.repository.*;
import com.datahub.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DeveloperRepository devRepo;
    private final AssetRepository assetRepo;
    private final SubscriptionRepository subRepo;
    private final UsageLogRepository logRepo;

    public DashboardServiceImpl(DeveloperRepository devRepo, AssetRepository assetRepo,
                                SubscriptionRepository subRepo, UsageLogRepository logRepo) {
        this.devRepo = devRepo;
        this.assetRepo = assetRepo;
        this.subRepo = subRepo;
        this.logRepo = logRepo;
    }

    @Override
    public DashboardDTO getDashboardData() {
        long totalUsers = devRepo.count();
        long totalAssets = assetRepo.countByStatus(Asset.Status.ACTIVE);
        long totalSubscriptions = subRepo.count();
        long totalApiCalls = logRepo.count();

        List<Map<String, Object>> assetTypeDistribution = new ArrayList<>();
        List<Object[]> typeCounts = assetRepo.countByType();
        for (Object[] row : typeCounts) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", row[0].toString());
            item.put("value", row[1]);
            assetTypeDistribution.add(item);
        }

        List<Map<String, Object>> last7DaysCalls = new ArrayList<>();
        List<Object[]> dayCounts = logRepo.countByDaySince(LocalDateTime.now().minusDays(7));
        for (Object[] row : dayCounts) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("date", row[0].toString());
            item.put("count", row[1]);
            last7DaysCalls.add(item);
        }

        List<Map<String, Object>> top5Assets = new ArrayList<>();
        List<Object[]> topAssets = subRepo.findTopSubscribedAssets();
        int count = 0;
        for (Object[] row : topAssets) {
            if (count++ >= 5) break;
            Long assetId = (Long) row[0];
            Long subCount = (Long) row[1];
            String title = assetRepo.findById(assetId)
                    .map(Asset::getTitle)
                    .orElse("未知资产");

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("assetId", assetId);
            item.put("title", title);
            item.put("subscriberCount", subCount);
            top5Assets.add(item);
        }

        DashboardDTO dto = new DashboardDTO();
        dto.setTotalUsers(totalUsers);
        dto.setTotalAssets(totalAssets);
        dto.setTotalSubscriptions(totalSubscriptions);
        dto.setTotalApiCalls(totalApiCalls);
        dto.setAssetTypeDistribution(assetTypeDistribution);
        dto.setLast7DaysCalls(last7DaysCalls);
        dto.setTop5Assets(top5Assets);
        return dto;
    }
}
