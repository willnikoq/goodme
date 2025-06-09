package com.goodme.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goodme.common.core.domain.R;
import com.goodme.common.core.exception.BusinessException;
import com.goodme.order.dto.OrderCreateRequest;
import com.goodme.order.dto.OrderDTO;
import com.goodme.order.dto.OrderItemDTO;
import com.goodme.order.entity.Order;
import com.goodme.order.entity.OrderItem;
import com.goodme.order.mapper.OrderItemMapper;
import com.goodme.order.mapper.OrderMapper;
import com.goodme.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<OrderDTO> createOrder(Long userId, OrderCreateRequest request) {
        // 1. 构建订单主表
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setShopId(request.getShopId());
        order.setStatus("UNPAID");
        order.setRemark(request.getRemark());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        // 2. 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderCreateRequest.OrderItemRequest itemReq : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setProductId(itemReq.getProductId());
            item.setSkuName(""); // 可根据skuId查找skuName
            item.setSpecJson(itemReq.getSpecJson());
            item.setPrice(BigDecimal.valueOf(10)); // TODO: 实际应查商品价格
            item.setQuantity(itemReq.getQuantity());
            item.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            totalAmount = totalAmount.add(item.getTotalPrice());
            orderItems.add(item);
        }
        order.setTotalAmount(totalAmount);
        // 3. 保存订单
        orderMapper.insert(order);
        // 4. 保存订单明细
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }
        // 5. 返回DTO
        OrderDTO dto = toOrderDTO(order, orderItems);
        return R.ok(dto);
    }

    @Override
    public R<List<OrderDTO>> getOrderList(Long userId) {
        List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId).orderByDesc(Order::getCreateTime));
        List<OrderDTO> dtos = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
            dtos.add(toOrderDTO(order, items));
        }
        return R.ok(dtos);
    }

    @Override
    public R<OrderDTO> getOrderDetail(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        return R.ok(toOrderDTO(order, items));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> cancelOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (!"UNPAID".equals(order.getStatus())) {
            throw new BusinessException("订单不可取消");
        }
        order.setStatus("CANCELLED");
        order.setCancelTime(new Date());
        orderMapper.updateById(order);
        return R.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> payOrder(Long userId, Long orderId, String payType) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (!"UNPAID".equals(order.getStatus())) {
            throw new BusinessException("订单不可支付");
        }
        order.setStatus("PAID");
        order.setPayType(payType);
        order.setPayTime(new Date());
        // 生成取餐码
        order.setPickupCode(generatePickupCode());
        orderMapper.updateById(order);
        return R.ok();
    }

    @Override
    public R<Boolean> queryPayResult(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        return R.ok("PAID".equals(order.getStatus()));
    }

    private OrderDTO toOrderDTO(Order order, List<OrderItem> items) {
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(order, dto);
        List<OrderItemDTO> itemDTOs = items.stream().map(item -> {
            OrderItemDTO d = new OrderItemDTO();
            BeanUtils.copyProperties(item, d);
            return d;
        }).collect(Collectors.toList());
        dto.setItems(itemDTOs);
        return dto;
    }

    private String generateOrderNo() {
        return "O" + System.currentTimeMillis() + (int)(Math.random()*1000);
    }

    private String generatePickupCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
} 