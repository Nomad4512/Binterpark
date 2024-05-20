package com.binterpark.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequestDto extends UserBaseDto{

    private String password;

    public UserLoginRequestDto(String email, String password) {
        super.setEmail(email);
        this.password = password;
    }
}
