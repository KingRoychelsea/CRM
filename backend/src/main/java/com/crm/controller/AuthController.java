package com.crm.controller;

import com.crm.common.Result;
import com.crm.dto.sys.SysUserDTO;
import com.crm.service.sys.SysUserService;
import com.crm.vo.sys.SysUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@Api(tags = "认证管理")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody SysUserDTO loginDTO) {
        return Result.success(sysUserService.login(loginDTO));
    }

    /**
     * 用户登出
     */
    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result<?> logout() {
        // 登出逻辑，实际项目中可能需要清除token等
        return Result.success();
    }

    /**
     * 获取用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<?> getInfo() {
        // 获取当前用户信息
        SysUserVO user = sysUserService.getCurrentUser();
        
        // 构建返回结果，包含 user、roles 和 permissions 字段
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("roles", new ArrayList<>()); // 暂时返回空列表
        result.put("permissions", new ArrayList<>()); // 暂时返回空列表
        
        return Result.success(result);
    }

    /**
     * 刷新token
     */
    @ApiOperation("刷新token")
    @PostMapping("/refresh")
    public Result<?> refreshToken() {
        // 刷新token逻辑
        return Result.success();
    }
}
