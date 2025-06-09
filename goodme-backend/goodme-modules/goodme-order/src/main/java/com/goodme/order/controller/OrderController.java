package com.goodme.order.controller;

import com.goodme.common.core.domain.R;
import com.goodme.common.core.web.controller.BaseController;
import com.goodme.common.security.annotation.RequiresLogin;
import com.goodme.order.dto.OrderCreateRequest;
import com.goodme.order.dto.OrderDTO;
import com.goodme.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "订单管理")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController extends BaseController {
    private final OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping("/create")
    @RequiresLogin
    public R<OrderDTO> createOrder(@RequestBody OrderCreateRequest request) {
        return orderService.createOrder(getUserId(), request);
    }

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    @RequiresLogin
    public R<List<OrderDTO>> getOrderList() {
        return orderService.getOrderList(getUserId());
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/detail")
    @RequiresLogin
    public R<OrderDTO> getOrderDetail(@ApiParam("订单ID") @RequestParam Long orderId) {
        return orderService.getOrderDetail(getUserId(), orderId);
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel")
    @RequiresLogin
    public R<Void> cancelOrder(@ApiParam("订单ID") @RequestParam Long orderId) {
        return orderService.cancelOrder(getUserId(), orderId);
    }

    @ApiOperation("支付订单")
    @PutMapping("/pay")
    @RequiresLogin
    public R<Void> payOrder(@ApiParam("订单ID") @RequestParam Long orderId,
                            @ApiParam("支付方式") @RequestParam String payType) {
        return orderService.payOrder(getUserId(), orderId, payType);
    }

    @ApiOperation("查询支付结果")
    @GetMapping("/pay-result")
    @RequiresLogin
    public R<Boolean> queryPayResult(@ApiParam("订单ID") @RequestParam Long orderId) {
        return orderService.queryPayResult(getUserId(), orderId);
    }
} 