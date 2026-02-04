package com.crm.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.entity.sys.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联Mapper接口
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 批量插入用户角色关联
     */
    void insertBatch(@Param("list") List<SysUserRole> list);
}
