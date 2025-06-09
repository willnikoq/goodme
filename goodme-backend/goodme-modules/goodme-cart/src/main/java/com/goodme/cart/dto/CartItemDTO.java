package com.goodme.cart.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车项DTO
 */
@Data
public class CartItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车项ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺Logo
     */
    private String shopLogo;

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
     * 规格名称
     */
    private String skuName;

    /**
     * 规格列表
     */
    private List<CartItemSpecDTO> specs;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 是否选中：0否，1是
     */
    private Boolean selected;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 