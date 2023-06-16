package com.blog.bloggy.favorite.repository;

import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.post.repository.PostRepository;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FavoriteRepositoryCustomTest {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void getPostsFavoritesUsernames() {
        String userId1="abcd123";
        String userId2="efgh456";
        List<String> names=new ArrayList<>();

        UserEntity user1 = getUserEntity(userId1);
        UserEntity user2 = getUserEntity(userId2);
        userRepository.save(user1);
        userRepository.save(user2);
        names.add(user1.getName());
        names.add(user2.getName());
        Post post = Post.builder()
                .build();
        postRepository.save(post);
        Favorite favorite1=Favorite.builder()
                .favoritePost(post)
                .favoriteUser(user1)
                .build();
        Favorite favorite2=Favorite.builder()
                .favoritePost(post)
                .favoriteUser(user2)
                .build();
        favoriteRepository.save(favorite1);
        favoriteRepository.save(favorite2);
        post.addFavorite(favorite1);
        post.addFavorite(favorite2);
        List<String> usernames = favoriteRepository.getPostsFavoritesUsernames(post.getId());
        for (String username : usernames) {
            System.out.println("username = " + username);
        }
        assertEquals(names,usernames);
    }
    private static UserEntity getUserEntity(String userId) {
        UserEntity user = UserEntity.builder()
                .email(userId + "@naver.com")
                .name("gon"+userId)
                .userId(userId)
                .build();
        return user;
    }
}