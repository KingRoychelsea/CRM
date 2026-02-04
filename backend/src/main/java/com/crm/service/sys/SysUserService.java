package com.crm.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.entity.sys.SysUser;
import com.crm.dto.sys.SysUserDTO;
import com.crm.vo.sys.SysUserVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户登录
     */
    Object login(SysUserDTO loginDTO);

    /**
     * 获取当前用户信息
     */
    SysUserVO getCurrentUser();

    /**
     * 用户分页查询
     */
    Object list(Integer pageNum, Integer pageSize, String username, String realName, Long deptId, Integer status);

    /**
     * 获取用户详情
     */
    SysUserVO getById(Long id);

    /**
     * 新增用户
     */
    void add(SysUserDTO userDTO);

    /**
     * 修改用户
     */
    void update(SysUserDTO userDTO);

    /**
     * 删除用户
     */
    void delete(Long id);

    /**
     * 批量删除用户
     */
    void batchDelete(List<Long> ids);

    /**
     * 重置密码
     */
    void resetPassword(Long id);

    /**
     * 启用/禁用用户
     */
    void updateStatus(Long id, Integer status);

    /**
     * 获取用户角色
     */
    List<Long> getUserRoles(Long id);

    /**
     * 分配角色
     */
    void assignRoles(Long id, List<Long> roleIds);

    /**
     * 根据用户名查询用户
     */
    SysUser getByUsername(String username);

    /**
     * 获取用户权限
     */
    List<String> getPermissions(Long userId);

    /**
     * 获取用户角色
     */
    List<Long> getRoles(Long userId);
}
