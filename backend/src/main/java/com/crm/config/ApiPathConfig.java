package com.crm.config;

/**
 * API path configuration management
 * Unified management of all API endpoint paths to avoid path duplication and configuration confusion
 */
public class ApiPathConfig {
    
    /**
     * Base path prefix
     */
    public static final String API_BASE_PATH = "/api";
    
    /**
     * System management module path
     */
    public static final String SYS_BASE_PATH = API_BASE_PATH + "/sys";
    
    /**
     * User management path
     */
    public static final String USER_PATH = SYS_BASE_PATH + "/user";
    
    /**
     * Role management path
     */
    public static final String ROLE_PATH = SYS_BASE_PATH + "/role";
    
    /**
     * Department management path
     */
    public static final String DEPT_PATH = SYS_BASE_PATH + "/dept";
    
    /**
     * Menu management path
     */
    public static final String MENU_PATH = SYS_BASE_PATH + "/menu";
    
    /**
     * Dictionary management path
     */
    public static final String DICT_PATH = SYS_BASE_PATH + "/dict";
    
    /**
     * Log management path
     */
    public static final String LOG_PATH = SYS_BASE_PATH + "/log";
    
    /**
     * Authentication management path
     */
    public static final String AUTH_PATH = API_BASE_PATH + "/auth";
    
    /**
     * Customer management path
     */
    public static final String CUSTOMER_PATH = API_BASE_PATH + "/customer";
    
    /**
     * Sales management path
     */
    public static final String SALES_PATH = API_BASE_PATH + "/sales";
    
    /**
     * Statistics management path
     */
    public static final String STATS_PATH = API_BASE_PATH + "/stats";
    
    /**
     * Health check path
     */
    public static final String HEALTH_PATH = API_BASE_PATH + "/health";
    
    /**
     * Get full path
     */
    public static String getFullPath(String basePath, String subPath) {
        if (subPath == null || subPath.isEmpty()) {
            return basePath;
        }
        if (subPath.startsWith("/")) {
            return basePath + subPath;
        }
        return basePath + "/" + subPath;
    }
    
    /**
     * Get path with ID
     */
    public static String getPathWithId(String basePath, Long id) {
        return basePath + "/" + id;
    }
    
    /**
     * Get path with ID and sub path
     */
    public static String getPathWithIdAndSubPath(String basePath, Long id, String subPath) {
        return getFullPath(basePath + "/" + id, subPath);
    }
}