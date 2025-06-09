package com.goodme.cart.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加购物车结果DTO
 */
@Data
public class CartAddResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车商品总数
     */
    private Integer cartCount;

    /**
     * 无参构造方法
     */
    public CartAddResultDTO() {
    }

    /**
     * 构造方法
     * 
     * @param cartCount 购物车商品总数
     */
    public CartAddResultDTO(Integer cartCount) {
        this.cartCount = cartCount;
    }
} 