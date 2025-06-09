package com.goodme.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 */
@Data
@TableName("product")
public class Product {
    
    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 商品名称
     */
    private String name;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 缩略图
     */
    private String thumbnail;
    
    /**
     * 商品图片，多个图片用逗号分隔
     */
    private String images;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商品价格
     */
    private BigDecimal price;
    
    /**
     * 库存
     */
    private Integer stock;
    
    /**
     * 库存预警值
     */
    private Integer stockWarning;
    
    /**
     * 销量
     */
    private Integer sales;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 重量(克)
     */
    private BigDecimal weight;
    
    /**
     * 状态：0下架，1上架
     */
    private Integer status;
    
    /**
     * 是否新品：0否，1是
     */
    private Integer isNew;
    
    /**
     * 是否热销：0否，1是
     */
    private Integer isHot;
    
    /**
     * 排序值，越小越靠前
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