package com.binterpark.service;

import com.binterpark.domain.User;
import com.binterpark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.annotation.Target;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(Target.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        return userRepository.findByUserEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username); // 사용자 찾기 실패
                    return new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.");
                });
    }

    // 해당하는 userdetails.User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(User user) {
        log.debug("Creating UserDetails for user: {}", user.getUserEmail());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserEmail())
                .password(user.getUserPw())
                .authorities(new SimpleGrantedAuthority(user.getUserRole()))
                .build();
    }
}
