package com.blog.bloggy.common.config;

import com.blog.bloggy.interceptor.AccessInterceptor;
import com.blog.bloggy.interceptor.RefreshInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AccessInterceptor accessInterceptor;
    private final RefreshInterceptor refreshInterceptor;
    //클린 빌드 후 다시 배포

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /* Interceptor 토큰 관리,AOP 관리로 변경
        registry.addInterceptor(accessInterceptor)
                .excludePathPatterns("/users","/test","/home",
                        "/login", "/error","/v1/auth/refresh","/posts");
         */
        /*
        registry.addInterceptor(refreshInterceptor)
                .addPathPatterns("/v1/auth/refresh");
        */
    }
    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 프론트 엔드 도메인을 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더를 허용
                .allowCredentials(true); // 인증 정보를 사용할 경우 true로 설정
        registry.addMapping("/**")
                .allowedOrigins("http://boggy.kro.kr") // 프론트 엔드 도메인을 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더를 허용
                .allowCredentials(true); // 인증 정보를 사용할 경우 true로 설정

    }

     */
}
