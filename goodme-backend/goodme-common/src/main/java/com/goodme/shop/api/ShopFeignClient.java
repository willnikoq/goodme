package com.goodme.shop.api;

import com.goodme.common.result.Result;
import com.goodme.shop.api.dto.ShopDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 店铺服务Feign客户端
 */
@FeignClient(value = "goodme-shop", path = "/shop")
public interface ShopFeignClient {

    /**
     * 获取店铺信息
     *
     * @param id 店铺ID
     * @return 店铺信息
     */
    @GetMapping("/api/shop/{id}")
    Result<ShopDTO> getShop(@PathVariable("id") Long id);
} 