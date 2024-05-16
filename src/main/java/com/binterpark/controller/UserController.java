package com.binterpark.controller;

import com.binterpark.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/join")
    public String moveToSignupForm(Model model){
        model.addAttribute("user",new UserRegistrationDto());
        return "signupForm";
    }

    @GetMapping("/login")
    public String moveToLonginForm(){
        return "login";
    }
}
