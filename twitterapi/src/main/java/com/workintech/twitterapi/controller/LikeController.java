package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.like.CreateLikeRequest;
import com.workintech.twitterapi.dto.like.LikeResponse;
import com.workintech.twitterapi.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
