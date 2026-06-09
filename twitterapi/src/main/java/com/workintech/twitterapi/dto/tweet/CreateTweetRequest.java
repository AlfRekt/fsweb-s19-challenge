package com.workintech.twitterapi.dto.tweet;

public record CreateTweetRequest(
        Long userId,
        String content
) {}