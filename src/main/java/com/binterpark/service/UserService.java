package com.binterpark.service;

import com.binterpark.common.UserRole;
import com.binterpark.domain.User;
import com.binterpark.dto.JwtDto;
import com.binterpark.dto.UserRegistrationDto;
import com.binterpark.jwt.JwtTokenProvider;
import com.binterpark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    Logger logger = LoggerFactory.getLogger(Target.class);

    // 로그인
    @Transactional
    public JwtDto login (String userEmail, String userPw) {
        // 1. Email/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(userEmail, userPw);
        logger.info("authenticationToken : "+authenticationToken);


        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            log.debug("Authentication successful for user: {}", userEmail); // 인증 성공 로그

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            return jwtTokenProvider.generateToken(authentication);
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", userEmail, e); // 인증 실패 로그
            throw e;
        }
    }

    // 회원가입
    @Transactional
    public User registerUser(UserRegistrationDto registrationDto) {
        User user = new User();
        logger.info("default UserRole : "+user.getRoles());
        user.setUserEmail(registrationDto.getEmail());
        user.setUserName(registrationDto.getName());
        user.setUserPw(passwordEncoder.encode(registrationDto.getPassword()));
        user.setSignupDate(LocalDateTime.now());
        user.setRoles(new ArrayList<>(Arrays.asList(UserRole.USER)));
        logger.info("set UserRole : "+user.getRoles());

        User savedUser = userRepository.save(user);
        logger.info("saved User : "+savedUser.getRoles());

        Optional<User> userFromDb = userRepository.findById(savedUser.getUserId());
        userFromDb.ifPresent(u -> logger.info("userRole from DB : "+u.getRoles()));

        return savedUser;
    }

    // 회원정보 가져오기
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 회원정보수정
    public User patchUser(Long id, Map<String, Object> updates) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            if (updates.containsKey("password")) {
                u.setUserPw(passwordEncoder.encode((String) updates.get("password")));
            }
            if (updates.containsKey("name")) {
                u.setUserName((String) updates.get("name"));
            }
            return userRepository.save(u);
        }
        return null;
    }

    // 회원삭제
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
        return false;
    }


}
