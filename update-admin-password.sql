USE crm;

-- 重新更新admin用户密码为123456的BCrypt加密值
UPDATE sys_user SET password = '$2a$10$FGG2RcLpHKdsxNHldzoji.RjWTD5E..BClDPkcd8IXJWvD8QiKZXe' WHERE username = 'admin';

-- 验证更新结果
SELECT id, username, password FROM sys_user;
