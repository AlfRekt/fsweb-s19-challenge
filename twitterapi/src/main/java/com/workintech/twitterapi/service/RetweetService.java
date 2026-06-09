package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.retweet.CreateRetweetRequest;
import com.workintech.twitterapi.dto.retweet.RetweetResponse;
import com.workintech.twitterapi.entity.Retweet;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.exception.ResourceNotFoundException;
import com.workintech.twitterapi.repository.IRetweetRepository;
import com.workintech.twitterapi.repository.ITweetRepository;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class RetweetService {
    private final IRetweetRepository retweetRepository;
    private final IUserRepository userRepository;
    private final ITweetRepository tweetRepository;

    public RetweetService(IRetweetRepository retweetRepository,
                          IUserRepository userRepository,
                          ITweetRepository tweetRepository) {
        this.retweetRepository = retweetRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    public RetweetResponse retweet(CreateRetweetRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        Tweet tweet =tweetRepository.findById(request.tweetId())
                .orElseThrow(()-> new ResourceNotFoundException("Tweet not found"));

        if(retweetRepository.existsByUserIdAndTweetId(request.userId(), request.tweetId())){
            throw new BadRequestException("User already retweeted this tweet");
        }

        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet);

        return toResponse(retweetRepository.save(retweet));
    }

    public RetweetResponse delete(Long id){
        Retweet retweet = retweetRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Retweet not found"));

        retweetRepository.delete(retweet);
        return toResponse(retweet);
    }

    private RetweetResponse toResponse(Retweet retweet){
        return new RetweetResponse(
                retweet.getId(),
                retweet.getUser().getId() != null ? retweet.getUser().getId() : null,
                retweet.getTweet().getId() != null ? retweet.getTweet().getId() : null
        );
    }
}
