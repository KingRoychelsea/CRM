package com.crm.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.entity.sys.SysMenu;
import com.crm.dto.sys.SysMenuDTO;
import com.crm.vo.sys.SysMenuVO;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单列表
     */
    List<SysMenuVO> getMenuList(SysMenuDTO dto);

    /**
     * 获取菜单树
     */
    List<SysMenuVO> getMenuTree();

    /**
     * 创建菜单
     */
    boolean createMenu(SysMenuDTO dto);

    /**
     * 更新菜单
     */
    boolean updateMenu(SysMenuDTO dto);

    /**
     * 删除菜单
     */
    boolean deleteMenu(Long id);

    /**
     * 更新菜单状态
     */
    boolean updateStatus(Long id, String status);

    /**
     * 获取用户菜单
     */
    List<SysMenu> getUserMenus(Long userId);

    /**
     * 获取用户权限
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 获取当前用户菜单
     */
    List<SysMenuVO> getCurrentUserMenu();

    /**
     * 添加菜单
     */
    boolean add(SysMenuDTO dto);

    /**
     * 更新菜单
     */
    boolean update(SysMenuDTO dto);

    /**
     * 删除菜单
     */
    boolean delete(Long id);

    /**
     * 批量删除菜单
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 排序菜单
     */
    boolean sort(List<SysMenuDTO> dtos);

    /**
     * 获取选择树
     */
    List<SysMenuVO> getSelectTree();
}
