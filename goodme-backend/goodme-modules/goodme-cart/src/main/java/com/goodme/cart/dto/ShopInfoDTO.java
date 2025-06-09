package com.goodme.cart.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 店铺信息DTO
 */
@Data
public class ShopInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long id;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 店铺Logo
     */
    private String logo;
} 