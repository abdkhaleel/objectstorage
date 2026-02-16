package com.khaleel.objectstorage.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/register")
    public void register(){
        //todo
    }

    @PostMapping("/login")
    public void login(){
        //todo
    }
}
