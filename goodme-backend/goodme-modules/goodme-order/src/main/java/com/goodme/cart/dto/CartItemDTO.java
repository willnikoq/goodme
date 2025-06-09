package com.goodme.cart.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {
    
    /**
     * 购物车项ID
     */
    private Long id;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品图片
     */
    private String productImage;
    
    /**
     * 商品规格ID
     */
    private Long skuId;
    
    /**
     * 商品规格名称
     */
    private String skuName;
    
    /**
     * 商品数量
     */
    private Integer quantity;
    
    /**
     * 商品单价
     */
    private BigDecimal price;
    
    /**
     * 商品总价
     */
    private BigDecimal totalPrice;
    
    /**
     * 是否选中
     */
    private Boolean selected;
} 