package com.crm.mapper.customer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.entity.customer.FollowRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户跟进记录Mapper
 */
@Mapper
public interface FollowRecordMapper extends BaseMapper<FollowRecord> {
}
