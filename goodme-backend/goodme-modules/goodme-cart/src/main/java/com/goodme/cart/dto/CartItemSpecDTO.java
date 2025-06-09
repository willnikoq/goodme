package com.goodme.cart.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车项规格DTO
 */
@Data
public class CartItemSpecDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规格ID
     */
    private Long specId;

    /**
     * 规格名称
     */
    private String specName;

    /**
     * 规格值ID
     */
    private Long valueId;

    /**
     * 规格值名称
     */
    private String valueName;

    /**
     * 额外价格
     */
    private BigDecimal extraPrice;
} 