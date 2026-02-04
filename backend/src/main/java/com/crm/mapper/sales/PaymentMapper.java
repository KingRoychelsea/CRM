package com.crm.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.entity.sales.Payment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 回款信息Mapper
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
}
