package com.blog.bloggy.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUser {

    private String email;

    private String thumbnail;

    private String blogName;

    private String description;
}
