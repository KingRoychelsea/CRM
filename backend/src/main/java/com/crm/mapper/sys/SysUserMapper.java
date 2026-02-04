package com.crm.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.entity.sys.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
