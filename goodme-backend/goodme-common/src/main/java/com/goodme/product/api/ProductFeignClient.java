package com.goodme.product.api;

import com.goodme.common.result.Result;
import com.goodme.product.api.dto.ProductDTO;
import com.goodme.product.api.dto.ProductSkuDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品服务Feign客户端
 */
@FeignClient(value = "goodme-product", path = "/product")
public interface ProductFeignClient {

    /**
     * 获取商品信息
     *
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/api/product/{id}")
    Result<ProductDTO> getProduct(@PathVariable("id") Long id);

    /**
     * 获取商品规格信息
     *
     * @param id 规格ID
     * @return 规格信息
     */
    @GetMapping("/api/product/sku/{id}")
    Result<ProductSkuDTO> getSku(@PathVariable("id") Long id);
} 