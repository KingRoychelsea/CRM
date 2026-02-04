package com.crm.mapper.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.entity.sales.Contract;
import org.apache.ibatis.annotations.Mapper;

/**
 * 合同信息Mapper
 */
@Mapper
public interface ContractMapper extends BaseMapper<Contract> {
}
