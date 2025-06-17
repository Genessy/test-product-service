package com.epsi.msproduct.dto.order.outgoing;

import java.util.List;
import com.epsi.msproduct.dto.order.common.*;

public class ProductOrderResponseDto {
    private String orderId;
    private String status;
    private List<AvailableProductDto> ordered;
    private List<WishedAndAvailableProductDto> partiallyOrdered;
    private List<WishedAndAvailableProductDto> notAvailable;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AvailableProductDto> getOrdered() {
        return ordered;
    }

    public void setOrdered(List<AvailableProductDto> ordered) {
        this.ordered = ordered;
    }

    public List<WishedAndAvailableProductDto> getPartiallyOrdered() {
        return partiallyOrdered;
    }

    public void setPartiallyOrdered(List<WishedAndAvailableProductDto> partiallyOrdered) {
        this.partiallyOrdered = partiallyOrdered;
    }

    public List<WishedAndAvailableProductDto> getNotAvailable() {
        return notAvailable;
    }

    public void setNotAvailable(List<WishedAndAvailableProductDto> notAvailable) {
        this.notAvailable = notAvailable;
    }
}
