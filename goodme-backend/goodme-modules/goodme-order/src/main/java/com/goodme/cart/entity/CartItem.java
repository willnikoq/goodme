package com.goodme.cart.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.goodme.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cart_item")
public class CartItem extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品规格ID
     */
    private Long skuId;
    
    /**
     * 商品数量
     */
    private Integer quantity;
    
    /**
     * 商品单价
     */
    private BigDecimal price;
    
    /**
     * 商品总价
     */
    private BigDecimal totalPrice;
    
    /**
     * 是否选中
     */
    private Boolean selected;
} 