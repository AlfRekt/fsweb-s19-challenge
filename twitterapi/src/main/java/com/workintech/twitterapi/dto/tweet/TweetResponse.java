package com.workintech.twitterapi.dto.tweet;

public record TweetResponse(
        Long id,
        String content,
        Long userId
) {}
