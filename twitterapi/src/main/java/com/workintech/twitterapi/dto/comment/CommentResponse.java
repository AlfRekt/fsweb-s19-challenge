package com.workintech.twitterapi.dto.comment;

public record CommentResponse(
        Long id,
        String content,
        Long userId,
        Long tweetId
) {}
