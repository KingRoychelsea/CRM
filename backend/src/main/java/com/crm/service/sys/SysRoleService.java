package com.crm.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.entity.sys.SysRole;
import com.crm.dto.sys.SysRoleDTO;
import com.crm.vo.sys.SysRoleVO;

import java.util.List;

/**
 * 角色服务接口
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取角色列表
     */
    List<SysRoleVO> getRoleList(SysRoleDTO dto);

    /**
     * 创建角色
     */
    boolean createRole(SysRoleDTO dto);

    /**
     * 更新角色
     */
    boolean updateRole(SysRoleDTO dto);

    /**
     * 删除角色
     */
    boolean deleteRole(Long id);

    /**
     * 更新角色状态
     */
    boolean updateStatus(Long id, String status);

    /**
     * 获取角色菜单
     */
    List<Long> getRoleMenus(Long roleId);

    /**
     * 分配菜单权限
     */
    boolean assignMenus(Long roleId, List<Long> menuIds);

    /**
     * 获取所有角色
     */
    List<SysRole> getAllRoles();

    /**
     * 添加角色
     */
    boolean add(SysRoleDTO dto);

    /**
     * 更新角色
     */
    boolean update(SysRoleDTO dto);

    /**
     * 删除角色
     */
    boolean delete(Long id);

    /**
     * 批量删除角色
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 获取所有角色（不分页）
     */
    List<SysRoleVO> getAll();

    /**
     * 获取角色用户
     */
    List<?> getRoleUsers(Long roleId, Integer pageNum, Integer pageSize, String keyword, String status);

    /**
     * 分配用户
     */
    boolean assignUsers(Long roleId, List<Long> userIds);
}
