package com.goodme.order.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private String productImage;
    private String skuName;
    private String specJson;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
} 