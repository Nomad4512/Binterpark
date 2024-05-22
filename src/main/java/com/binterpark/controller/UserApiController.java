package com.binterpark.controller;

import com.binterpark.exception.PasswordsNotEqualException;
import com.binterpark.exception.UserAlreadyExistException;
import com.binterpark.exception.UserNotFoundException;
import com.binterpark.domain.User;
import com.binterpark.dto.JwtDto;
import com.binterpark.dto.UserLoginRequestDto;
import com.binterpark.dto.UserRegistrationDto;
import com.binterpark.dto.UserResponseDto;
import com.binterpark.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Target;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " + e.getMessage());
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            User user = userService.registerUser(registrationDto);
            return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (PasswordsNotEqualException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "id") Long id) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            UserResponseDto userResponseDto = new UserResponseDto(user);
            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser(@PathVariable(name = "id") Long id, @RequestBody Map<String, Object> updates) {

        try {
            User updatedUser = userService.patchUser(id, updates);
            UserResponseDto userResponseDto = new UserResponseDto(updatedUser);
            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long id) {

        try{
            userService.deleteUser(id);
            return new ResponseEntity<>("회원탈퇴가 완료되었습니다.",HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
