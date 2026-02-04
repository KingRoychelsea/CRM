package com.crm.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.entity.sales.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单信息Mapper
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
