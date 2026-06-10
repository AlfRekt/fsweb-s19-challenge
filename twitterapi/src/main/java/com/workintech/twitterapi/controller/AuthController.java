package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.user.CreateLoginRequest;
import com.workintech.twitterapi.dto.user.CreateUserRequest;
import com.workintech.twitterapi.dto.user.UserResponse;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody CreateUserRequest request){
        return authenticationService.register(request.userName(),request.email(),request.password());
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody CreateLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.userName(), request.password()));
        User user = (User) authentication.getPrincipal();
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
    }
}
