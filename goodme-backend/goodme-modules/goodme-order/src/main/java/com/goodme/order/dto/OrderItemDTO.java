package com.goodme.order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private String productImage;
    private String skuName;
    private String specJson;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
} 