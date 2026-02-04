package com.crm.mapper.customer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.entity.customer.Contact;
import org.apache.ibatis.annotations.Mapper;

/**
 * 联系人信息Mapper
 */
@Mapper
public interface ContactMapper extends BaseMapper<Contact> {
}
