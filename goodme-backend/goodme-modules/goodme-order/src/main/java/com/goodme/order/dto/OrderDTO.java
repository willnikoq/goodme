package com.goodme.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long shopId;
    private String shopName;
    private BigDecimal totalAmount;
    private String status;
    private String pickupCode;
    private String payType;
    private Date payTime;
    private Date cancelTime;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private List<OrderItemDTO> items;
} 