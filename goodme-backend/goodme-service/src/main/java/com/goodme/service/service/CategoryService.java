package com.goodme.service.service;

import com.goodme.service.dto.CategoryDTO;

import java.util.List;

/**
 * 商品分类服务接口
 */
public interface CategoryService {
    
    /**
     * 获取所有分类列表（树形结构）
     *
     * @return 分类列表
     */
    List<CategoryDTO> getCategoryTree();
    
    /**
     * 根据ID获取分类
     *
     * @param id 分类ID
     * @return 分类信息
     */
    CategoryDTO getCategoryById(Long id);
} 