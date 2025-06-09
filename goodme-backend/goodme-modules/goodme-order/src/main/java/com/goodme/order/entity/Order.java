package com.goodme.order.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long shopId;
    private BigDecimal totalAmount;
    private String status;
    private String pickupCode;
    private String payType;
    private Date payTime;
    private Date cancelTime;
    private String remark;
    private Date createTime;
    private Date updateTime;
} 