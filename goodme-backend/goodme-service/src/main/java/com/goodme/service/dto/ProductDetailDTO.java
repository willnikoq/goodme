package com.goodme.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 商品详情DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品详情")
public class ProductDetailDTO extends ProductDTO {
    
    @Schema(description = "商品图片，多个图片用逗号分隔")
    private String images;
    
    @Schema(description = "商品规格详情")
    private List<ProductSpecDTO> specs;
    
    @Schema(description = "相关商品推荐")
    private List<ProductDTO> relatedProducts;
}