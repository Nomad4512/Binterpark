package com.binterpark.controller;

import com.binterpark.domain.User;
import com.binterpark.dto.JwtDto;
import com.binterpark.dto.UserLoginRequestDto;
import com.binterpark.exception.UserNotFoundException;
import com.binterpark.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserApiControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserApiController userApiController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userApiController)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // UTF-8 필터 추가
                .build();
    }

    @Test
    public void testLogin_Success() throws Exception {
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto("test@example.com", "password");
        JwtDto jwtDto = new JwtDto("Bearer", "accessToken", "refreshToken");

        when(userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword())).thenReturn(jwtDto);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grantType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    public void testLogin_Failure() throws Exception {
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto("test@example.com", "password");

        when(userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword())).thenThrow(new RuntimeException("Authentication failed"));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("로그인 실패: Authentication failed"));
    }

    @Test
    public void testGetUser_Success() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUserEmail("test@example.com");
        user.setUserName("Test User");
        user.setSignupDate(LocalDateTime.now());
        user.setUserRole("USER");

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"))
                .andExpect(jsonPath("$.userName").value("Test User"))
                .andExpect(jsonPath("$.userRole").value("USER"));
    }

    @Test
    public void testGetUser_NotFound() throws Exception {
        Long userId = 1L;

        when(userService.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("사용자를 찾을 수 없습니다."));
    }

    @Test
    public void testPatchUser_Success() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUserEmail("test@example.com");
        user.setUserName("Old Name");
        user.setUserPw("oldPassword");
        user.setSignupDate(LocalDateTime.now());
        user.setUserRole("USER");

        User updatedUser = new User();
        updatedUser.setUserId(userId);
        updatedUser.setUserEmail("test@example.com");
        updatedUser.setUserName("New Name");
        updatedUser.setUserPw("encodedNewPassword");
        updatedUser.setSignupDate(LocalDateTime.now());
        updatedUser.setUserRole("USER");

        when(userService.patchUser(eq(userId), anyMap())).thenReturn(updatedUser);

        mockMvc.perform(patch("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Name\",\"password\":\"newPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"))
                .andExpect(jsonPath("$.userName").value("New Name"))
                .andExpect(jsonPath("$.userRole").value("USER"));
    }

    @Test
    public void testPatchUser_UserNotFound() throws Exception {
        Long userId = 1L;

        when(userService.patchUser(eq(userId), anyMap())).thenThrow(new UserNotFoundException("사용자를 찾을 수 없습니다."));

        mockMvc.perform(patch("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Name\",\"password\":\"newPassword\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("사용자를 찾을 수 없습니다."));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("회원탈퇴가 완료되었습니다."));
    }

    @Test
    public void testDeleteUser_UserNotFound() throws Exception {
        Long userId = 1L;

        doThrow(new UserNotFoundException("사용자를 찾을 수 없습니다.")).when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("사용자를 찾을 수 없습니다."));
    }
}
