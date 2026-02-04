package com.crm.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tomcat配置类，添加全局CORS过滤器
 */
@Configuration
public class TomcatConfig {

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            // 这里可以添加Tomcat连接器的自定义配置
        });
        return factory;
    }

    /**
     * 注册全局CORS过滤器
     */
    @Bean
    public org.springframework.boot.web.servlet.FilterRegistrationBean<CorsFilter> corsFilter() {
        org.springframework.boot.web.servlet.FilterRegistrationBean<CorsFilter> registrationBean = new org.springframework.boot.web.servlet.FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }

    /**
     * 全局CORS过滤器
     */
    public static class CorsFilter implements javax.servlet.Filter {

        @Override
        public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
            javax.servlet.http.HttpServletRequest httpRequest = (javax.servlet.http.HttpServletRequest) request;
            javax.servlet.http.HttpServletResponse httpResponse = (javax.servlet.http.HttpServletResponse) response;

            // 设置CORS头
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Max-Age", "3600");

            // 如果是OPTIONS请求，直接返回200
            if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
                httpResponse.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);
                return;
            }

            // 继续过滤链
            chain.doFilter(request, response);
        }

        @Override
        public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
            // 初始化方法
        }

        @Override
        public void destroy() {
            // 销毁方法
        }
    }
}
