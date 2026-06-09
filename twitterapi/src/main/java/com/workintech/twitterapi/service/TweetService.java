package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.tweet.CreateTweetRequest;
import com.workintech.twitterapi.dto.tweet.TweetResponse;
import com.workintech.twitterapi.dto.tweet.UpdateTweetRequest;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.exception.ResourceNotFoundException;
import com.workintech.twitterapi.repository.ITweetRepository;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService {

    private final ITweetRepository tweetRepository;
    private final IUserRepository userRepository;

    @Autowired
    public TweetService(ITweetRepository tweetRepository, IUserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public List<TweetResponse> findAll() {
        return tweetRepository.findAll()
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
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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

        if (request.content() == null || request.content().isBlank()) {
            throw new BadRequestException("Invalid Tweet data!");
        }

        tweet.setContent(request.content());
        return toResponse(tweetRepository.save(tweet));
    }

    public TweetResponse delete(Long id) {
        Tweet tweet = findEntityById(id);
        tweetRepository.delete(tweet);
        return toResponse(tweet);
    }

    private TweetResponse toResponse(Tweet tweet) {
        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser() != null ? tweet.getUser().getId() : null
        );
    }
}