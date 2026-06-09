package com.workintech.twitterapi.dto.comment;

public record CreateCommentRequest(
        Long userId,
        Long tweetId,
        String content
) {}
