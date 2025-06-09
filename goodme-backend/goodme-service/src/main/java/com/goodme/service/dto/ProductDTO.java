package com.goodme.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品DTO
 */
@Data
@Schema(description = "商品信息")
public class ProductDTO {
    
    @Schema(description = "商品ID")
    private Long id;
    
    @Schema(description = "商品名称")
    private String name;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "分类名称")
    private String categoryName;
    
    @Schema(description = "缩略图")
    private String thumbnail;
    
    @Schema(description = "商品图片列表")
    private List<String> imageList;
    
    @Schema(description = "商品描述")
    private String description;
    
    @Schema(description = "商品价格")
    private BigDecimal price;
    
    @Schema(description = "库存")
    private Integer stock;
    
    @Schema(description = "销量")
    private Integer sales;
    
    @Schema(description = "单位")
    private String unit;
    
    @Schema(description = "状态：0下架，1上架")
    private Integer status;
    
    @Schema(description = "是否新品：0否，1是")
    private Integer isNew;
    
    @Schema(description = "是否热销：0否，1是")
    private Integer isHot;
    
    @Schema(description = "商品规格列表")
    private List<ProductSpecDTO> specs;
} 