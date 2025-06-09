package com.goodme.product.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品DTO
 */
@Data
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 主图
     */
    private String mainImage;

    /**
     * 商品图片，多个图片用逗号分隔
     */
    private String images;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 单位
     */
    private String unit;

    /**
     * 重量(克)
     */
    private BigDecimal weight;

    /**
     * 状态：0下架，1上架
     */
    private Integer status;

    /**
     * 是否新品：0否，1是
     */
    private Integer isNew;

    /**
     * 是否热销：0否，1是
     */
    private Integer isHot;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 