package com.goodme.order.service;

import com.goodme.order.dto.OrderCreateRequest;
import com.goodme.order.dto.OrderDTO;
import com.goodme.common.core.domain.R;
import java.util.List;

public interface OrderService {
    R<OrderDTO> createOrder(Long userId, OrderCreateRequest request);
    R<List<OrderDTO>> getOrderList(Long userId);
    R<OrderDTO> getOrderDetail(Long userId, Long orderId);
    R<Void> cancelOrder(Long userId, Long orderId);
    R<Void> payOrder(Long userId, Long orderId, String payType);
    R<Boolean> queryPayResult(Long userId, Long orderId);
} 