package com.goodme.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品规格值DTO
 */
@Data
@Schema(description = "商品规格值")
public class ProductSpecValueDTO {
    
    @Schema(description = "规格值ID")
    private Long id;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "规格ID")
    private Long specId;
    
    @Schema(description = "规格值")
    private String value;
    
    @Schema(description = "额外价格")
    private BigDecimal extraPrice;
    
    @Schema(description = "是否默认：0否，1是")
    private Integer isDefault;
} 