package com.goodme.cart.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车列表DTO
 */
@Data
public class CartListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺信息
     */
    private ShopInfoDTO shopInfo;

    /**
     * 购物车商品列表
     */
    private List<CartItemDTO> items;

    /**
     * 商品总数
     */
    private Integer totalCount;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;
} 