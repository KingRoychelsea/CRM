-- CRM System Database SQL Script
-- MySQL 8.0

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS crm DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use database
USE crm;

-- System management module table structure

-- 1. Department table (sys_dept)
CREATE TABLE IF NOT EXISTS sys_dept (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Department ID',
    dept_name VARCHAR(50) NOT NULL COMMENT 'Department name',
    parent_id BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'Parent department ID',
    order_num INT(11) NOT NULL DEFAULT 0 COMMENT 'Order number',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT 'Status',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'Creator',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'Updater',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT 'Logical delete flag',
    remark VARCHAR(500) DEFAULT '' COMMENT 'Remark',
    PRIMARY KEY (id),
    KEY idx_parent_id (parent_id),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='Department table';

-- 2. 角色表（sys_role）
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_key VARCHAR(50) NOT NULL COMMENT '角色关键字',
    order_num INT(11) NOT NULL DEFAULT 0 COMMENT '排序号',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_key (role_key),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='角色表';

-- 3. 用户表（sys_user）
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) NOT NULL COMMENT '昵称',
    dept_id BIGINT(20) DEFAULT NULL COMMENT '部门ID',
    email VARCHAR(100) DEFAULT '' COMMENT '邮箱',
    phone VARCHAR(20) DEFAULT '' COMMENT '手机号',
    avatar VARCHAR(255) DEFAULT '' COMMENT '头像',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_dept_id (dept_id),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='用户表';

-- 4. 菜单表（sys_menu）
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    parent_id BIGINT(20) NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    order_num INT(11) NOT NULL DEFAULT 0 COMMENT '排序号',
    path VARCHAR(200) DEFAULT '' COMMENT '路由路径',
    component VARCHAR(255) DEFAULT '' COMMENT '组件路径',
    query VARCHAR(255) DEFAULT '' COMMENT '路由参数',
    is_frame CHAR(1) NOT NULL DEFAULT '0' COMMENT '是否为外部链接',
    is_cache CHAR(1) NOT NULL DEFAULT '0' COMMENT '是否缓存',
    menu_type CHAR(1) NOT NULL COMMENT '菜单类型',
    visible CHAR(1) NOT NULL DEFAULT '1' COMMENT '菜单状态',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '菜单状态',
    perms VARCHAR(100) DEFAULT '' COMMENT '权限标识',
    icon VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_parent_id (parent_id),
    KEY idx_menu_type (menu_type),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='菜单表';

