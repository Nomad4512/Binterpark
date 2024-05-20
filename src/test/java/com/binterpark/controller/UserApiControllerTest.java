package com.binterpark.controller;

import com.binterpark.domain.User;
import com.binterpark.dto.UserRegistrationDto;
import com.binterpark.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserApiControllerTest {

    @Autowired
    private UserApiController userApiController;

    @MockBean
    private UserService userService;

    @AfterEach
    public void tearDown() {
        Mockito.reset(userService);
    }

    /*
    @Test
    public void 회원가입성공() {
        // given
        UserRegistrationDto registrationDto = new UserRegistrationDto("test@example.com", "password", "password", "tester");
        User user = new User(registrationDto.getEmail(), registrationDto.getPassword(), registrationDto.getName());

        // mock
        Mockito.when(userService.registerUser(registrationDto)).thenReturn(user);

        // when
        ResponseEntity<?> responseEntity = userApiController.registerUser(registrationDto);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(user);

    }*/

    @Test
    public void 회원정보조회실패() {
        // given
        Long id = 1L;

        // mock
        Mockito.when(userService.findById(id)).thenReturn(Optional.empty());

        // when
        ResponseEntity<?> responseEntity = userApiController.getUser(id);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void 회원정보수정() {
        // given
        Long id = 1L;
        Map<String, Object> updates = new HashMap<>();
        String pw = "비밀번호당";
        updates.put("password", pw);

        User expectedUser = new User();
        expectedUser.setUserId(id);
        expectedUser.setUserEmail("test@example.com");
        expectedUser.setUserPw(pw);
        expectedUser.setUserName("Tester");

        // Mock
        Mockito.when(userService.patchUser(id, updates)).thenReturn(expectedUser);

        // when
        ResponseEntity<User> responseEntity = userApiController.patchUser(id,updates);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedUser);

    }

    @Test
    public void 회원삭제성공() {
        // given
        Long id = 1L;

        // mock
        Mockito.when(userService.deleteUser(id)).thenReturn(true);

        // when
        ResponseEntity<?> responseEntity = userApiController.deleteUser(id);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void 회원삭제실패() {
        // given
        Long id = 3L;

        // mock
        Mockito.when(userService.deleteUser(id)).thenReturn(false);

        // when
        ResponseEntity<?> responseEntity = userApiController.deleteUser(id);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}