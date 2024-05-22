package com.binterpark.service;

import com.binterpark.exception.PasswordsNotEqualException;
import com.binterpark.exception.UserAlreadyExistException;
import com.binterpark.exception.UserNotFoundException;
import com.binterpark.common.UserRole;
import com.binterpark.domain.User;
import com.binterpark.dto.JwtDto;
import com.binterpark.dto.UserRegistrationDto;
import com.binterpark.jwt.JwtTokenProvider;
import com.binterpark.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {

        if (closeable!=null) {
            closeable.close();
        }
    }

    // =============================== 로그인 테스트 ===============================
    @Test
    public void testLogin_Success() {
        String userEmail = "test@example.com";
        String userPw = "password";

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, userPw);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(new JwtDto("Bearer", "accessToken", "refreshToken"));

        JwtDto jwtDto = userService.login(userEmail, userPw);

        assertNotNull(jwtDto);
        assertEquals("Bearer", jwtDto.getGrantType());
        assertEquals("accessToken", jwtDto.getAccessToken());
        assertEquals("refreshToken", jwtDto.getRefreshToken());
    }

    @Test
    public void testLogin_Failure() {
        String userEmail = "test@example.com";
        String userPw = "password";

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, userPw);

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(authenticationToken)).thenThrow(new RuntimeException("Authentication failed"));

        assertThrows(RuntimeException.class, () -> userService.login(userEmail, userPw));
    }

    // =============================== 회원 CRUD 테스트 ===============================

    @Test
    public void testRegisterUser_Success() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("test@example.com");
        registrationDto.setName("Test User");
        registrationDto.setPassword("password");
        registrationDto.setConfirmPassword("password");

        when(userRepository.findByUserEmail(registrationDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setUserEmail(registrationDto.getEmail());
        savedUser.setUserName(registrationDto.getName());
        savedUser.setUserPw("encodedPassword");
        savedUser.setSignupDate(LocalDateTime.now());
        savedUser.setUserRole(UserRole.USER.getRoleName());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(registrationDto);

        assertNotNull(result);
        assertEquals("test@example.com", result.getUserEmail());
        assertEquals("Test User", result.getUserName());
        assertEquals("encodedPassword", result.getUserPw());
        assertEquals(UserRole.USER.getRoleName(), result.getUserRole());
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("test@example.com");
        registrationDto.setName("Test User");
        registrationDto.setPassword("password");
        registrationDto.setConfirmPassword("password");

        User existingUser = new User();
        existingUser.setUserEmail("test@example.com");

        when(userRepository.findByUserEmail(registrationDto.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistException.class, () -> userService.registerUser(registrationDto));
    }

    @Test
    public void testRegisterUser_PasswordsNotEqual() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("test@example.com");
        registrationDto.setName("Test User");
        registrationDto.setPassword("password");
        registrationDto.setConfirmPassword("differentPassword");

        assertThrows(PasswordsNotEqualException.class, () -> userService.registerUser(registrationDto));
    }

    @Test
    public void testFindById_Success() {
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUserEmail("test@example.com");
        user.setUserName("Test User");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(userId);

        assertTrue(foundUser.isPresent());
        assertEquals(userId, foundUser.get().getUserId());
        assertEquals("test@example.com", foundUser.get().getUserEmail());
        assertEquals("Test User", foundUser.get().getUserName());
    }

    @Test
    public void testFindById_NotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findById(userId);

        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testPatchUser_Success() {
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUserEmail("test@example.com");
        user.setUserName("Old Name");
        user.setUserPw("oldPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(user)).thenReturn(user);

        Map<String, Object> updates = Map.of("password", "newPassword", "name", "New Name");

        User updatedUser = userService.patchUser(userId, updates);

        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getUserName());
        assertEquals("encodedNewPassword", updatedUser.getUserPw());
    }

    @Test
    public void testPatchUser_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Map<String, Object> updates = Map.of("password", "newPassword", "name", "New Name");

        assertThrows(UserNotFoundException.class, () -> userService.patchUser(userId, updates));
    }

    @Test
    public void testDeleteUser_Success() {
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUserEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> userService.deleteUser(userId));
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }

}