package com.blog.summer.domain.badcomment;


import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
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
