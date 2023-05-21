package com.blog.summer.domain;


import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Tag {

    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @OneToMany(mappedBy = "tag")
    private List<PostTag> postTags=new ArrayList<>();

    private String name;


    public Tag() {}

    @Builder
    public Tag(String name) {
        this.name = name;
    }

    public void addPostTag(PostTag postTag) {
        this.postTags.add(postTag);
    }

    public void removePostTag(PostTag postTag) {
        postTags.remove(postTag);
    }
}
