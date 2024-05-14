package com.binterpark.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/join")
    public String moveToSignupForm(){
        //model.addAttribute("user",new UserRegistrationDto());
        return "signupForm";
    }

    @GetMapping("/login")
    public String moveToLonginForm(){
        return "login";
    }
}
