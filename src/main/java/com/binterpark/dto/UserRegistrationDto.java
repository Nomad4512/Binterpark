package com.binterpark.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationDto extends UserBaseDto{

    private String name;
    private String password;
    private String confirmPassword;


    public UserRegistrationDto(String email, String password, String confirmPassword, String name) {
        super.setEmail(email);
        this.name = name;
        this.confirmPassword = confirmPassword;
        this.password = password;
    }

}
