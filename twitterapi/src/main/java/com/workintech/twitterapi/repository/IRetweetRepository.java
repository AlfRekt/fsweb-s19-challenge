package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRetweetRepository extends JpaRepository<Retweet,Long> {
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
}
