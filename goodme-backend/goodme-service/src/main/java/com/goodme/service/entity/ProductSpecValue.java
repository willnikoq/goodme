package com.goodme.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品规格值实体类
 */
@Data
@TableName("product_spec_value")
public class ProductSpecValue {
    
    /**
     * 规格值ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 规格ID
     */
    private Long specId;
    
    /**
     * 规格值（如：中杯、常温、全糖、珍珠）
     */
    private String value;
    
    /**
     * 额外价格
     */
    private BigDecimal extraPrice;
    
    /**
     * 是否默认：0否，1是
     */
    private Integer isDefault;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除：0未删除，1已删除
     */
    @TableLogic
    private Integer deleted;
} 