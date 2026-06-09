package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.user.CreateUserRequest;
import com.workintech.twitterapi.dto.user.UpdateUserRequest;
import com.workintech.twitterapi.dto.user.UserResponse;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<UserResponse> findAll(){
        return userService.findAll();
    }

    @PostMapping("/")
    public UserResponse save(@RequestBody CreateUserRequest request){
        return userService.save(request);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id,
                               @RequestBody UpdateUserRequest request){
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public UserResponse delete(@PathVariable Long id){
        return userService.delete(id);
    }
}
