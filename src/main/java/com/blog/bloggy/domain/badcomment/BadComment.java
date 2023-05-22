package com.blog.bloggy.domain.badcomment;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Data
public class BadComment {
    @Id
    @GeneratedValue
    @Column(name = "bad_comment_id")
    private Long id;

    private String body;
}
