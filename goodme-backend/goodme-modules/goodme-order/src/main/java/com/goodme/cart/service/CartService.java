package com.goodme.cart.service;

import com.goodme.cart.dto.CartItemDTO;
import com.goodme.common.core.domain.R;

import java.util.List;

public interface CartService {
    
    /**
     * 添加商品到购物车
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @param skuId 商品规格ID
     * @param quantity 商品数量
     * @return 操作结果
     */
    R<CartItemDTO> addToCart(Long userId, Long productId, Long skuId, Integer quantity);
    
    /**
     * 获取购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    R<List<CartItemDTO>> getCartList(Long userId);
    
    /**
     * 更新购物车商品数量
     *
     * @param userId 用户ID
     * @param cartItemId 购物车项ID
     * @param quantity 商品数量
     * @return 操作结果
     */
    R<CartItemDTO> updateQuantity(Long userId, Long cartItemId, Integer quantity);
    
    /**
     * 删除购物车商品
     *
     * @param userId 用户ID
     * @param cartItemId 购物车项ID
     * @return 操作结果
     */
    R<Void> deleteCartItem(Long userId, Long cartItemId);
    
    /**
     * 清空购物车
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    R<Void> clearCart(Long userId);
    
    /**
     * 更新购物车商品选中状态
     *
     * @param userId 用户ID
     * @param cartItemId 购物车项ID
     * @param selected 是否选中
     * @return 操作结果
     */
    R<Void> updateSelected(Long userId, Long cartItemId, Boolean selected);
} 