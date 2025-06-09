package com.goodme.shop.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 店铺DTO
 */
@Data
public class ShopDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long id;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 店铺logo
     */
    private String logo;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 营业时间
     */
    private String businessHours;

    /**
     * 状态：0关闭，1营业中
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 