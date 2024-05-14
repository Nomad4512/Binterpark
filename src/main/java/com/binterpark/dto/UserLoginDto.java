package com.binterpark.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto extends UserBaseDto{

    private String password;

    public UserLoginDto(String email, String password) {
        super.setEmail(email);
        this.password = password;
    }
}
