package com.blog.bloggy.common.config;

import com.blog.bloggy.interceptor.AccessInterceptor;
import com.blog.bloggy.interceptor.RefreshInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AccessInterceptor accessInterceptor;
    private final RefreshInterceptor refreshInterceptor;
    //클린 빌드 후 다시 배포
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor)
                .excludePathPatterns("/users","/test","/home",
                        "/login", "/error","/v1/auth/refresh")
                .excludePathPatterns(String.valueOf(HttpMethod.GET), "/posts");

        registry.addInterceptor(refreshInterceptor)
                .addPathPatterns("/v1/auth/refresh");
    }
}
