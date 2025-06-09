package com.goodme.order.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderCreateRequest {
    private Long shopId;
    private List<OrderItemRequest> items;
    private String remark;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Long skuId;
        private Integer quantity;
        private String specJson;
    }
} 