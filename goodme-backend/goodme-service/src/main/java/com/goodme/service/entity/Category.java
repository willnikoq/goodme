package com.goodme.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品分类实体类
 */
@Data
@TableName("category")
public class Category {
    
    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 分类图片
     */
    private String image;
    
    /**
     * 父分类ID，0表示一级分类
     */
    private Long parentId;
    
    /**
     * 分类层级：1一级，2二级
     */
    private Integer level;
    
    /**
     * 排序值，越小越靠前
     */
    private Integer sort;
    
    /**
     * 状态：0禁用，1启用
     */
    private Integer status;
    
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