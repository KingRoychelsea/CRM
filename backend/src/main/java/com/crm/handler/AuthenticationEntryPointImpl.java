package com.crm.handler;

import com.crm.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证入口点
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 设置响应状态码
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");
        // 获取输出流
        PrintWriter out = response.getWriter();
        // 输出错误信息
        out.write(Result.error(401, "未认证，请先登录").toString());
        // 刷新并关闭输出流
        out.flush();
        out.close();
    }
}
