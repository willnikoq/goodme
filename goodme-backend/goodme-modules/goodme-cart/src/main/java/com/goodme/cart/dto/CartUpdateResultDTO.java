package com.goodme.cart.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 更新购物车结果DTO
 */
@Data
public class CartUpdateResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 更新后的总价
     */
    private BigDecimal totalPrice;

    /**
     * 无参构造方法
     */
    public CartUpdateResultDTO() {
    }

    /**
     * 构造方法
     * 
     * @param totalPrice 更新后的总价
     */
    public CartUpdateResultDTO(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
} 