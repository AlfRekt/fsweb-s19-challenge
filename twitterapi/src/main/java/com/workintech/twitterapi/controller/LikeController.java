package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.like.CreateLikeRequest;
import com.workintech.twitterapi.dto.like.LikeResponse;
import com.workintech.twitterapi.dto.tweet.TweetResponse;
import com.workintech.twitterapi.dto.user.UserResponse;
import com.workintech.twitterapi.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like")
    public LikeResponse like(@RequestBody CreateLikeRequest request){
        return likeService.like(request);
    }
    @PostMapping("/dislike")
    public LikeResponse dislike(@RequestBody CreateLikeRequest request){
        return likeService.dislike(request);
    }

    @GetMapping("/like/tweet/{tweetId}")
    public List<UserResponse> getUsersWhoLiked(@PathVariable Long tweetId) {
        return likeService.getUsersWhoLiked(tweetId);
    }

    @GetMapping("/like/user/{userId}")
    public List<TweetResponse> getLikedTweets(@PathVariable Long userId) {
        return likeService.getLikedTweets(userId);
    }
}
