package com.goodme.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品查询请求
 */
@Data
@Schema(description = "商品查询请求")
public class ProductQueryRequest {
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "关键词")
    private String keyword;
    
    @Schema(description = "是否新品：0否，1是")
    private Integer isNew;
    
    @Schema(description = "是否热销：0否，1是")
    private Integer isHot;
    
    @Schema(description = "排序字段：price-价格，sales-销量，time-上架时间")
    private String sortField;
    
    @Schema(description = "排序方式：asc-升序，desc-降序")
    private String sortOrder;
    
    @Schema(description = "页码，从1开始")
    private Integer page = 1;
    
    @Schema(description = "每页条数")
    private Integer size = 10;
} 