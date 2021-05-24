package com.akgarg.jwtauthenticationimplementation.controller;

import com.akgarg.jwtauthenticationimplementation.config.CustomUserDetailsService;
import com.akgarg.jwtauthenticationimplementation.helper.JWTHelperUtil;
import com.akgarg.jwtauthenticationimplementation.model.JwtRequest;
import com.akgarg.jwtauthenticationimplementation.model.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JWTController {

    private final CustomUserDetailsService customUserDetailsService;
    private final JWTHelperUtil jwtHelperUtil;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public JWTController(CustomUserDetailsService customUserDetailsService, JWTHelperUtil jwtHelperUtil, AuthenticationManager authenticationManager) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtHelperUtil = jwtHelperUtil;
        this.authenticationManager = authenticationManager;
    }


    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest request) throws Exception {
        System.out.println(request);

        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception("Username or password is invalid");
        }

        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtHelperUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }
}
