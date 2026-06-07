package com.datahub.dto;

import jakarta.validation.constraints.NotNull;

public class SubscribeRequest {
    @NotNull(message = "资产ID不能为空")
    private Long assetId;

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
}
