package com.goodme.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goodme.service.entity.ProductSpecValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品规格值Mapper
 */
@Mapper
public interface ProductSpecValueMapper extends BaseMapper<ProductSpecValue> {
    
    /**
     * 根据商品ID查询规格值列表
     * 
     * @param productId 商品ID
     * @return 规格值列表
     */
    List<ProductSpecValue> getByProductId(@Param("productId") Long productId);
} 