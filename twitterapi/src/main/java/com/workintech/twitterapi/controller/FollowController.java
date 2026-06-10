package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.follow.CreateFollowRequest;
import com.workintech.twitterapi.dto.follow.FollowResponse;
import com.workintech.twitterapi.dto.user.UserResponse;
import com.workintech.twitterapi.service.FollowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/")
    public FollowResponse follow(@RequestBody CreateFollowRequest request) {
        return followService.follow(request);
    }

    @DeleteMapping("/{followingId}")
    public FollowResponse unfollow(@PathVariable Long followingId) {
        return followService.unfollow(followingId);
    }

    @GetMapping("/following/{userId}")
    public List<UserResponse> getFollowing(@PathVariable Long userId) {
        return followService.getFollowing(userId);
    }

    @GetMapping("/followers/{userId}")
    public List<UserResponse> getFollowers(@PathVariable Long userId) {
        return followService.getFollowers(userId);
    }
}
