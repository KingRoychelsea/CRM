USE crm;

-- 更新用户密码为123456的BCrypt加密值
UPDATE sys_user SET password = '$2a$10$FGG2RcLpHKdsxNHldzoji.RjWTD5E..BClDPkcd8IXJWvD8QiKZXe' WHERE username = 'admin';
UPDATE sys_user SET password = '$2a$10$FGG2RcLpHKdsxNHldzoji.RjWTD5E..BClDPkcd8IXJWvD8QiKZXe' WHERE username = 'user';

-- 验证更新结果
SELECT id, username, LENGTH(password) AS password_length FROM sys_user;
