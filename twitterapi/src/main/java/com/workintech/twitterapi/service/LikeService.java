package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.like.CreateLikeRequest;
import com.workintech.twitterapi.dto.like.LikeResponse;
import com.workintech.twitterapi.dto.tweet.TweetResponse;
import com.workintech.twitterapi.dto.user.UserResponse;
import com.workintech.twitterapi.entity.Like;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.exception.ResourceNotFoundException;
import com.workintech.twitterapi.exception.UnauthorizedActionException;
import com.workintech.twitterapi.repository.ILikeRepository;
import com.workintech.twitterapi.repository.ITweetRepository;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    private final ILikeRepository likeRepository;
    private final CurrentUserService currentUserService;
    private final ITweetRepository tweetRepository;
    private final IUserRepository userRepository;

    @Autowired
    public LikeService(ILikeRepository likeRepository, CurrentUserService currentUserService, ITweetRepository tweetRepository,IUserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.currentUserService = currentUserService;
        this.tweetRepository = tweetRepository;
        this.userRepository =userRepository;
    }

    public LikeResponse like(CreateLikeRequest request){
        User user = currentUserService.getCurrentUser();

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
        User user = currentUserService.getCurrentUser();
        Like like = likeRepository.findByUserIdAndTweetId(user.getId(),request.tweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Like not found"));
        likeRepository.delete(like);
        return toResponse(like);
    }

    public List<UserResponse> getUsersWhoLiked(Long tweetId) {
        tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        return likeRepository.findByTweet_Id(tweetId)
                .stream()
                .map(like -> toUserResponse(like.getUser()))
                .toList();
    }

    public List<TweetResponse> getLikedTweets(Long userId) {
        User currentUser = currentUserService.getCurrentUser();
        if (!userId.equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You can only view your own likes");
        }

        return likeRepository.findByUser_Id(userId)
                .stream()
                .map(like -> toTweetResponse(like.getTweet()))
                .toList();
    }

    private TweetResponse toTweetResponse(Tweet tweet) {
        return new TweetResponse(tweet.getId(), tweet.getContent(), tweet.getUser().getId(),null);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
    }

    private LikeResponse toResponse(Like like){
        return new LikeResponse(
                like.getId(),
                like.getUser() != null ? like.getUser().getId() : null,
                like.getTweet() != null ? like.getTweet().getId() : null
        );
    }
}
