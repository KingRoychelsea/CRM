package com.crm.handler;

import com.crm.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 访问拒绝处理器
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 设置响应状态码
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");
        // 获取输出流
        PrintWriter out = response.getWriter();
        // 输出错误信息
        out.write(Result.error(403, "权限不足，请联系管理员").toString());
        // 刷新并关闭输出流
        out.flush();
        out.close();
    }
}
