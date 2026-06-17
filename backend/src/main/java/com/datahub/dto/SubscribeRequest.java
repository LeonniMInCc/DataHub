package com.datahub.dto;

import jakarta.validation.constraints.NotNull;

public class SubscribeRequest {
    @NotNull(message = "资产ID不能为空")
    private Long assetId;

    private Boolean mockPaymentSuccess;

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public Boolean getMockPaymentSuccess() { return mockPaymentSuccess; }
    public void setMockPaymentSuccess(Boolean mockPaymentSuccess) { this.mockPaymentSuccess = mockPaymentSuccess; }
}
