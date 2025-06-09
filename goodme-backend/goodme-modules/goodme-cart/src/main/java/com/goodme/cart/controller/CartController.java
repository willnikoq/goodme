package com.goodme.cart.controller;

import com.goodme.cart.dto.CartAddResultDTO;
import com.goodme.cart.dto.CartItemDTO;
import com.goodme.cart.dto.CartListDTO;
import com.goodme.cart.dto.CartUpdateResultDTO;
import com.goodme.cart.service.CartService;
import com.goodme.common.core.web.controller.BaseController;
import com.goodme.common.result.Result;
import com.goodme.common.security.annotation.RequiresLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 * 
 * @author goodme
 */
@Tag(name = "购物车管理", description = "购物车相关接口")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController extends BaseController {

    private final CartService cartService;

    /**
     * 添加商品到购物车
     * 
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @param skuId 商品规格ID
     * @param quantity 商品数量
     * @param specJson 规格JSON
     * @return 购物车商品总数
     */
    @Operation(summary = "添加商品到购物车", description = "将商品添加到用户的购物车中")
    @PostMapping(value = "/add", name = "添加商品到购物车")
    @RequiresLogin
    public Result<CartAddResultDTO> addToCart(
            @Parameter(description = "店铺ID") @RequestParam Long shopId,
            @Parameter(description = "商品ID") @RequestParam Long productId,
            @Parameter(description = "商品规格ID") @RequestParam Long skuId,
            @Parameter(description = "商品数量") @RequestParam Integer quantity,
            @Parameter(description = "规格JSON") @RequestParam(required = false) String specJson) {
        return cartService.addToCart(getUserId(), shopId, productId, skuId, quantity, specJson);
    }

    /**
     * 获取购物车列表
     * 
     * @param shopId 店铺ID
     * @return 购物车列表信息
     */
    @Operation(summary = "获取购物车列表", description = "获取当前用户指定店铺的购物车商品列表")
    @GetMapping(value = "/list", name = "获取购物车列表")
    @RequiresLogin
    public Result<CartListDTO> getCartList(
            @Parameter(description = "店铺ID") @RequestParam Long shopId) {
        return cartService.getCartList(getUserId(), shopId);
    }

    /**
     * 更新购物车商品数量
     * 
     * @param cartItemId 购物车项ID
     * @param quantity 商品数量
     * @return 更新后的总价
     */
    @Operation(summary = "更新购物车商品数量", description = "修改购物车中商品的数量")
    @PutMapping(value = "/update-quantity", name = "更新购物车商品数量")
    @RequiresLogin
    public Result<CartUpdateResultDTO> updateQuantity(
            @Parameter(description = "购物车项ID") @RequestParam Long cartItemId,
            @Parameter(description = "商品数量") @RequestParam Integer quantity) {
        return cartService.updateQuantity(getUserId(), cartItemId, quantity);
    }

    /**
     * 删除购物车商品
     * 
     * @param cartItemIds 购物车项ID列表
     * @return 操作结果
     */
    @Operation(summary = "删除购物车商品", description = "从购物车中删除指定的商品")
    @DeleteMapping(value = "/delete", name = "删除购物车商品")
    @RequiresLogin
    public Result<Void> deleteCartItems(
            @Parameter(description = "购物车项ID列表") @RequestParam List<Long> cartItemIds) {
        return cartService.deleteCartItems(getUserId(), cartItemIds);
    }

    /**
     * 清空购物车
     * 
     * @param shopId 店铺ID
     * @return 操作结果
     */
    @Operation(summary = "清空购物车", description = "清空指定店铺的所有购物车商品")
    @DeleteMapping(value = "/clear", name = "清空购物车")
    @RequiresLogin
    public Result<Void> clearCart(
            @Parameter(description = "店铺ID") @RequestParam Long shopId) {
        return cartService.clearCart(getUserId(), shopId);
    }

    /**
     * 更新购物车商品选中状态
     * 
     * @param cartItemId 购物车项ID
     * @param selected 是否选中
     * @return 操作结果
     */
    @Operation(summary = "更新购物车商品选中状态", description = "修改购物车中商品的选中状态")
    @PutMapping(value = "/update-selected", name = "更新购物车商品选中状态")
    @RequiresLogin
    public Result<Void> updateSelected(
            @Parameter(description = "购物车项ID") @RequestParam Long cartItemId,
            @Parameter(description = "是否选中") @RequestParam Boolean selected) {
        return cartService.updateSelected(getUserId(), cartItemId, selected);
    }
} 