package com.binterpark.dto;

import com.binterpark.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long userId;
    private final String userEmail;
    private final String userName;
    private final String userRole;

    public UserResponseDto (User user) {
        this.userId = user.getUserId();
        this.userEmail = user.getUserEmail();
        this.userName = user.getUserName();
        this.userRole = user.getUserRole();
    }
}
