package com.workintech.twitterapi.dto.retweet;

public record CreateQuoteRequest(Long quotedTweetId,
                                 String content) {
}
