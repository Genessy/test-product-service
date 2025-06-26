package com.epsi.msproduct.dto.order.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WishedProductDto {
    @JsonProperty("IdProduct")
    private String productId;
    @JsonProperty("Name")
    private String productName;
    @JsonProperty("Quantity")
    private String quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
