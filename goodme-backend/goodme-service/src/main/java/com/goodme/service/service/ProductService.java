package com.goodme.service.service;

import com.goodme.service.dto.PageResult;
import com.goodme.service.dto.ProductDTO;
import com.goodme.service.dto.ProductDetailDTO;
import com.goodme.service.dto.ProductQueryRequest;

/**
 * 商品服务接口
 */
public interface ProductService {
    
    /**
     * 分页查询商品列表
     *
     * @param request 查询条件
     * @return 商品分页列表
     */
    PageResult<ProductDTO> getProductPage(ProductQueryRequest request);
    
    /**
     * 获取商品详情
     *
     * @param id 商品ID
     * @return 商品详情
     */
    ProductDetailDTO getProductDetail(Long id);
} 