package com.crm.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.sys.SysRole;
import com.crm.entity.sys.SysRoleMenu;
import com.crm.entity.sys.SysUser;
import com.crm.entity.sys.SysUserRole;
import com.crm.mapper.sys.SysRoleMapper;
import com.crm.mapper.sys.SysRoleMenuMapper;
import com.crm.mapper.sys.SysUserMapper;
import com.crm.mapper.sys.SysUserRoleMapper;
import com.crm.service.sys.SysRoleService;
import com.crm.dto.sys.SysRoleDTO;
import com.crm.vo.sys.SysRoleVO;
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
 * 角色服务实现类
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public List<SysRoleVO> getRoleList(SysRoleDTO dto) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (dto.getRoleName() != null) {
            wrapper.like(SysRole::getRoleName, dto.getRoleName());
        }
        if (dto.getRoleKey() != null) {
            wrapper.like(SysRole::getRoleKey, dto.getRoleKey());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(SysRole::getStatus, dto.getStatus());
        }
        wrapper.eq(SysRole::getDelFlag, "0");
        wrapper.orderByAsc(SysRole::getOrderNum);

        List<SysRole> roleList = roleMapper.selectList(wrapper);
        List<SysRoleVO> voList = new ArrayList<>();

        for (SysRole role : roleList) {
            SysRoleVO vo = new SysRoleVO();
            BeanUtils.copyProperties(role, vo);
            // 获取角色菜单
            List<Long> menuIds = getRoleMenus(role.getId());
            vo.setMenuIds(menuIds);
            // 获取用户数量
            LambdaQueryWrapper<SysUserRole> userRoleWrapper = new LambdaQueryWrapper<>();
            userRoleWrapper.eq(SysUserRole::getRoleId, role.getId());
            long userCount = userRoleMapper.selectCount(userRoleWrapper);
            vo.setUserCount((int) userCount);
            voList.add(vo);
        }

        return voList;
    }

    @Transactional
    @Override
    public boolean createRole(SysRoleDTO dto) {
        // 检查角色关键字是否已存在
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleKey, dto.getRoleKey());
        wrapper.eq(SysRole::getDelFlag, "0");
        SysRole existingRole = roleMapper.selectOne(wrapper);
        if (existingRole != null) {
            throw new BusinessException("角色关键字已存在");
        }

        // 创建角色
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        role.setDelFlag("0");
        role.setCreateBy(SecurityUtils.getUsername());
        role.setCreateTime(new Date());
        role.setUpdateBy(SecurityUtils.getUsername());
        role.setUpdateTime(new Date());

        boolean saved = save(role);
        if (saved && dto.getMenuIds() != null && !dto.getMenuIds().isEmpty()) {
            // 保存角色菜单关联
            List<SysRoleMenu> roleMenus = dto.getMenuIds().stream().map(menuId -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(role.getId());
                roleMenu.setMenuId(menuId);
                return roleMenu;
            }).collect(Collectors.toList());
            roleMenuMapper.insertBatch(roleMenus);
        }

        return saved;
    }

    @Transactional
    @Override
    public boolean updateRole(SysRoleDTO dto) {
        SysRole role = getById(dto.getId());
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 检查角色关键字是否已存在
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleKey, dto.getRoleKey());
        wrapper.ne(SysRole::getId, dto.getId());
        wrapper.eq(SysRole::getDelFlag, "0");
        SysRole existingRole = roleMapper.selectOne(wrapper);
        if (existingRole != null) {
            throw new BusinessException("角色关键字已存在");
        }

        // 更新角色
        BeanUtils.copyProperties(dto, role);
        role.setUpdateBy(SecurityUtils.getUsername());
        role.setUpdateTime(new Date());

        boolean updated = updateById(role);
        if (updated && dto.getMenuIds() != null) {
            // 删除原有角色菜单关联
            LambdaQueryWrapper<SysRoleMenu> menuWrapper = new LambdaQueryWrapper<>();
            menuWrapper.eq(SysRoleMenu::getRoleId, role.getId());
            roleMenuMapper.delete(menuWrapper);

            // 保存新角色菜单关联
            if (!dto.getMenuIds().isEmpty()) {
                List<SysRoleMenu> roleMenus = dto.getMenuIds().stream().map(menuId -> {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(role.getId());
                    roleMenu.setMenuId(menuId);
                    return roleMenu;
                }).collect(Collectors.toList());
                roleMenuMapper.insertBatch(roleMenus);
            }
        }

        return updated;
    }

    @Override
    public boolean deleteRole(Long id) {
        // 检查角色是否被使用
        LambdaQueryWrapper<SysUserRole> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(SysUserRole::getRoleId, id);
        long userCount = userRoleMapper.selectCount(userRoleWrapper);
        if (userCount > 0) {
            throw new BusinessException("角色已被使用，无法删除");
        }

        SysRole role = getById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 逻辑删除角色
        role.setDelFlag("1");
        role.setUpdateBy(SecurityUtils.getUsername());
        role.setUpdateTime(new Date());

        boolean deleted = updateById(role);
        if (deleted) {
            // 删除角色菜单关联
            LambdaQueryWrapper<SysRoleMenu> menuWrapper = new LambdaQueryWrapper<>();
            menuWrapper.eq(SysRoleMenu::getRoleId, id);
            roleMenuMapper.delete(menuWrapper);
        }

        return deleted;
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        SysRole role = getById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 更新状态
        role.setStatus(status);
        role.setUpdateBy(SecurityUtils.getUsername());
        role.setUpdateTime(new Date());

        return updateById(role);
    }

    @Override
    public List<Long> getRoleMenus(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> roleMenus = roleMenuMapper.selectList(wrapper);
        return roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean assignMenus(Long roleId, List<Long> menuIds) {
        // 检查角色是否存在
        SysRole role = getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 删除原有角色菜单关联
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);

        // 保存新角色菜单关联
        if (menuIds != null && !menuIds.isEmpty()) {
            List<SysRoleMenu> roleMenus = menuIds.stream().map(menuId -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                return roleMenu;
            }).collect(Collectors.toList());
            roleMenuMapper.insertBatch(roleMenus);
        }

        return true;
    }

    @Override
    public List<SysRole> getAllRoles() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getDelFlag, "0");
        wrapper.eq(SysRole::getStatus, "1");
        wrapper.orderByAsc(SysRole::getOrderNum);
        return roleMapper.selectList(wrapper);
    }

    @Override
    public boolean add(SysRoleDTO dto) {
        return createRole(dto);
    }

    @Override
    public boolean update(SysRoleDTO dto) {
        return updateRole(dto);
    }

    @Override
    public boolean delete(Long id) {
        return deleteRole(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        for (Long id : ids) {
            deleteRole(id);
        }
        return true;
    }

    @Override
    public List<SysRoleVO> getAll() {
        List<SysRole> roles = getAllRoles();
        return roles.stream().map(role -> {
            SysRoleVO vo = new SysRoleVO();
            BeanUtils.copyProperties(role, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<?> getRoleUsers(Long roleId, Integer pageNum, Integer pageSize, String keyword, String status) {
        LambdaQueryWrapper<SysUserRole> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(SysUserRole::getRoleId, roleId);
        List<SysUserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);
        
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Long> userIds = userRoles.stream().map(SysUserRole::getUserId).collect(Collectors.toList());
        
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUser::getId, userIds);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(SysUser::getUsername, keyword).or().like(SysUser::getNickname, keyword));
        }
        if (status != null) {
            wrapper.eq(SysUser::getStatus, status);
        }
        wrapper.eq(SysUser::getDelFlag, "0");
        wrapper.orderByAsc(SysUser::getId);
        
        return userMapper.selectList(wrapper);
    }

    @Override
    public boolean assignUsers(Long roleId, List<Long> userIds) {
        SysRole role = getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getRoleId, roleId);
        userRoleMapper.delete(wrapper);
        
        if (userIds != null && !userIds.isEmpty()) {
            List<SysUserRole> userRoles = userIds.stream().map(userId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toList());
            userRoleMapper.insertBatch(userRoles);
        }
        
        return true;
    }
}
