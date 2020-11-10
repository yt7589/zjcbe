package com.zhuanjingkj.zjcbe.facade.conf;

import com.zhuanjingkj.zjcbe.facade.filter.UserIdHeaderFilter;
import com.zhuanjingkj.zjcbe.facade.interceptor.FeignRequestInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FeignBasicAuthConfig {
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        UserIdHeaderFilter userIdHeaderFilter = new UserIdHeaderFilter();
        bean.setFilter(userIdHeaderFilter);
        List<String> urlPaterns = new ArrayList<>(1);
        urlPaterns.add("/*");
        bean.setUrlPatterns(urlPaterns);
        return bean;
    }
}
