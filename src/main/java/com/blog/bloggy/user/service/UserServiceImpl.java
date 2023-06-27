package com.blog.bloggy.user.service;

import com.blog.bloggy.common.exception.UserNotFoundException;
import com.blog.bloggy.user.dto.TestMaskingDto;
import com.blog.bloggy.user.dto.TokenUserDto;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.dto.UserDto;
import com.blog.bloggy.token.repository.TokenRepository;
import com.blog.bloggy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException(username));

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }
    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = UserEntity.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .userId(userDto.getUserId())
                .build();
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException());

        if(userEntity==null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() ->  new UsernameNotFoundException(email));
        if(userEntity ==null)
            throw new UsernameNotFoundException(email);

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public TokenUserDto getTokenUserDtoByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException());

        TokenUserDto userDto = TokenUserDto.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .build();
        return userDto;
    }

    @Override
    public TestMaskingDto getTestMaskingDtoByUserId(String userId){
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException());
        TestMaskingDto userDto=TestMaskingDto.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .build();
        return userDto;
    }
}
