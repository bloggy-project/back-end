package com.blog.summer.dto.user;


import com.blog.summer.domain.BaseTimeEntity;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;


    private String encryptedPwd;

}
