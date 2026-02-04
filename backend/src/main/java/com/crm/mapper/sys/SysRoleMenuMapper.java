package com.crm.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.entity.sys.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单关联Mapper接口
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 批量插入角色菜单关联
     */
    void insertBatch(@Param("list") List<SysRoleMenu> list);
}