-- 5. 角色菜单关联表（sys_role_menu）
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    role_id BIGINT(20) NOT NULL COMMENT '角色ID',
    menu_id BIGINT(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_menu (role_id, menu_id),
    KEY idx_role_id (role_id),
    KEY idx_menu_id (menu_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='角色菜单关联表';

-- 6. 用户角色关联表（sys_user_role）
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    role_id BIGINT(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id),
    KEY idx_user_id (user_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='用户角色关联表';

-- 7. 操作日志表（sys_oper_log）
CREATE TABLE IF NOT EXISTS sys_oper_log (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    title VARCHAR(100) NOT NULL COMMENT '操作标题',
    business_type INT(2) NOT NULL DEFAULT 0 COMMENT '业务类型（0-其他，1-新增，2-修改，3-删除）',
    method VARCHAR(100) DEFAULT '' COMMENT '方法名称',
    request_method VARCHAR(10) DEFAULT '' COMMENT '请求方式',
    operator_type INT(1) NOT NULL DEFAULT 0 COMMENT '操作类型（0-其他，1-后台用户，2-手机端用户）',
    oper_name VARCHAR(50) DEFAULT '' COMMENT '操作人员',
    dept_name VARCHAR(50) DEFAULT '' COMMENT '部门名称',
    oper_url VARCHAR(255) DEFAULT '' COMMENT '请求URL',
    oper_ip VARCHAR(128) DEFAULT '' COMMENT '操作IP',
    oper_location VARCHAR(255) DEFAULT '' COMMENT '操作地点',
    oper_param VARCHAR(2000) DEFAULT '' COMMENT '请求参数',
    json_result VARCHAR(2000) DEFAULT '' COMMENT '返回参数',
    status INT(1) NOT NULL DEFAULT 1 COMMENT '操作状态（0-失败，1-成功）',
    error_msg VARCHAR(2000) DEFAULT '' COMMENT '错误消息',
    oper_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (id),
    KEY idx_oper_time (oper_time),
    KEY idx_business_type (business_type),
    KEY idx_status (status)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='操作日志表';

-- 8. 登录日志表（sys_login_log）
CREATE TABLE IF NOT EXISTS sys_login_log (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '登录状态（0-失败，1-成功）',
    ipaddr VARCHAR(128) DEFAULT '' COMMENT 'IP地址',
    login_location VARCHAR(255) DEFAULT '' COMMENT '登录地点',
    browser VARCHAR(50) DEFAULT '' COMMENT '浏览器类型',
    os VARCHAR(50) DEFAULT '' COMMENT '操作系统',
    msg VARCHAR(255) DEFAULT '' COMMENT '提示消息',
    login_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    PRIMARY KEY (id),
    KEY idx_login_time (login_time),
    KEY idx_username (username),
    KEY idx_status (status)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='登录日志表';

-- 9. 数据字典表（sys_dict）
CREATE TABLE IF NOT EXISTS sys_dict (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
    dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
    dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态（0-禁用，1-启用）',
    order_num INT(11) NOT NULL DEFAULT 0 COMMENT '排序号',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_dict_type (dict_type),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='数据字典表';

-- 10. 数据字典详情表（sys_dict_item）
CREATE TABLE IF NOT EXISTS sys_dict_item (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '字典项ID',
    dict_id BIGINT(20) NOT NULL COMMENT '字典ID',
    item_name VARCHAR(100) NOT NULL COMMENT '字典项名称',
    item_value VARCHAR(100) NOT NULL COMMENT '字典项值',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态（0-禁用，1-启用）',
    order_num INT(11) NOT NULL DEFAULT 0 COMMENT '排序号',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_dict_id (dict_id),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='数据字典详情表';

-- 客户管理模块表结构

-- 1. 客户表（crm_customer）
CREATE TABLE IF NOT EXISTS crm_customer (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '客户ID',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    customer_type VARCHAR(50) DEFAULT '' COMMENT '客户类型（潜在客户、意向客户、合作客户、流失客户）',
    industry VARCHAR(50) DEFAULT '' COMMENT '所属行业',
    address VARCHAR(255) DEFAULT '' COMMENT '地址',
    phone VARCHAR(20) DEFAULT '' COMMENT '电话',
    email VARCHAR(100) DEFAULT '' COMMENT '邮箱',
    website VARCHAR(255) DEFAULT '' COMMENT '网站',
    source VARCHAR(50) DEFAULT '' COMMENT '客户来源',
    owner_user_id BIGINT(20) DEFAULT NULL COMMENT '负责人ID',
    owner_user_name VARCHAR(50) DEFAULT '' COMMENT '负责人名称',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_customer_type (customer_type),
    KEY idx_owner_user_id (owner_user_id),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='客户表';

-- 2. 联系人表（crm_contact）
CREATE TABLE IF NOT EXISTS crm_contact (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '联系人ID',
    customer_id BIGINT(20) NOT NULL COMMENT '客户ID',
    contact_name VARCHAR(50) NOT NULL COMMENT '联系人姓名',
    position VARCHAR(50) DEFAULT '' COMMENT '职位',
    phone VARCHAR(20) DEFAULT '' COMMENT '手机号',
    email VARCHAR(100) DEFAULT '' COMMENT '邮箱',
    priority INT(1) DEFAULT 2 COMMENT '优先级（1-高，2-中，3-低）',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_customer_id (customer_id),
    KEY idx_priority (priority),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='联系人表';

-- 3. 客户跟进记录表（crm_follow_record）
CREATE TABLE IF NOT EXISTS crm_follow_record (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    customer_id BIGINT(20) NOT NULL COMMENT '客户ID',
    follow_content TEXT COMMENT '跟进内容',
    follow_way VARCHAR(50) DEFAULT '' COMMENT '跟进方式（电话、微信、面谈）',
    follow_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '跟进时间',
    next_follow_time DATETIME DEFAULT NULL COMMENT '下次跟进时间',
    follow_user_id BIGINT(20) DEFAULT NULL COMMENT '跟进人ID',
    follow_user_name VARCHAR(50) DEFAULT '' COMMENT '跟进人名称',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_customer_id (customer_id),
    KEY idx_follow_time (follow_time),
    KEY idx_next_follow_time (next_follow_time),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='客户跟进记录表';

-- 4. 客户流失表（crm_customer_loss）
CREATE TABLE IF NOT EXISTS crm_customer_loss (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '流失ID',
    customer_id BIGINT(20) NOT NULL COMMENT '客户ID',
    loss_reason TEXT COMMENT '流失原因',
    loss_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '流失时间',
    handle_status VARCHAR(50) DEFAULT '待处理' COMMENT '处理状态（待处理、已处理）',
    handle_remark TEXT COMMENT '处理备注',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_customer_id (customer_id),
    KEY idx_loss_time (loss_time),
    KEY idx_handle_status (handle_status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='客户流失表';

-- 销售管理模块表结构

-- 1. 商机表（crm_business）
CREATE TABLE IF NOT EXISTS crm_business (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商机ID',
    business_name VARCHAR(100) NOT NULL COMMENT '商机名称',
    customer_id BIGINT(20) NOT NULL COMMENT '客户ID',
    business_amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '商机金额',
    business_stage VARCHAR(50) DEFAULT '' COMMENT '商机阶段（初步接洽、需求确认、方案报价、合同签订）',
    expected_close_time DATETIME DEFAULT NULL COMMENT '预计成交时间',
    probability INT(3) DEFAULT 0 COMMENT '成交概率（0-100）',
    owner_user_id BIGINT(20) DEFAULT NULL COMMENT '负责人ID',
    owner_user_name VARCHAR(50) DEFAULT '' COMMENT '负责人名称',
    status VARCHAR(50) DEFAULT '进行中' COMMENT '状态（进行中、已关闭、已转化）',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_customer_id (customer_id),
    KEY idx_business_stage (business_stage),
    KEY idx_status (status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='商机表';

-- 2. 合同表（crm_contract）
CREATE TABLE IF NOT EXISTS crm_contract (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '合同ID',
    contract_name VARCHAR(100) NOT NULL COMMENT '合同名称',
    contract_no VARCHAR(50) NOT NULL COMMENT '合同编号',
    customer_id BIGINT(20) NOT NULL COMMENT '客户ID',
    business_id BIGINT(20) DEFAULT NULL COMMENT '商机ID',
    contract_amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '合同金额',
    start_time DATETIME DEFAULT NULL COMMENT '开始时间',
    end_time DATETIME DEFAULT NULL COMMENT '结束时间',
    contract_status VARCHAR(50) DEFAULT '草稿' COMMENT '合同状态（草稿、生效、终止、过期）',
    attachment VARCHAR(255) DEFAULT '' COMMENT '附件路径',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_contract_no (contract_no),
    KEY idx_customer_id (customer_id),
    KEY idx_business_id (business_id),
    KEY idx_contract_status (contract_status),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='合同表';

-- 3. 订单表（crm_order）
CREATE TABLE IF NOT EXISTS crm_order (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    contract_id BIGINT(20) NOT NULL COMMENT '合同ID',
    customer_id BIGINT(20) NOT NULL COMMENT '客户ID',
    order_amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '订单金额',
    order_status VARCHAR(50) DEFAULT '待付款' COMMENT '订单状态（待付款、已付款、待发货、已完成、已取消）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单时间',
    pay_time DATETIME DEFAULT NULL COMMENT '付款时间',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_contract_id (contract_id),
    KEY idx_customer_id (customer_id),
    KEY idx_order_status (order_status),
    KEY idx_create_time (create_time),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='订单表';

-- 4. 回款表（crm_payment）
CREATE TABLE IF NOT EXISTS crm_payment (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '回款ID',
    payment_no VARCHAR(50) NOT NULL COMMENT '回款编号',
    order_id BIGINT(20) NOT NULL COMMENT '订单ID',
    customer_id BIGINT(20) NOT NULL COMMENT '客户ID',
    payment_amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '回款金额',
    payment_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回款时间',
    payment_way VARCHAR(50) DEFAULT '' COMMENT '回款方式',
    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_payment_no (payment_no),
    KEY idx_order_id (order_id),
    KEY idx_customer_id (customer_id),
    KEY idx_payment_time (payment_time),
    KEY idx_del_flag (del_flag)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='回款表';

-- 插入基础测试数据

-- 1. 部门数据
INSERT INTO sys_dept (id, dept_name, parent_id, order_num, status, create_by, create_time) VALUES
(1, '总部', 0, 1, '1', 'admin', NOW()),
(2, '销售部', 1, 2, '1', 'admin', NOW()),
(3, '市场部', 1, 3, '1', 'admin', NOW()),
(4, '技术部', 1, 4, '1', 'admin', NOW());

-- 2. 角色数据
INSERT INTO sys_role (id, role_name, role_key, order_num, status, create_by, create_time) VALUES
(1, '管理员', 'admin', 1, '1', 'admin', NOW()),
(2, '普通用户', 'user', 2, '1', 'admin', NOW());

-- 3. 菜单数据
INSERT INTO sys_menu (id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
-- 系统管理
(1, '系统管理', 0, 1, 'system', 'Layout', '0', '0', 'M', '1', '1', '', 'system', 'admin', NOW()),
(2, '用户管理', 1, 1, 'user', 'system/user/index', '0', '0', 'C', '1', '1', 'system:user:list', 'user', 'admin', NOW()),
(3, '角色管理', 1, 2, 'role', 'system/role/index', '0', '0', 'C', '1', '1', 'system:role:list', 'role', 'admin', NOW()),
(4, '菜单管理', 1, 3, 'menu', 'system/menu/index', '0', '0', 'C', '1', '1', 'system:menu:list', 'menu', 'admin', NOW()),
(5, '部门管理', 1, 4, 'dept', 'system/dept/index', '0', '0', 'C', '1', '1', 'system:dept:list', 'dept', 'admin', NOW()),
(6, '操作日志', 1, 5, 'operlog', 'system/operlog/index', '0', '0', 'C', '1', '1', 'system:operlog:list', 'log', 'admin', NOW()),
(7, '登录日志', 1, 6, 'loginlog', 'system/loginlog/index', '0', '0', 'C', '1', '1', 'system:loginlog:list', 'log', 'admin', NOW()),
(8, '数据字典', 1, 7, 'dict', 'system/dict/index', '0', '0', 'C', '1', '1', 'system:dict:list', 'dict', 'admin', NOW()),

-- 客户管理
(9, '客户管理', 0, 2, 'customer', 'Layout', '0', '0', 'M', '1', '1', '', 'customer', 'admin', NOW()),
(10, '客户信息', 9, 1, 'customerInfo', 'customer/customer/index', '0', '0', 'C', '1', '1', 'customer:customer:list', 'customer', 'admin', NOW()),
(11, '联系人管理', 9, 2, 'contact', 'customer/contact/index', '0', '0', 'C', '1', '1', 'customer:contact:list', 'contact', 'admin', NOW()),
(12, '跟进记录', 9, 3, 'follow', 'customer/follow/index', '0', '0', 'C', '1', '1', 'customer:follow:list', 'follow', 'admin', NOW()),
(13, '客户流失', 9, 4, 'loss', 'customer/loss/index', '0', '0', 'C', '1', '1', 'customer:loss:list', 'loss', 'admin', NOW()),

-- 销售管理
(14, '销售管理', 0, 3, 'sales', 'Layout', '0', '0', 'M', '1', '1', '', 'sales', 'admin', NOW()),
(15, '商机管理', 14, 1, 'business', 'sales/business/index', '0', '0', 'C', '1', '1', 'sales:business:list', 'business', 'admin', NOW()),
(16, '合同管理', 14, 2, 'contract', 'sales/contract/index', '0', '0', 'C', '1', '1', 'sales:contract:list', 'contract', 'admin', NOW()),
(17, '订单管理', 14, 3, 'order', 'sales/order/index', '0', '0', 'C', '1', '1', 'sales:order:list', 'order', 'admin', NOW()),
(18, '回款管理', 14, 4, 'payment', 'sales/payment/index', '0', '0', 'C', '1', '1', 'sales:payment:list', 'payment', 'admin', NOW()),

-- 数据统计
(19, '数据统计', 0, 4, 'statistics', 'Layout', '0', '0', 'M', '1', '1', '', 'statistics', 'admin', NOW()),
(20, '客户统计', 19, 1, 'customerStats', 'statistics/customer/index', '0', '0', 'C', '1', '1', 'statistics:customer:list', 'customer', 'admin', NOW()),
(21, '销售统计', 19, 2, 'salesStats', 'statistics/sales/index', '0', '0', 'C', '1', '1', 'statistics:sales:list', 'sales', 'admin', NOW()),
(22, '商机统计', 19, 3, 'businessStats', 'statistics/business/index', '0', '0', 'C', '1', '1', 'statistics:business:list', 'business', 'admin', NOW());

-- 4. 用户数据（密码：123456，BCrypt加密）
INSERT INTO sys_user (id, username, password, nickname, dept_id, email, phone, status, create_by, create_time) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '管理员', 1, 'admin@example.com', '13800138000', '1', 'admin', NOW()),
(2, 'user', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '普通用户', 2, 'user@example.com', '13800138001', '1', 'admin', NOW());

-- 5. 角色菜单关联数据
-- 管理员角色关联所有菜单
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8),
(1, 9), (1, 10), (1, 11), (1, 12), (1, 13),
(1, 14), (1, 15), (1, 16), (1, 17), (1, 18),
(1, 19), (1, 20), (1, 21), (1, 22);

-- 普通用户角色关联部分菜单
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(2, 9), (2, 10), (2, 11), (2, 12), (2, 13),
(2, 14), (2, 15), (2, 16), (2, 17), (2, 18),
(2, 19), (2, 20), (2, 21), (2, 22);

-- 6. 用户角色关联数据
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1), -- 管理员关联管理员角色
(2, 2); -- 普通用户关联普通用户角色

-- 7. 数据字典数据
INSERT INTO sys_dict (id, dict_name, dict_type, status, order_num, create_by, create_time) VALUES
(1, '客户类型', 'customer_type', '1', 1, 'admin', NOW()),
(2, '商机阶段', 'business_stage', '1', 2, 'admin', NOW()),
(3, '合同状态', 'contract_status', '1', 3, 'admin', NOW()),
(4, '订单状态', 'order_status', '1', 4, 'admin', NOW()),
(5, '跟进方式', 'follow_way', '1', 5, 'admin', NOW());

-- 数据字典详情数据
INSERT INTO sys_dict_item (dict_id, item_name, item_value, status, order_num, create_by, create_time) VALUES
-- 客户类型
(1, '潜在客户', '潜在客户', '1', 1, 'admin', NOW()),
(1, '意向客户', '意向客户', '1', 2, 'admin', NOW()),
(1, '合作客户', '合作客户', '1', 3, 'admin', NOW()),
(1, '流失客户', '流失客户', '1', 4, 'admin', NOW()),
-- 商机阶段
(2, '初步接洽', '初步接洽', '1', 1, 'admin', NOW()),
(2, '需求确认', '需求确认', '1', 2, 'admin', NOW()),
(2, '方案报价', '方案报价', '1', 3, 'admin', NOW()),
(2, '合同签订', '合同签订', '1', 4, 'admin', NOW()),
-- 合同状态
(3, '草稿', '草稿', '1', 1, 'admin', NOW()),
(3, '生效', '生效', '1', 2, 'admin', NOW()),
(3, '终止', '终止', '1', 3, 'admin', NOW()),
(3, '过期', '过期', '1', 4, 'admin', NOW()),
-- 订单状态
(4, '待付款', '待付款', '1', 1, 'admin', NOW()),
(4, '已付款', '已付款', '1', 2, 'admin', NOW()),
(4, '待发货', '待发货', '1', 3, 'admin', NOW()),
(4, '已完成', '已完成', '1', 4, 'admin', NOW()),
(4, '已取消', '已取消', '1', 5, 'admin', NOW()),
-- 跟进方式
(5, '电话', '电话', '1', 1, 'admin', NOW()),
(5, '微信', '微信', '1', 2, 'admin', NOW()),
(5, '面谈', '面谈', '1', 3, 'admin', NOW());

-- 8. 客户测试数据
INSERT INTO crm_customer (id, customer_name, customer_type, industry, address, phone, email, source, owner_user_id, owner_user_name, create_by, create_time) VALUES
(1, '阿里巴巴集团', '合作客户', '互联网', '杭州市余杭区文一西路969号', '0571-88888888', 'contact@alibaba.com', '搜索引擎', 2, '普通用户', 'admin', NOW()),
(2, '腾讯科技', '合作客户', '互联网', '深圳市南山区科技园', '0755-88888888', 'contact@tencent.com', '朋友介绍', 2, '普通用户', 'admin', NOW()),
(3, '字节跳动', '意向客户', '互联网', '北京市海淀区知春路', '010-88888888', 'contact@bytedance.com', '社交媒体', 2, '普通用户', 'admin', NOW()),
(4, '美团点评', '潜在客户', 'O2O', '北京市朝阳区望京', '010-99999999', 'contact@meituan.com', '线下活动', 2, '普通用户', 'admin', NOW());

-- 9. 联系人测试数据
INSERT INTO crm_contact (customer_id, contact_name, position, phone, email, priority, create_by, create_time) VALUES
(1, '马云', '创始人', '13800138000', 'mayun@alibaba.com', 1, 'admin', NOW()),
(1, '张勇', 'CEO', '13800138001', 'zhangyong@alibaba.com', 1, 'admin', NOW()),
(2, '马化腾', 'CEO', '13800138002', 'mahuateng@tencent.com', 1, 'admin', NOW()),
(3, '张一鸣', 'CEO', '13800138003', 'zhangyiming@bytedance.com', 1, 'admin', NOW());

-- 10. 客户跟进记录测试数据
INSERT INTO crm_follow_record (customer_id, follow_content, follow_way, follow_time, next_follow_time, follow_user_id, follow_user_name, create_by, create_time) VALUES
(1, '与客户沟通合作事宜，客户表示满意', '电话', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 2, '普通用户', 'admin', NOW()),
(2, '拜访客户，讨论新项目合作', '面谈', NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY), 2, '普通用户', 'admin', NOW()),
(3, '发送产品方案，等待客户反馈', '微信', NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY), 2, '普通用户', 'admin', NOW());

-- 11. 商机测试数据
INSERT INTO crm_business (business_name, customer_id, business_amount, business_stage, expected_close_time, probability, owner_user_id, owner_user_name, status, create_by, create_time) VALUES
('阿里云服务合作', 1, 1000000.00, '合同签订', DATE_ADD(NOW(), INTERVAL 30 DAY), 90, 2, '普通用户', '进行中', 'admin', NOW()),
('腾讯云服务合作', 2, 800000.00, '方案报价', DATE_ADD(NOW(), INTERVAL 60 DAY), 70, 2, '普通用户', '进行中', 'admin', NOW()),
('抖音广告投放', 3, 500000.00, '需求确认', DATE_ADD(NOW(), INTERVAL 90 DAY), 50, 2, '普通用户', '进行中', 'admin', NOW());

-- 12. 合同测试数据
INSERT INTO crm_contract (contract_name, contract_no, customer_id, business_id, contract_amount, start_time, end_time, contract_status, create_by, create_time) VALUES
('阿里云服务合同', 'HT2024001', 1, 1, 1000000.00, NOW(), DATE_ADD(NOW(), INTERVAL 365 DAY), '生效', 'admin', NOW()),
('腾讯云服务合同', 'HT2024002', 2, 2, 800000.00, NOW(), DATE_ADD(NOW(), INTERVAL 365 DAY), '草稿', 'admin', NOW());

-- 13. 订单测试数据
INSERT INTO crm_order (order_no, contract_id, customer_id, order_amount, order_status, create_time, create_by) VALUES
('DD2024001', 1, 1, 500000.00, '已付款', NOW(), 'admin'),
('DD2024002', 1, 1, 500000.00, '待付款', NOW(), 'admin'),
('DD2024003', 2, 2, 800000.00, '待付款', NOW(), 'admin');

-- 14. 回款测试数据
INSERT INTO crm_payment (payment_no, order_id, customer_id, payment_amount, payment_time, payment_way, create_by, create_time) VALUES
('HK2024001', 1, 1, 500000.00, NOW(), '银行转账', 'admin', NOW());

-- 提交事务
COMMIT;

-- 显示创建结果
SELECT 'CRM系统数据库创建完成！' AS result;
