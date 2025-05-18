package com.epsi.TestProductService.dto;

import java.io.Serializable;

public class ProductOrderDto implements Serializable {
    private String productId;
    private int stock;

    public ProductOrderDto() {}

    public ProductOrderDto(String productId, int stock) {
        this.productId = productId;
        this.stock = stock;
    }

    public String getProductId() {
        return productId;
    }

    public int getStock() {
        return stock;
    }
}

