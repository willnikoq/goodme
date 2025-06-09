package com.goodme.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 商品分类DTO
 */
@Data
@Schema(description = "商品分类")
public class CategoryDTO {
    
    @Schema(description = "分类ID")
    private Long id;
    
    @Schema(description = "分类名称")
    private String name;
    
    @Schema(description = "分类图片")
    private String image;
    
    @Schema(description = "父分类ID，0表示一级分类")
    private Long parentId;
    
    @Schema(description = "分类层级：1一级，2二级")
    private Integer level;
    
    @Schema(description = "排序值")
    private Integer sort;
    
    @Schema(description = "子分类列表")
    private List<CategoryDTO> children;
} 