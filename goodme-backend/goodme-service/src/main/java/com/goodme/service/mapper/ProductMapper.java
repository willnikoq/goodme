package com.goodme.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goodme.service.dto.ProductDTO;
import com.goodme.service.dto.ProductQueryRequest;
import com.goodme.service.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品Mapper
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    
    /**
     * 分页查询商品
     * 
     * @param page 分页参数
     * @param query 查询条件
     * @return 商品列表
     */
    IPage<ProductDTO> queryProductPage(Page<ProductDTO> page, @Param("query") ProductQueryRequest query);
} 