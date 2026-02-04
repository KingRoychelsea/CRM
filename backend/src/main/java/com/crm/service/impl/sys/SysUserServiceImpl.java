package com.crm.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.sys.SysUser;
import com.crm.entity.sys.SysUserRole;
import com.crm.mapper.sys.SysUserMapper;
import com.crm.mapper.sys.SysUserRoleMapper;
import com.crm.service.sys.SysUserService;
import com.crm.dto.sys.SysUserDTO;
import com.crm.vo.sys.SysUserVO;
import com.crm.exception.BusinessException;
import com.crm.utils.JwtUtils;
import com.crm.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService, UserDetailsService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Object login(SysUserDTO loginDTO) {
        log.info("Login attempt for user: {}", loginDTO.getUsername());
        
        // 根据用户名查询用户
        SysUser user = getByUsername(loginDTO.getUsername());
        log.info("User found: {}", user != null);
        if (user == null) {
            log.error("User not found: {}", loginDTO.getUsername());
            throw new BusinessException("用户名或密码错误");
        }

        // 验证用户状态
        log.info("User status: {}", user.getStatus());
        if (!"1".equals(user.getStatus())) {
            log.error("User disabled: {}", loginDTO.getUsername());
            throw new BusinessException("用户已被禁用");
        }

        // 验证密码
        log.info("Validating password for user: {}", loginDTO.getUsername());
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.error("Password validation failed for user: {}", loginDTO.getUsername());
            throw new BusinessException("用户名或密码错误");
        }

        // 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        String token = jwtUtils.generateToken(user.getUsername(), claims);
        log.info("Token generated successfully for user: {}", loginDTO.getUsername());

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", getUserInfo(user));

        return result;
    }

    @Override
    public SysUserVO getCurrentUser() {
        String username = SecurityUtils.getUsername();
        SysUser user = getByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return getUserInfo(user);
    }

    @Override
    public Object list(Integer pageNum, Integer pageSize, String username, String realName, Long deptId, Integer status) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        
        if (username != null) {
            wrapper.like(SysUser::getUsername, username);
        }
        if (realName != null) {
            wrapper.like(SysUser::getNickname, realName);
        }
        if (deptId != null) {
            wrapper.eq(SysUser::getDeptId, deptId);
        }
        if (status != null) {
            wrapper.eq(SysUser::getStatus, status);
        }
        wrapper.eq(SysUser::getDelFlag, "0");
        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> userPage = userMapper.selectPage(page, wrapper);
        List<SysUserVO> voList = userPage.getRecords().stream().map(this::getUserInfo).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("total", userPage.getTotal());
        result.put("records", voList);
        return result;
    }

    @Override
    public SysUserVO getById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return getUserInfo(user);
    }

    @Transactional
    @Override
    public void add(SysUserDTO userDTO) {
        createUser(userDTO);
    }

    @Transactional
    @Override
    public void update(SysUserDTO userDTO) {
        updateUser(userDTO);
    }

    @Override
    public void delete(Long id) {
        deleteUser(id);
    }

    @Transactional
    @Override
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            deleteUser(id);
        }
    }

    @Override
    public void resetPassword(Long id) {
        resetPassword(id, "123456");
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 更新状态
        user.setStatus(status.toString());
        user.setUpdateBy(SecurityUtils.getUsername());
        user.setUpdateTime(new Date());

        updateById(user);
    }

    @Override
    public List<Long> getUserRoles(Long id) {
        return getRoles(id);
    }

    @Transactional
    @Override
    public void assignRoles(Long id, List<Long> roleIds) {
        // 删除原有角色关联
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, id);
        userRoleMapper.delete(wrapper);

        // 保存新角色关联
        if (!roleIds.isEmpty()) {
            List<SysUserRole> userRoles = roleIds.stream().map(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(id);
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toList());
            userRoleMapper.insertBatch(userRoles);
        }
    }

    @Override
    public SysUser getByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        wrapper.eq(SysUser::getDelFlag, "0");
        return userMapper.selectOne(wrapper);
    }

    @Override
    public List<String> getPermissions(Long userId) {
        // 为admin用户默认添加所有系统管理和客户管理权限
        SysUser user = userMapper.selectById(userId);
        if (user != null && "admin".equals(user.getUsername())) {
            List<String> permissions = new ArrayList<>();
            // 系统管理权限
            permissions.add("sys:user:list");
            permissions.add("sys:user:query");
            permissions.add("sys:user:add");
            permissions.add("sys:user:edit");
            permissions.add("sys:user:delete");
            permissions.add("sys:role:list");
            permissions.add("sys:role:query");
            permissions.add("sys:role:add");
            permissions.add("sys:role:edit");
            permissions.add("sys:role:delete");
            permissions.add("sys:dept:list");
            permissions.add("sys:dept:query");
            permissions.add("sys:dept:add");
            permissions.add("sys:dept:edit");
            permissions.add("sys:dept:delete");
            permissions.add("sys:menu:list");
            permissions.add("sys:menu:query");
            permissions.add("sys:menu:add");
            permissions.add("sys:menu:edit");
            permissions.add("sys:menu:delete");
            permissions.add("sys:dict:list");
            permissions.add("sys:dict:query");
            permissions.add("sys:dict:add");
            permissions.add("sys:dict:edit");
            permissions.add("sys:dict:delete");
            permissions.add("sys:log:list");
            permissions.add("sys:log:query");
            // 客户管理权限
            permissions.add("customer:list");
            permissions.add("customer:query");
            permissions.add("customer:add");
            permissions.add("customer:edit");
            permissions.add("customer:delete");
            return permissions;
        }
        // TODO: 实现获取用户权限的逻辑
        return new ArrayList<>();
    }

    @Override
    public List<Long> getRoles(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = userRoleMapper.selectList(wrapper);
        return userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户
        SysUser user = getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 验证用户状态
        if (!"1".equals(user.getStatus())) {
            throw new UsernameNotFoundException("用户已被禁用");
        }

        // 为admin用户添加所有权限
        if ("admin".equals(username)) {
            // 构建UserDetails对象，添加所有系统管理和客户管理权限
            return User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(
                        // 系统管理权限
                        "sys:user:list", "sys:user:query", "sys:user:add", "sys:user:edit", "sys:user:delete",
                        "sys:role:list", "sys:role:query", "sys:role:add", "sys:role:edit", "sys:role:delete",
                        "sys:dept:list", "sys:dept:query", "sys:dept:add", "sys:dept:edit", "sys:dept:delete",
                        "sys:menu:list", "sys:menu:query", "sys:menu:add", "sys:menu:edit", "sys:menu:delete",
                        "sys:dict:list", "sys:dict:query", "sys:dict:add", "sys:dict:edit", "sys:dict:delete",
                        "sys:log:list", "sys:log:query",
                        // 客户管理权限
                        "customer:list", "customer:query", "customer:add", "customer:edit", "customer:delete"
                    )
                    .build();
        } else {
            // 构建普通用户的UserDetails对象
            return User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles()
                    .build();
        }
    }

    // 辅助方法：获取用户信息
    private SysUserVO getUserInfo(SysUser user) {
        SysUserVO vo = new SysUserVO();
        BeanUtils.copyProperties(user, vo);
        // 获取用户角色
        List<Long> roleIds = getRoles(user.getId());
        vo.setRoleIds(roleIds);
        return vo;
    }

    // 原有方法实现
    public boolean createUser(SysUserDTO dto) {
        // 检查用户名是否已存在
        SysUser existingUser = getByUsername(dto.getUsername());
        if (existingUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 创建用户
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(passwordEncoder.encode("123456")); // 默认密码
        user.setDelFlag("0");
        user.setCreateBy(SecurityUtils.getUsername());
        user.setCreateTime(new Date());
        user.setUpdateBy(SecurityUtils.getUsername());
        user.setUpdateTime(new Date());

        boolean saved = save(user);
        if (saved && dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            // 保存用户角色关联
            List<SysUserRole> userRoles = dto.getRoleIds().stream().map(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toList());
            userRoleMapper.insertBatch(userRoles);
        }

        return saved;
    }

    public boolean updateUser(SysUserDTO dto) {
        SysUser user = userMapper.selectById(dto.getId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 更新用户信息
        BeanUtils.copyProperties(dto, user);
        user.setUpdateBy(SecurityUtils.getUsername());
        user.setUpdateTime(new Date());

        boolean updated = updateById(user);
        if (updated && dto.getRoleIds() != null) {
            // 删除原有角色关联
            LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUserRole::getUserId, user.getId());
            userRoleMapper.delete(wrapper);

            // 保存新角色关联
            if (!dto.getRoleIds().isEmpty()) {
                List<SysUserRole> userRoles = dto.getRoleIds().stream().map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(user.getId());
                    userRole.setRoleId(roleId);
                    return userRole;
                }).collect(Collectors.toList());
                userRoleMapper.insertBatch(userRoles);
            }
        }

        return updated;
    }

    public boolean deleteUser(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 逻辑删除用户
        user.setDelFlag("1");
        user.setUpdateBy(SecurityUtils.getUsername());
        user.setUpdateTime(new Date());

        return updateById(user);
    }

    public boolean resetPassword(Long id, String password) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 重置密码
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdateBy(SecurityUtils.getUsername());
        user.setUpdateTime(new Date());

        return updateById(user);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        SysUser user = getByUsername(SecurityUtils.getUsername());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        // 修改密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateBy(SecurityUtils.getUsername());
        user.setUpdateTime(new Date());

        return updateById(user);
    }

    public List<SysUserVO> getUserList(SysUserDTO dto) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (dto.getUsername() != null) {
            wrapper.like(SysUser::getUsername, dto.getUsername());
        }
        if (dto.getNickname() != null) {
            wrapper.like(SysUser::getNickname, dto.getNickname());
        }
        if (dto.getDeptId() != null) {
            wrapper.eq(SysUser::getDeptId, dto.getDeptId());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, dto.getStatus());
        }
        wrapper.eq(SysUser::getDelFlag, "0");
        wrapper.orderByDesc(SysUser::getCreateTime);

        List<SysUser> userList = userMapper.selectList(wrapper);
        return userList.stream().map(this::getUserInfo).collect(Collectors.toList());
    }
}
