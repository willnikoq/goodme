package com.goodme.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品规格实体类
 */
@Data
@TableName("product_spec")
public class ProductSpec {
    
    /**
     * 规格ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 规格名称（如：杯型、温度、甜度、加料）
     */
    private String name;
    
    /**
     * 排序值
     */
    private Integer sort;
    
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