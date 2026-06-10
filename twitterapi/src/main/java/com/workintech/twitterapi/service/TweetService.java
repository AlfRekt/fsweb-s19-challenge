package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.retweet.CreateQuoteRequest;
import com.workintech.twitterapi.dto.tweet.CreateTweetRequest;
import com.workintech.twitterapi.dto.tweet.TweetResponse;
import com.workintech.twitterapi.dto.tweet.UpdateTweetRequest;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.exception.ResourceNotFoundException;
import com.workintech.twitterapi.exception.UnauthorizedActionException;
import com.workintech.twitterapi.repository.ITweetRepository;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService {

    private final ITweetRepository tweetRepository;
    private final IUserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Autowired
    public TweetService(ITweetRepository tweetRepository, IUserRepository userRepository,CurrentUserService currentUserService) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public List<TweetResponse> findAll() {
        return tweetRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Tweet findEntityById(Long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));
    }

    public TweetResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public List<TweetResponse> findByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return tweetRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TweetResponse save(CreateTweetRequest request) {
        User user = currentUserService.getCurrentUser();

        if (request.content() == null || request.content().isBlank()) {
            throw new BadRequestException("Invalid Tweet data!");
        }

        Tweet tweet = new Tweet();
        tweet.setContent(request.content());
        tweet.setUser(user);

        return toResponse(tweetRepository.save(tweet));
    }

    public TweetResponse update(Long tweetId, UpdateTweetRequest request) {
        Tweet tweet = findEntityById(tweetId);
        User currentUser = currentUserService.getCurrentUser();

        if (!tweet.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You can only update your own tweet");
        }

        if (request.content() == null || request.content().isBlank()) {
            throw new BadRequestException("Invalid Tweet data!");
        }

        tweet.setContent(request.content());
        return toResponse(tweetRepository.save(tweet));
    }

    public TweetResponse delete(Long id) {
        Tweet tweet = findEntityById(id);
        User currentUser = currentUserService.getCurrentUser();

        if (!tweet.getUser().getId().equals(currentUser.getId()) && !currentUserService.isAdmin(currentUser)) {
            throw new UnauthorizedActionException("You can only delete your own tweet");
        }

        tweetRepository.delete(tweet);
        return toResponse(tweet);
    }

    public TweetResponse quote(CreateQuoteRequest request) {
        User user = currentUserService.getCurrentUser();
        Tweet quoted = tweetRepository.findById(request.quotedTweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Quoted tweet not found"));

        if (request.content() == null || request.content().isBlank()) {
            throw new BadRequestException("Quote must include content");
        }

        Tweet quoteTweet = new Tweet();
        quoteTweet.setContent(request.content());
        quoteTweet.setUser(user);
        quoteTweet.setQuotedTweet(quoted);

        return toResponse(tweetRepository.save(quoteTweet));
    }

    private TweetResponse toResponse(Tweet tweet) {
        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser() != null ? tweet.getUser().getId() : null,
                tweet.getQuotedTweet() != null ? tweet.getQuotedTweet().getId() : null
        );
    }
}