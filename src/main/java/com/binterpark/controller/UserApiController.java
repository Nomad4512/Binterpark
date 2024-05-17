package com.binterpark.controller;

import com.binterpark.domain.User;
import com.binterpark.dto.UserRegistrationDto;
import com.binterpark.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {

        User user = userService.registerUser(registrationDto);


        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
