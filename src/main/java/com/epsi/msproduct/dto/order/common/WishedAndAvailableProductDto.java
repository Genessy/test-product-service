package com.epsi.msproduct.dto.order.common;

import java.util.List;

public class WishedAndAvailableProductDto {
    private List<WishedProductDto> wishedProduct;
    private List<AvailableProductDto> availableProduct;

    public List<WishedProductDto> getWishedProduct() {
        return wishedProduct;
    }

    public void setWishedProduct(List<WishedProductDto> wishedProduct) {
        this.wishedProduct = wishedProduct;
    }

    public List<AvailableProductDto> getAvailableProduct() {
        return availableProduct;
    }

    public void setAvailableProduct(List<AvailableProductDto> availableProduct) {
        this.availableProduct = availableProduct;
    }
}
