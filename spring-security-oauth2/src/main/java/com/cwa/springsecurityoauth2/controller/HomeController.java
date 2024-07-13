package com.cwa.springsecurityoauth2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to home page";
    }

    @GetMapping("/secured")
    public String secured() {
        return "Welcome to secured page";
    }
}
