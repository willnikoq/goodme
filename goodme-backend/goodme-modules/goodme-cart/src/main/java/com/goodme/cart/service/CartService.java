package com.goodme.cart.service;

import com.goodme.cart.dto.CartAddResultDTO;
import com.goodme.cart.dto.CartItemDTO;
import com.goodme.cart.dto.CartListDTO;
import com.goodme.cart.dto.CartUpdateResultDTO;
import com.goodme.common.result.Result;

import java.util.List;

public interface CartService {
    
    /**
     * 添加商品到购物车
     *
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @param skuId 商品规格ID
     * @param quantity 商品数量
     * @param specJson 规格JSON
     * @return 操作结果
     */
    Result<CartAddResultDTO> addToCart(Long userId, Long shopId, Long productId, Long skuId, Integer quantity, String specJson);
    
    /**
     * 获取购物车列表
     *
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 购物车列表
     */
    Result<CartListDTO> getCartList(Long userId, Long shopId);
    
    /**
     * 更新购物车商品数量
     *
     * @param userId 用户ID
     * @param cartItemId 购物车项ID
     * @param quantity 商品数量
     * @return 操作结果
     */
    Result<CartUpdateResultDTO> updateQuantity(Long userId, Long cartItemId, Integer quantity);
    
    /**
     * 删除购物车商品
     *
     * @param userId 用户ID
     * @param cartItemIds 购物车项ID列表
     * @return 操作结果
     */
    Result<Void> deleteCartItems(Long userId, List<Long> cartItemIds);
    
    /**
     * 清空购物车
     *
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 操作结果
     */
    Result<Void> clearCart(Long userId, Long shopId);
    
    /**
     * 更新购物车商品选中状态
     *
     * @param userId 用户ID
     * @param cartItemId 购物车项ID
     * @param selected 是否选中
     * @return 操作结果
     */
    Result<Void> updateSelected(Long userId, Long cartItemId, Boolean selected);
} 