package com.blog.bloggy.presigned.controller;


import com.blog.bloggy.aop.token.AccessTokenRequired;
import com.blog.bloggy.aop.token.dto.AccessTokenDto;
import com.blog.bloggy.presigned.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @AccessTokenRequired
    @GetMapping("/pre-signed")
    public String createPresignedURL(AccessTokenDto accessTokenDto){
        return s3Service.getGeneratePreSignedUrlRequest(accessTokenDto);
    }

}

