package com.goodme.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 商品规格DTO
 */
@Data
@Schema(description = "商品规格")
public class ProductSpecDTO {
    
    @Schema(description = "规格ID")
    private Long id;
    
    @Schema(description = "规格名称")
    private String name;
    
    @Schema(description = "排序值")
    private Integer sort;
    
    @Schema(description = "规格值列表")
    private List<ProductSpecValueDTO> values;
} 