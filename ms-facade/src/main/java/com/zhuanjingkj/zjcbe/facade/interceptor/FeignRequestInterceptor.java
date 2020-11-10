package com.zhuanjingkj.zjcbe.facade.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;

import java.util.Map;

public class FeignRequestInterceptor implements RequestInterceptor {
    public FeignRequestInterceptor() {
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, String> attributes = RibbonFilterContextHolder.getCurrentContext().getAttributes();
        for (String key : attributes.keySet()) {
            String value = attributes.get(key);
            requestTemplate.header(key, value);
            System.out.println("FeignRequestInterceptor: " + key + "=" + value + "!");
        }
    }
}
