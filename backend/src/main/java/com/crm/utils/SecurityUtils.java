package com.crm.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 安全工具类
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户的用户名
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return "";
    }

    /**
     * 获取当前登录用户的用户名（兼容方法）
     */
    public static String getCurrentUserName() {
        return getUsername();
    }

    /**
     * 获取当前登录用户的部门ID
     */
    public static Long getCurrentUserDeptId() {
        // 模拟实现，实际应该从用户信息中获取
        return 1L;
    }

    /**
     * 获取当前登录用户的部门名称
     */
    public static String getCurrentUserDeptName() {
        // 模拟实现，实际应该从用户信息中获取
        return "技术部";
    }

    /**
     * 获取当前登录用户的ID
     */
    public static Long getCurrentUserId() {
        // 模拟实现，实际应该从用户信息中获取
        return 1L;
    }

    /**
     * 获取当前登录用户的认证信息
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 检查用户是否已认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String);
    }
}
