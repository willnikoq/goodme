package com.goodme.cart.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemSpecDTO {
    /**
     * 规格名称
     */
    private String specName;
    
    /**
     * 规格值名称
     */
    private String valueName;
    
    /**
     * 额外价格
     */
    private BigDecimal extraPrice;
} 