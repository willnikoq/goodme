package com.goodme.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goodme.common.exception.BusinessException;
import com.goodme.service.dto.CategoryDTO;
import com.goodme.service.entity.Category;
import com.goodme.service.mapper.CategoryMapper;
import com.goodme.service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现类
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<CategoryDTO> getCategoryTree() {
        // 查询所有有效的分类
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, 1)
                   .orderByAsc(Category::getSort);
        List<Category> categories = list(queryWrapper);
        
        if (CollectionUtils.isEmpty(categories)) {
            return new ArrayList<>();
        }
        
        // 转换为DTO
        List<CategoryDTO> categoryDTOList = categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 构建树形结构
        return buildCategoryTree(categoryDTOList);
    }
    
    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = getById(id);
        if (category == null || category.getStatus() != 1) {
            throw new BusinessException("分类不存在或已禁用");
        }
        
        return convertToDTO(category);
    }
    
    /**
     * 构建分类树形结构
     */
    private List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categoryList) {
        // 先按parentId分组
        Map<Long, List<CategoryDTO>> parentIdMap = categoryList.stream()
                .collect(Collectors.groupingBy(CategoryDTO::getParentId));
        
        // 获取一级分类
        List<CategoryDTO> rootCategories = parentIdMap.getOrDefault(0L, new ArrayList<>());
        
        // 为一级分类设置子分类
        rootCategories.forEach(root -> {
            List<CategoryDTO> children = parentIdMap.getOrDefault(root.getId(), new ArrayList<>());
            children.sort(Comparator.comparing(CategoryDTO::getSort));
            root.setChildren(children);
        });
        
        return rootCategories.stream()
                .sorted(Comparator.comparing(CategoryDTO::getSort))
                .collect(Collectors.toList());
    }
    
    /**
     * 将实体转换为DTO
     */
    private CategoryDTO convertToDTO(Category category) {
        if (category == null) {
            return null;
        }
        
        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(category, dto);
        return dto;
    }
} 