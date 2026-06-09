package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.tweet.CreateTweetRequest;
import com.workintech.twitterapi.dto.tweet.TweetResponse;
import com.workintech.twitterapi.dto.tweet.UpdateTweetRequest;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.service.TweetService;
import com.workintech.twitterapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping("/")
    public List<TweetResponse> findAll(){
        return tweetService.findAll();
    }

    @GetMapping("/findById/{id}")
    public TweetResponse findById(@PathVariable Long id){
        return tweetService.findById(id);
    }

    @GetMapping("/findByUserId/{id}")
    public List<TweetResponse> findByUserId(@PathVariable Long id){
        return tweetService.findByUserId(id);
    }

    @PostMapping("/")
    public TweetResponse save(@RequestBody CreateTweetRequest request){
        return tweetService.save(request);
    }

    @PutMapping("/{id}")
    public TweetResponse update(@PathVariable Long id,
                                @RequestBody UpdateTweetRequest request){
        return tweetService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public TweetResponse delete(@PathVariable Long id){
        return tweetService.delete(id);
    }
}
