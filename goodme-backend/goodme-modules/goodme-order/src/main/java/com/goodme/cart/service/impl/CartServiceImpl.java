package com.goodme.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodme.cart.dto.CartItemDTO;
import com.goodme.cart.dto.CartItemSpecDTO;
import com.goodme.cart.entity.CartItem;
import com.goodme.cart.mapper.CartItemMapper;
import com.goodme.cart.service.CartService;
import com.goodme.common.core.domain.R;
import com.goodme.common.core.exception.BusinessException;
import com.goodme.product.api.ProductFeignClient;
import com.goodme.product.api.dto.ProductDTO;
import com.goodme.product.api.dto.ProductSkuDTO;
import com.goodme.shop.api.ShopFeignClient;
import com.goodme.shop.api.dto.ShopDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartService {

    private final ProductFeignClient productFeignClient;
    private final ShopFeignClient shopFeignClient;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<CartItemDTO> addToCart(Long userId, Long shopId, Long productId, Long skuId, Integer quantity, String specJson) {
        // 1. 校验店铺是否存在
        R<ShopDTO> shopResult = shopFeignClient.getShop(shopId);
        if (!shopResult.isSuccess()) {
            throw new BusinessException("店铺不存在");
        }
        
        // 2. 校验商品和规格是否存在
        R<ProductDTO> productResult = productFeignClient.getProduct(productId);
        if (!productResult.isSuccess()) {
            throw new BusinessException("商品不存在");
        }
        ProductDTO product = productResult.getData();
        
        R<ProductSkuDTO> skuResult = productFeignClient.getSku(skuId);
        if (!skuResult.isSuccess()) {
            throw new BusinessException("商品规格不存在");
        }
        ProductSkuDTO sku = skuResult.getData();
        
        // 3. 检查购物车是否已存在该商品
        CartItem existItem = getOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getShopId, shopId)
                .eq(CartItem::getProductId, productId)
                .eq(CartItem::getSkuId, skuId));
        
        if (existItem != null) {
            // 已存在则更新数量
            existItem.setQuantity(existItem.getQuantity() + quantity);
            existItem.setTotalPrice(sku.getPrice().multiply(BigDecimal.valueOf(existItem.getQuantity())));
            updateById(existItem);
            return R.ok(convertToDTO(existItem, product, sku, shopResult.getData()));
        }
        
        // 4. 不存在则新增
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setShopId(shopId);
        cartItem.setProductId(productId);
        cartItem.setSkuId(skuId);
        cartItem.setSpecJson(specJson);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(sku.getPrice());
        cartItem.setTotalPrice(sku.getPrice().multiply(BigDecimal.valueOf(quantity)));
        cartItem.setSelected(true);
        
        save(cartItem);
        return R.ok(convertToDTO(cartItem, product, sku, shopResult.getData()));
    }

    @Override
    public R<List<CartItemDTO>> getCartList(Long userId, Long shopId) {
        List<CartItem> cartItems = list(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getShopId, shopId)
                .orderByDesc(CartItem::getCreateTime));
        
        // 获取店铺信息
        R<ShopDTO> shopResult = shopFeignClient.getShop(shopId);
        if (!shopResult.isSuccess()) {
            throw new BusinessException("店铺不存在");
        }
        ShopDTO shop = shopResult.getData();
        
        List<CartItemDTO> cartItemDTOs = cartItems.stream()
                .map(item -> {
                    R<ProductDTO> productResult = productFeignClient.getProduct(item.getProductId());
                    R<ProductSkuDTO> skuResult = productFeignClient.getSku(item.getSkuId());
                    if (!productResult.isSuccess() || !skuResult.isSuccess()) {
                        return null;
                    }
                    return convertToDTO(item, productResult.getData(), skuResult.getData(), shop);
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
        
        return R.ok(cartItemDTOs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<CartItemDTO> updateQuantity(Long userId, Long cartItemId, Integer quantity) {
        CartItem cartItem = getOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getId, cartItemId)
                .eq(CartItem::getUserId, userId));
        
        if (cartItem == null) {
            throw new BusinessException("购物车商品不存在");
        }
        
        R<ProductSkuDTO> skuResult = productFeignClient.getSku(cartItem.getSkuId());
        if (!skuResult.isSuccess()) {
            throw new BusinessException("商品规格不存在");
        }
        
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(skuResult.getData().getPrice().multiply(BigDecimal.valueOf(quantity)));
        updateById(cartItem);
        
        R<ProductDTO> productResult = productFeignClient.getProduct(cartItem.getProductId());
        R<ShopDTO> shopResult = shopFeignClient.getShop(cartItem.getShopId());
        return R.ok(convertToDTO(cartItem, productResult.getData(), skuResult.getData(), shopResult.getData()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> deleteCartItems(Long userId, List<Long> cartItemIds) {
        boolean removed = remove(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .in(CartItem::getId, cartItemIds));
        
        if (!removed) {
            throw new BusinessException("购物车商品不存在");
        }
        
        return R.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> clearCart(Long userId, Long shopId) {
        remove(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getShopId, shopId));
        return R.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> updateSelected(Long userId, Long cartItemId, Boolean selected) {
        CartItem cartItem = getOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getId, cartItemId)
                .eq(CartItem::getUserId, userId));
        
        if (cartItem == null) {
            throw new BusinessException("购物车商品不存在");
        }
        
        cartItem.setSelected(selected);
        updateById(cartItem);
        return R.ok();
    }

    private CartItemDTO convertToDTO(CartItem cartItem, ProductDTO product, ProductSkuDTO sku, ShopDTO shop) {
        CartItemDTO dto = new CartItemDTO();
        BeanUtils.copyProperties(cartItem, dto);
        dto.setProductName(product.getName());
        dto.setProductImage(product.getMainImage());
        dto.setSkuName(sku.getName());
        dto.setShopName(shop.getName());
        dto.setShopLogo(shop.getLogo());
        
        // 转换规格JSON
        try {
            if (cartItem.getSpecJson() != null) {
                List<CartItemSpecDTO> specs = objectMapper.readValue(cartItem.getSpecJson(), 
                    new TypeReference<List<CartItemSpecDTO>>() {});
                dto.setSpecs(specs);
            }
        } catch (Exception e) {
            log.error("转换规格JSON失败", e);
        }
        
        return dto;
    }
} 