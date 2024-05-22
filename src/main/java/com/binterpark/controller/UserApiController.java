package com.binterpark.controller;

import com.binterpark.domain.User;
import com.binterpark.dto.JwtDto;
import com.binterpark.dto.UserLoginRequestDto;
import com.binterpark.dto.UserRegistrationDto;
import com.binterpark.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Target;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(Target.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        String userEmail = userLoginRequestDto.getEmail();
        String userPw = userLoginRequestDto.getPassword();
        logger.info("email : "+userEmail+", pw : "+userPw);

        try {
            JwtDto tokenInfo = userService.login(userEmail, userPw);
            logger.info("성공 , info : "+tokenInfo);
            return ResponseEntity.ok(tokenInfo);
        } catch (Exception e) {
            // 적절한 예외 처리를 통해 상태 코드와 메시지를 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " + e.getMessage());
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {

        User user = userService.registerUser(registrationDto);

        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "id") Long id) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            logger.info("Get userRole : "+user.getUserRole());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable(name = "id") Long id, @RequestBody Map<String, Object> updates) {

        User patchedUser = userService.patchUser(id, updates);

        if (patchedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(patchedUser, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
