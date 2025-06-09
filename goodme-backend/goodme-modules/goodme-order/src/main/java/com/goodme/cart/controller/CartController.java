package com.goodme.cart.controller;

import com.goodme.cart.dto.CartItemDTO;
import com.goodme.cart.service.CartService;
import com.goodme.common.core.domain.R;
import com.goodme.common.core.web.controller.BaseController;
import com.goodme.common.security.annotation.RequiresLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "购物车管理")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController extends BaseController {

    private final CartService cartService;

    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    @RequiresLogin
    public R<CartItemDTO> addToCart(
            @ApiParam("店铺ID") @RequestParam Long shopId,
            @ApiParam("商品ID") @RequestParam Long productId,
            @ApiParam("商品规格ID") @RequestParam Long skuId,
            @ApiParam("商品数量") @RequestParam Integer quantity,
            @ApiParam("规格JSON") @RequestParam(required = false) String specJson) {
        return cartService.addToCart(getUserId(), shopId, productId, skuId, quantity, specJson);
    }

    @ApiOperation("获取购物车列表")
    @GetMapping("/list")
    @RequiresLogin
    public R<List<CartItemDTO>> getCartList(
            @ApiParam("店铺ID") @RequestParam Long shopId) {
        return cartService.getCartList(getUserId(), shopId);
    }

    @ApiOperation("更新购物车商品数量")
    @PutMapping("/update-quantity")
    @RequiresLogin
    public R<CartItemDTO> updateQuantity(
            @ApiParam("购物车项ID") @RequestParam Long cartItemId,
            @ApiParam("商品数量") @RequestParam Integer quantity) {
        return cartService.updateQuantity(getUserId(), cartItemId, quantity);
    }

    @ApiOperation("删除购物车商品")
    @DeleteMapping("/delete")
    @RequiresLogin
    public R<Void> deleteCartItems(
            @ApiParam("购物车项ID列表") @RequestParam List<Long> cartItemIds) {
        return cartService.deleteCartItems(getUserId(), cartItemIds);
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clear")
    @RequiresLogin
    public R<Void> clearCart(
            @ApiParam("店铺ID") @RequestParam Long shopId) {
        return cartService.clearCart(getUserId(), shopId);
    }

    @ApiOperation("更新购物车商品选中状态")
    @PutMapping("/update-selected")
    @RequiresLogin
    public R<Void> updateSelected(
            @ApiParam("购物车项ID") @RequestParam Long cartItemId,
            @ApiParam("是否选中") @RequestParam Boolean selected) {
        return cartService.updateSelected(getUserId(), cartItemId, selected);
    }
} 