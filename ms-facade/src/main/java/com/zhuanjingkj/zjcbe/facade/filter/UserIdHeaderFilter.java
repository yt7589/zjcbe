package com.zhuanjingkj.zjcbe.facade.filter;

import com.zhuanjingkj.zjcbe.common.AppConst;
import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;
import io.netty.util.internal.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserIdHeaderFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        String userId = httpRequest.getParameter(AppConst.ZULL_AUTH_HEADER);
        if (!StringUtil.isNullOrEmpty(userId)) {
            RibbonFilterContextHolder.getCurrentContext().add(AppConst.ZULL_AUTH_HEADER, userId);
            System.out.println("ms-facade: get userIdHeaderFilter: uid=" + userId + "!");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
