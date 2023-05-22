package com.blog.bloggy.service;

import com.blog.bloggy.domain.PostTag;
import com.blog.bloggy.domain.Tag;
import com.blog.bloggy.dto.PostTagStatus;
import com.blog.bloggy.repository.TagRepository;
import com.blog.bloggy.repository.postTag.PostTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class PostTagService {
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    @Scheduled(cron ="0 0/10 * * * ?")
    public void registerUpdateStatusPost(){
        //이 부분 나중에 쿼리 1개로 최적화 여지 있을듯
        List<String> tagNames = postTagRepository.findTagNamesByStatusUpdate();
        List<PostTag> postTags = postTagRepository.findAllStatusUpdate();
        for (String tagName : tagNames) {
            Tag tag = Tag.builder()
                    .name(tagName)
                    .build();
            tagRepository.save(tag);
            postTags.forEach((pt)->{
                if (pt.getTagName().equals(tag.getName())) {
                    pt.setTag(tag);
                    pt.setStatus(PostTagStatus.REGISTERED);
                }
            });
        }
        postTagRepository.saveAll(postTags);
    }
    @Scheduled(cron ="0 0/10 * * * ?")
    public void deleteDeleteStatusPost(){
        postTagRepository.deleteAllStatusDelete();
    }

}
