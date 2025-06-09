package com.goodme.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodme.common.core.domain.R;
import com.goodme.order.dto.OrderCreateRequest;
import com.goodme.order.dto.OrderDTO;
import com.goodme.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 订单API测试类
 */
@SpringBootTest
@AutoConfigureMockMvc
public class OrderApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderDTO mockOrder;
    private List<OrderDTO> mockOrders;
    private OrderCreateRequest createRequest;

    @BeforeEach
    public void setup() {
        // 模拟订单
        mockOrder = new OrderDTO();
        mockOrder.setId(1L);
        mockOrder.setOrderNo("O202506091234567");
        mockOrder.setUserId(1L);
        mockOrder.setShopId(1L);
        mockOrder.setTotalAmount(new BigDecimal("45.00"));
        mockOrder.setStatus("UNPAID");
        mockOrder.setCreateTime(new Date());
        
        // 模拟订单列表
        mockOrders = new ArrayList<>();
        mockOrders.add(mockOrder);
        
        // 模拟创建订单请求
        createRequest = new OrderCreateRequest();
        createRequest.setShopId(1L);
        createRequest.setRemark("少糖，多冰");
        
        List<OrderCreateRequest.OrderItemRequest> items = new ArrayList<>();
        OrderCreateRequest.OrderItemRequest item1 = new OrderCreateRequest.OrderItemRequest();
        item1.setProductId(101L);
        item1.setSkuId(1001L);
        item1.setQuantity(2);
        item1.setSpecJson("[{\"specId\":1,\"specName\":\"规格\",\"valueId\":1,\"valueName\":\"中杯\"}]");
        items.add(item1);
        
        OrderCreateRequest.OrderItemRequest item2 = new OrderCreateRequest.OrderItemRequest();
        item2.setProductId(102L);
        item2.setSkuId(1002L);
        item2.setQuantity(1);
        item2.setSpecJson("[{\"specId\":1,\"specName\":\"规格\",\"valueId\":2,\"valueName\":\"大杯\"}]");
        items.add(item2);
        
        createRequest.setItems(items);
    }

    @Test
    public void testCreateOrder() throws Exception {
        when(orderService.createOrder(anyLong(), any(OrderCreateRequest.class)))
                .thenReturn(R.ok(mockOrder));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.orderNo").value("O202506091234567"));
    }

    @Test
    public void testGetOrderList() throws Exception {
        when(orderService.getOrderList(anyLong()))
                .thenReturn(R.ok(mockOrders));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].orderNo").value("O202506091234567"));
    }

    @Test
    public void testGetOrderDetail() throws Exception {
        when(orderService.getOrderDetail(anyLong(), anyLong()))
                .thenReturn(R.ok(mockOrder));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/detail")
                .param("orderId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.orderNo").value("O202506091234567"));
    }

    @Test
    public void testCancelOrder() throws Exception {
        when(orderService.cancelOrder(anyLong(), anyLong()))
                .thenReturn(R.ok());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/order/cancel")
                .param("orderId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testPayOrder() throws Exception {
        when(orderService.payOrder(anyLong(), anyLong(), anyString()))
                .thenReturn(R.ok());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/order/pay")
                .param("orderId", "1")
                .param("payType", "WECHAT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testQueryPayResult() throws Exception {
        when(orderService.queryPayResult(anyLong(), anyLong()))
                .thenReturn(R.ok(true));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/pay-result")
                .param("orderId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }
} 