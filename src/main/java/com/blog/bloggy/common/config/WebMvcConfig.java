package com.blog.bloggy.common.config;

import com.blog.bloggy.interceptor.AccessInterceptor;
import com.blog.bloggy.interceptor.RefreshInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AccessInterceptor accessInterceptor;
    private final RefreshInterceptor refreshInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor)
                .excludePathPatterns("/home","/users","/test",
                        "/login", "/error","/v1/auth/refresh");
        registry.addInterceptor(refreshInterceptor)
                .addPathPatterns("/v1/auth/refresh");
    }
}
