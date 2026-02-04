package com.crm.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * SHA512密码编码器实现
 */
public class Sha512PasswordEncoder implements PasswordEncoder {
    
    private static final String ALGORITHM = "SHA-512";
    private static final int SALT_LENGTH = 32;
    private static final int ITERATIONS = 10000;
    
    @Override
    public String encode(CharSequence rawPassword) {
        // 生成随机盐值
        byte[] salt = generateSalt();
        // 加密密码
        byte[] hash = hashPassword(rawPassword.toString(), salt);
        // 组合盐值和哈希值
        byte[] combined = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(hash, 0, combined, salt.length, hash.length);
        // Base64编码
        return Base64.getEncoder().encodeToString(combined);
    }
    
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            // Base64解码
            byte[] combined = Base64.getDecoder().decode(encodedPassword);
            // 提取盐值
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            // 提取哈希值
            byte[] storedHash = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, SALT_LENGTH, storedHash, 0, storedHash.length);
            // 计算原始密码的哈希值
            byte[] computedHash = hashPassword(rawPassword.toString(), salt);
            // 比较哈希值
            return MessageDigest.isEqual(storedHash, computedHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 生成随机盐值
     */
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
    
    /**
     * 哈希密码
     */
    private byte[] hashPassword(String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.reset();
            digest.update(salt);
            byte[] input = digest.digest(password.getBytes());
            
            // 多次迭代增强安全性
            for (int i = 0; i < ITERATIONS; i++) {
                digest.reset();
                input = digest.digest(input);
            }
            
            return input;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 algorithm not available", e);
        }
    }
}