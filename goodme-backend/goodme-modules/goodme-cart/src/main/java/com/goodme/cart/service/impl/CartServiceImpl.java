package com.goodme.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodme.cart.dto.CartAddResultDTO;
import com.goodme.cart.dto.CartItemDTO;
import com.goodme.cart.dto.CartItemSpecDTO;
import com.goodme.cart.dto.CartListDTO;
import com.goodme.cart.dto.CartUpdateResultDTO;
import com.goodme.cart.dto.ShopInfoDTO;
import com.goodme.cart.entity.CartItem;
import com.goodme.cart.mapper.CartItemMapper;
import com.goodme.cart.service.CartService;
import com.goodme.common.core.exception.BusinessException;
import com.goodme.common.result.Result;
import com.goodme.product.api.ProductFeignClient;
import com.goodme.product.api.dto.ProductDTO;
import com.goodme.product.api.dto.ProductSkuDTO;
import com.goodme.shop.api.ShopFeignClient;
import com.goodme.shop.api.dto.ShopDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartService {

    private final ProductFeignClient productFeignClient;
    private final ShopFeignClient shopFeignClient;
    private final ObjectMapper objectMapper;

    public CartServiceImpl(@Qualifier("mockProductFeignClient") ProductFeignClient productFeignClient,
                          @Qualifier("mockShopFeignClient") ShopFeignClient shopFeignClient,
                          ObjectMapper objectMapper) {
        this.productFeignClient = productFeignClient;
        this.shopFeignClient = shopFeignClient;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<CartAddResultDTO> addToCart(Long userId, Long shopId, Long productId, Long skuId, Integer quantity, String specJson) {
        // 1. 校验店铺是否存在
        Result<ShopDTO> shopResult = shopFeignClient.getShop(shopId);
        if (!shopResult.isSuccess()) {
            throw new BusinessException("店铺不存在");
        }
        
        // 2. 校验商品和规格是否存在
        Result<ProductDTO> productResult = productFeignClient.getProduct(productId);
        if (!productResult.isSuccess()) {
            throw new BusinessException("商品不存在");
        }
        ProductDTO product = productResult.getData();
        
        Result<ProductSkuDTO> skuResult = productFeignClient.getSku(skuId);
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
        } else {
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
        }
        
        // 5. 返回购物车商品总数
        Long cartCount = count(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId));
        
        return Result.success(new CartAddResultDTO(cartCount.intValue()));
    }

    @Override
    public Result<CartListDTO> getCartList(Long userId, Long shopId) {
        // 1. 获取购物车列表
        List<CartItem> cartItems = list(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getShopId, shopId)
                .orderByDesc(CartItem::getCreateTime));
        
        // 2. 获取店铺信息
        Result<ShopDTO> shopResult = shopFeignClient.getShop(shopId);
        if (!shopResult.isSuccess()) {
            throw new BusinessException("店铺不存在");
        }
        ShopDTO shop = shopResult.getData();
        
        // 3. 构建返回数据
        CartListDTO cartListDTO = new CartListDTO();
        
        // 设置店铺信息
        ShopInfoDTO shopInfoDTO = new ShopInfoDTO();
        shopInfoDTO.setId(shop.getId());
        shopInfoDTO.setName(shop.getName());
        shopInfoDTO.setLogo(shop.getLogo());
        cartListDTO.setShopInfo(shopInfoDTO);
        
        // 设置购物车商品列表
        List<CartItemDTO> cartItemDTOs = cartItems.stream()
                .map(item -> {
                    Result<ProductDTO> productResult = productFeignClient.getProduct(item.getProductId());
                    Result<ProductSkuDTO> skuResult = productFeignClient.getSku(item.getSkuId());
                    if (!productResult.isSuccess() || !skuResult.isSuccess()) {
                        return null;
                    }
                    return convertToDTO(item, productResult.getData(), skuResult.getData(), shop);
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
        
        cartListDTO.setItems(cartItemDTOs);
        
        // 计算商品总数和总价
        int totalCount = cartItemDTOs.stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();
        
        BigDecimal totalPrice = cartItemDTOs.stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        cartListDTO.setTotalCount(totalCount);
        cartListDTO.setTotalPrice(totalPrice);
        
        return Result.success(cartListDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<CartUpdateResultDTO> updateQuantity(Long userId, Long cartItemId, Integer quantity) {
        // 1. 校验购物车商品是否存在
        CartItem cartItem = getOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getId, cartItemId)
                .eq(CartItem::getUserId, userId));
        
        if (cartItem == null) {
            throw new BusinessException("购物车商品不存在");
        }
        
        // 2. 校验商品规格是否存在
        Result<ProductSkuDTO> skuResult = productFeignClient.getSku(cartItem.getSkuId());
        if (!skuResult.isSuccess()) {
            throw new BusinessException("商品规格不存在");
        }
        
        // 3. 更新数量和总价
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(skuResult.getData().getPrice().multiply(BigDecimal.valueOf(quantity)));
        updateById(cartItem);
        
        // 4. 返回更新后的总价
        return Result.success(new CartUpdateResultDTO(cartItem.getTotalPrice()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteCartItems(Long userId, List<Long> cartItemIds) {
        boolean removed = remove(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .in(CartItem::getId, cartItemIds));
        
        if (!removed) {
            throw new BusinessException("购物车商品不存在");
        }
        
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> clearCart(Long userId, Long shopId) {
        remove(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getShopId, shopId));
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateSelected(Long userId, Long cartItemId, Boolean selected) {
        CartItem cartItem = getOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getId, cartItemId)
                .eq(CartItem::getUserId, userId));
        
        if (cartItem == null) {
            throw new BusinessException("购物车商品不存在");
        }
        
        cartItem.setSelected(selected);
        updateById(cartItem);
        return Result.success();
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