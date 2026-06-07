package com.datahub.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class AssetRequest {
    @NotBlank(message = "标题不能为空")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "资产类型不能为空")
    private String assetType;

    private String description;

    private String endpointUrl;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", message = "价格不能为负")
    private BigDecimal pricePerMonth;

    private String tags;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAssetType() { return assetType; }
    public void setAssetType(String assetType) { this.assetType = assetType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
    public BigDecimal getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}
