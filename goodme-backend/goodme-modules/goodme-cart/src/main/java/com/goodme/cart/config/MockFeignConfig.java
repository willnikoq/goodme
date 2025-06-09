package com.goodme.cart.config;

import com.goodme.common.result.Result;
import com.goodme.product.api.ProductFeignClient;
import com.goodme.product.api.dto.ProductDTO;
import com.goodme.product.api.dto.ProductSkuDTO;
import com.goodme.shop.api.ShopFeignClient;
import com.goodme.shop.api.dto.ShopDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * 模拟Feign客户端配置
 * 在微服务间通信未完全建立前，提供模拟实现
 */
@Configuration
public class MockFeignConfig {

    @Bean("mockProductFeignClient")
    public ProductFeignClient mockProductFeignClient() {
        return new ProductFeignClient() {
            @Override
            public Result<ProductDTO> getProduct(Long productId) {
                // 模拟商品数据
                ProductDTO product = new ProductDTO();
                product.setId(productId);
                product.setName("模拟商品-" + productId);
                product.setMainImage("https://example.com/product.jpg");
                product.setPrice(new BigDecimal("99.99"));
                product.setDescription("这是一个模拟商品");
                return Result.success(product);
            }

            @Override
            public Result<ProductSkuDTO> getSku(Long skuId) {
                // 模拟商品规格数据
                ProductSkuDTO sku = new ProductSkuDTO();
                sku.setId(skuId);
                sku.setName("默认规格");
                sku.setPrice(new BigDecimal("99.99"));
                sku.setStock(999);
                return Result.success(sku);
            }
        };
    }

    @Bean("mockShopFeignClient")
    public ShopFeignClient mockShopFeignClient() {
        return new ShopFeignClient() {
            @Override
            public Result<ShopDTO> getShop(Long shopId) {
                // 模拟店铺数据
                ShopDTO shop = new ShopDTO();
                shop.setId(shopId);
                shop.setName("模拟店铺-" + shopId);
                shop.setLogo("https://example.com/shop-logo.jpg");
                shop.setStatus(1);
                return Result.success(shop);
            }
        };
    }
} 