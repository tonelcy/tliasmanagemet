package com.lcy.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//@WebFilter("/*")
@Slf4j
public class DemoFilter implements Filter {
    //初始化方法, web服务器启动, 创建Filter实例时调用, 只调用一次
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化过滤器: {}", filterConfig.getFilterName());
        Filter.super.init(filterConfig);
    }
    //拦截到请求时,调用该方法,可以调用多次
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("拦截到请求: {}", servletRequest.getServletContext());
        //放行
        filterChain.doFilter(servletRequest, servletResponse);
    }
    //销毁方法, web服务器关闭时调用, 只调用一次
    @Override
    public void destroy() {
        log.info("销毁过滤器...");
        Filter.super.destroy();
    }
}