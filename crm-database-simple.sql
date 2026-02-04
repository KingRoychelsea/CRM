-- CRM System Database SQL Script
-- MySQL 8.0

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS crm DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use database
USE crm;

-- Drop tables if they exist
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;

-- 1. User table (sys_user)
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    dept_id BIGINT(20) DEFAULT NULL,
    email VARCHAR(100) DEFAULT '',
    phone VARCHAR(20) DEFAULT '',
    avatar VARCHAR(255) DEFAULT '',
    status CHAR(1) NOT NULL DEFAULT '1',
    create_by VARCHAR(64) NOT NULL DEFAULT '',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) NOT NULL DEFAULT '',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    remark VARCHAR(500) DEFAULT '',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB AUTO_INCREMENT=100;

-- 2. Role table (sys_role)
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    role_key VARCHAR(50) NOT NULL,
    order_num INT(11) NOT NULL DEFAULT 0,
    status CHAR(1) NOT NULL DEFAULT '1',
    create_by VARCHAR(64) NOT NULL DEFAULT '',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) NOT NULL DEFAULT '',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    remark VARCHAR(500) DEFAULT '',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_key (role_key)
) ENGINE=InnoDB AUTO_INCREMENT=100;

-- 3. User role relationship table (sys_user_role)
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    user_id BIGINT(20) NOT NULL,
    role_id BIGINT(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB AUTO_INCREMENT=100;

-- Insert initial data
-- 1. Roles
INSERT INTO sys_role (id, role_name, role_key, status, create_by) VALUES
(1, 'Admin', 'admin', '1', 'system'),
(2, 'User', 'user', '1', 'system');

-- 2. Users (password: 123456, BCrypt encrypted)
INSERT INTO sys_user (id, username, password, nickname, status, create_by) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'Administrator', '1', 'system'),
(2, 'user', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'Normal User', '1', 'system');

-- 3. User role relationships
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2);

-- Commit transaction
COMMIT;

-- Show result
SELECT 'CRM system database created successfully!' AS result;