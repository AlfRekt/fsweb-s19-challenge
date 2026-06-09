package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.like.CreateLikeRequest;
import com.workintech.twitterapi.dto.like.LikeResponse;
import com.workintech.twitterapi.entity.Like;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.exception.ResourceNotFoundException;
import com.workintech.twitterapi.repository.ILikeRepository;
import com.workintech.twitterapi.repository.ITweetRepository;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final ILikeRepository likeRepository;
    private final IUserRepository userRepository;
    private final ITweetRepository tweetRepository;

    @Autowired
    public LikeService(ILikeRepository likeRepository, IUserRepository userRepository, ITweetRepository tweetRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    public LikeResponse like(CreateLikeRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tweet tweet = tweetRepository.findById(request.tweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));


        if(likeRepository.existsByUserIdAndTweetId(user.getId(),tweet.getId())){
            throw new BadRequestException("User already liked this tweet");
        }

        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);

        return toResponse(likeRepository.save(like));
    }

    public LikeResponse dislike(CreateLikeRequest request){
        Like like = likeRepository.findByUserIdAndTweetId(request.userId(),request.tweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Like not found"));
        likeRepository.delete(like);
        return toResponse(like);
    }

    private LikeResponse toResponse(Like like){
        return new LikeResponse(
                like.getId(),
                like.getUser() != null ? like.getUser().getId() : null,
                like.getTweet() != null ? like.getTweet().getId() : null
        );
    }
}
