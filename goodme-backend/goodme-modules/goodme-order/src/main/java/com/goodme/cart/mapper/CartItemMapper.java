package com.goodme.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goodme.cart.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
} 