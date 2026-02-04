package com.crm.mapper.customer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.entity.customer.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户信息Mapper
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
