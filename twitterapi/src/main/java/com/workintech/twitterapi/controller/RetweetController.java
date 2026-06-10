package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.retweet.CreateRetweetRequest;
import com.workintech.twitterapi.dto.retweet.RetweetResponse;
import com.workintech.twitterapi.service.RetweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/retweet/")
public class RetweetController {
    private final RetweetService retweetService;

    @Autowired
    public RetweetController(RetweetService retweetService) {
        this.retweetService = retweetService;
    }

    @GetMapping("/")
    public List<RetweetResponse> findAll() {
        return retweetService.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<RetweetResponse> findByUserId(@PathVariable Long userId) {
        return retweetService.findByUserId(userId);
    }

    @PostMapping()
    public RetweetResponse retweet(@RequestBody CreateRetweetRequest request){
        return retweetService.retweet(request);
    }

    @DeleteMapping("{id}")
    public RetweetResponse delete(@PathVariable Long id){
        return retweetService.delete(id);
    }
}
