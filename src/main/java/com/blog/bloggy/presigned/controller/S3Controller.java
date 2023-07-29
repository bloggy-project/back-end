package com.blog.bloggy.presigned.controller;


import com.blog.bloggy.aop.token.AccessTokenRequired;
import com.blog.bloggy.aop.token.dto.AccessTokenDto;
import com.blog.bloggy.post.dto.ResponsePost;
import com.blog.bloggy.presigned.dto.FileDto;
import com.blog.bloggy.presigned.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @AccessTokenRequired
    @PostMapping("/pre-signed")
    public ResponseEntity<String> createPresignedURL(AccessTokenDto accessTokenDto, FileDto fileDto){
        String response = s3Service.getGeneratePreSignedUrlRequest(accessTokenDto,fileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

