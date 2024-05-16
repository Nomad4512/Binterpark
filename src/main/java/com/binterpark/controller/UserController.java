package com.binterpark.controller;

import com.binterpark.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/join")
    public String moveToSignupForm(){
        return "signupForm";
    }

    @GetMapping("/login")
    public String moveToLoginForm(){
        return "login";
    }
}
