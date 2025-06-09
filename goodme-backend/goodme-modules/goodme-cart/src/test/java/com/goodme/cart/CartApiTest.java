package com.goodme.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodme.cart.dto.CartItemDTO;
import com.goodme.cart.service.CartService;
import com.goodme.common.result.Result;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 购物车API测试类
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CartApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private CartItemDTO mockCartItem;
    private List<CartItemDTO> mockCartItems;

    @BeforeEach
    public void setup() {
        // 模拟购物车项
        mockCartItem = new CartItemDTO();
        mockCartItem.setId(1L);
        mockCartItem.setUserId(1L);
        mockCartItem.setShopId(1L);
        mockCartItem.setProductId(101L);
        mockCartItem.setSkuId(1001L);
        mockCartItem.setProductName("珍珠奶茶");
        mockCartItem.setProductImage("https://example.com/image.jpg");
        mockCartItem.setPrice(new BigDecimal("15.00"));
        mockCartItem.setQuantity(2);
        mockCartItem.setTotalPrice(new BigDecimal("30.00"));
        mockCartItem.setSelected(true);

        // 模拟购物车列表
        mockCartItems = new ArrayList<>();
        mockCartItems.add(mockCartItem);
    }

    @Test
    public void testAddToCart() throws Exception {
        when(cartService.addToCart(anyLong(), anyLong(), anyLong(), anyLong(), anyInt(), anyString()))
                .thenReturn(Result.success(mockCartItem));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/add")
                .param("shopId", "1")
                .param("productId", "101")
                .param("skuId", "1001")
                .param("quantity", "2")
                .param("specJson", "[{\"specId\":1,\"specName\":\"规格\",\"valueId\":1,\"valueName\":\"中杯\"}]")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.productName").value("珍珠奶茶"));
    }

    @Test
    public void testGetCartList() throws Exception {
        when(cartService.getCartList(anyLong(), anyLong()))
                .thenReturn(Result.success(mockCartItems));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/list")
                .param("shopId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].productName").value("珍珠奶茶"));
    }

    @Test
    public void testUpdateQuantity() throws Exception {
        CartItemDTO updatedItem = new CartItemDTO();
        updatedItem.setId(1L);
        updatedItem.setQuantity(5);
        updatedItem.setTotalPrice(new BigDecimal("75.00"));
        updatedItem.setProductName("珍珠奶茶");

        when(cartService.updateQuantity(anyLong(), anyLong(), anyInt()))
                .thenReturn(Result.success(updatedItem));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/update-quantity")
                .param("cartItemId", "1")
                .param("quantity", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.quantity").value(5));
    }

    @Test
    public void testDeleteCartItems() throws Exception {
        when(cartService.deleteCartItems(anyLong(), anyList()))
                .thenReturn(Result.success());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/delete")
                .param("cartItemIds", "1,2,3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testClearCart() throws Exception {
        when(cartService.clearCart(anyLong(), anyLong()))
                .thenReturn(Result.success());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/clear")
                .param("shopId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testUpdateSelected() throws Exception {
        when(cartService.updateSelected(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(Result.success());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/update-selected")
                .param("cartItemId", "1")
                .param("selected", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
} 