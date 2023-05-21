package com.blog.summer.dto.post;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RequestPostRegister {

    @NotNull(message = "Title cannot be null")
    @Size(min=2, message = "Title not be less than 2 characters")
    private String title;

    @NotNull(message = "content cannot be null")
    @Size(min=2, message = "content not be less than 2 characters")
    private String content;


    private String categoryName;

    private List<String> tagNames;

}
