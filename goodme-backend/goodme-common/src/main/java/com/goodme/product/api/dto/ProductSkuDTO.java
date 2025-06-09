package com.goodme.product.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品规格DTO
 */
@Data
public class ProductSkuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规格ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 规格名称
     */
    private String name;

    /**
     * 规格编码
     */
    private String code;

    /**
     * 规格图片
     */
    private String image;

    /**
     * 规格价格
     */
    private BigDecimal price;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 规格值JSON
     */
    private String specJson;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 