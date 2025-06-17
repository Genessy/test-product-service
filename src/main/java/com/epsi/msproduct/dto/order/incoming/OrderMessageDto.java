package com.epsi.msproduct.dto.order.incoming;

import com.epsi.msproduct.dto.order.common.WishedProductDto;

import java.util.List;

public class OrderMessageDto {
    private String orderId;
    private List<WishedProductDto> products;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<WishedProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<WishedProductDto> products) {
        this.products = products;
    }
}