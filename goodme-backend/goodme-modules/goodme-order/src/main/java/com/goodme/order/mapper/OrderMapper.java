package com.goodme.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goodme.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
} 