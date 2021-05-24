package com.akgarg.jwtauthenticationimplementation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HomeController {

    @RequestMapping("/home")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("This is the main page of the JWT authentication API.<br> " +
                "This page is protected by the JWT.");
    }

}
