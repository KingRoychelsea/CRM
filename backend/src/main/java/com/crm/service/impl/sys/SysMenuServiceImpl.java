package com.crm.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.sys.SysMenu;
import com.crm.entity.sys.SysRoleMenu;
import com.crm.entity.sys.SysUserRole;
import com.crm.mapper.sys.SysMenuMapper;
import com.crm.mapper.sys.SysRoleMenuMapper;
import com.crm.mapper.sys.SysUserRoleMapper;
import com.crm.service.sys.SysMenuService;
import com.crm.dto.sys.SysMenuDTO;
import com.crm.vo.sys.SysMenuVO;
import com.crm.exception.BusinessException;
import com.crm.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<SysMenuVO> getMenuList(SysMenuDTO dto) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        if (dto.getMenuName() != null) {
            wrapper.like(SysMenu::getMenuName, dto.getMenuName());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(SysMenu::getStatus, dto.getStatus());
        }
        if (dto.getMenuType() != null) {
            wrapper.eq(SysMenu::getMenuType, dto.getMenuType());
        }
        wrapper.eq(SysMenu::getDelFlag, "0");
        wrapper.orderByAsc(SysMenu::getParentId).orderByAsc(SysMenu::getOrderNum);

        List<SysMenu> menuList = menuMapper.selectList(wrapper);
        return buildMenuTree(menuList);
    }

    @Override
    public List<SysMenuVO> getMenuTree() {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getDelFlag, "0");
        wrapper.eq(SysMenu::getStatus, "1");
        wrapper.orderByAsc(SysMenu::getParentId).orderByAsc(SysMenu::getOrderNum);

        List<SysMenu> menuList = menuMapper.selectList(wrapper);
        return buildMenuTree(menuList);
    }

    @Transactional
    @Override
    public boolean createMenu(SysMenuDTO dto) {
        // 检查菜单名称是否已存在
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getMenuName, dto.getMenuName());
        wrapper.eq(SysMenu::getParentId, dto.getParentId());
        wrapper.eq(SysMenu::getDelFlag, "0");
        SysMenu existingMenu = menuMapper.selectOne(wrapper);
        if (existingMenu != null) {
            throw new BusinessException("菜单名称已存在");
        }

        // 创建菜单
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(dto, menu);
        menu.setDelFlag("0");
        menu.setCreateBy(SecurityUtils.getUsername());
        menu.setCreateTime(new Date());
        menu.setUpdateBy(SecurityUtils.getUsername());
        menu.setUpdateTime(new Date());

        return save(menu);
    }

    @Transactional
    @Override
    public boolean updateMenu(SysMenuDTO dto) {
        SysMenu menu = getById(dto.getId());
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }

        // 检查菜单名称是否已存在
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getMenuName, dto.getMenuName());
        wrapper.eq(SysMenu::getParentId, dto.getParentId());
        wrapper.ne(SysMenu::getId, dto.getId());
        wrapper.eq(SysMenu::getDelFlag, "0");
        SysMenu existingMenu = menuMapper.selectOne(wrapper);
        if (existingMenu != null) {
            throw new BusinessException("菜单名称已存在");
        }

        // 更新菜单
        BeanUtils.copyProperties(dto, menu);
        menu.setUpdateBy(SecurityUtils.getUsername());
        menu.setUpdateTime(new Date());

        return updateById(menu);
    }

    @Transactional
    @Override
    public boolean deleteMenu(Long id) {
        // 检查是否有子菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        wrapper.eq(SysMenu::getDelFlag, "0");
        long childCount = menuMapper.selectCount(wrapper);
        if (childCount > 0) {
            throw new BusinessException("存在子菜单，无法删除");
        }

        // 检查是否有角色关联
        LambdaQueryWrapper<SysRoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.eq(SysRoleMenu::getMenuId, id);
        long roleMenuCount = roleMenuMapper.selectCount(roleMenuWrapper);
        if (roleMenuCount > 0) {
            throw new BusinessException("菜单已被角色使用，无法删除");
        }

        SysMenu menu = getById(id);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }

        // 逻辑删除菜单
        menu.setDelFlag("1");
        menu.setUpdateBy(SecurityUtils.getUsername());
        menu.setUpdateTime(new Date());

        return updateById(menu);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        SysMenu menu = getById(id);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }

        // 更新状态
        menu.setStatus(status);
        menu.setUpdateBy(SecurityUtils.getUsername());
        menu.setUpdateTime(new Date());

        return updateById(menu);
    }

    @Override
    public List<SysMenu> getUserMenus(Long userId) {
        // 获取用户角色
        LambdaQueryWrapper<SysUserRole> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);

        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取角色菜单
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        LambdaQueryWrapper<SysRoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.in(SysRoleMenu::getRoleId, roleIds);
        List<SysRoleMenu> roleMenus = roleMenuMapper.selectList(roleMenuWrapper);

        if (roleMenus.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取菜单
        List<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<SysMenu> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.in(SysMenu::getId, menuIds);
        menuWrapper.eq(SysMenu::getDelFlag, "0");
        menuWrapper.eq(SysMenu::getStatus, "1");
        menuWrapper.orderByAsc(SysMenu::getParentId).orderByAsc(SysMenu::getOrderNum);

        return menuMapper.selectList(menuWrapper);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        List<SysMenu> menus = getUserMenus(userId);
        return menus.stream()
                .filter(menu -> menu.getPerms() != null && !menu.getPerms().isEmpty())
                .map(SysMenu::getPerms)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<SysMenuVO> getCurrentUserMenu() {
        Long userId = SecurityUtils.getCurrentUserId();
        List<SysMenu> menus = getUserMenus(userId);
        return buildMenuTree(menus);
    }

    @Override
    public boolean add(SysMenuDTO dto) {
        return createMenu(dto);
    }

    @Override
    public boolean update(SysMenuDTO dto) {
        return updateMenu(dto);
    }

    @Override
    public boolean delete(Long id) {
        return deleteMenu(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        for (Long id : ids) {
            deleteMenu(id);
        }
        return true;
    }

    @Override
    public boolean sort(List<SysMenuDTO> dtos) {
        for (SysMenuDTO dto : dtos) {
            SysMenu menu = getById(dto.getId());
            if (menu != null) {
                menu.setOrderNum(dto.getOrderNum());
                menu.setUpdateBy(SecurityUtils.getUsername());
                menu.setUpdateTime(new Date());
                updateById(menu);
            }
        }
        return true;
    }

    @Override
    public List<SysMenuVO> getSelectTree() {
        return getMenuTree();
    }

    /**
     * 构建菜单树
     */
    private List<SysMenuVO> buildMenuTree(List<SysMenu> menuList) {
        List<SysMenuVO> voList = new ArrayList<>();
        List<SysMenuVO> parentList = new ArrayList<>();

        // 转换为VO
        for (SysMenu menu : menuList) {
            SysMenuVO vo = new SysMenuVO();
            BeanUtils.copyProperties(menu, vo);
            vo.setChildren(new ArrayList<>());
            voList.add(vo);
            if (menu.getParentId() == 0) {
                parentList.add(vo);
            }
        }

        // 构建树
        for (SysMenuVO parent : parentList) {
            buildChildTree(parent, voList);
        }

        return parentList;
    }

    /**
     * 递归构建子菜单树
     */
    private void buildChildTree(SysMenuVO parent, List<SysMenuVO> allMenus) {
        for (SysMenuVO menu : allMenus) {
            if (menu.getParentId().equals(parent.getId())) {
                parent.getChildren().add(menu);
                buildChildTree(menu, allMenus);
            }
        }
    }
}
