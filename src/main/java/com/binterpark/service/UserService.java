package com.binterpark.service;

import com.binterpark.common.UserRole;
import com.binterpark.domain.User;
import com.binterpark.dto.UserRegistrationDto;
import com.binterpark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    public User registerUser(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setUserEmail(registrationDto.getEmail());
        user.setUserName(registrationDto.getName());
        user.setUserPw(passwordEncoder.encode(registrationDto.getPassword()));
        user.setSignupDate(LocalDateTime.now());
        user.setUserRole(UserRole.USER);

        return userRepository.save(user);
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
