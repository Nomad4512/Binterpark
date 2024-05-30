package com.binterpark.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/join")
    public String moveToSignupForm(){
        return "signupForm";
    }

    @GetMapping("/login")
    public String moveToLoginForm(){
        return "loginForm";
    }

    @GetMapping("/cart")
    public String moveToShoppingCart() {
        return "shoppingCart";
    }

    @GetMapping("/wishlist")
    public String moveToWishlist() {
        return "wishlist";
    }
}
