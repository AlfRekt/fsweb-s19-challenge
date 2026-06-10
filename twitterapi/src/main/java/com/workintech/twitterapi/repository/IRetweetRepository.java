package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRetweetRepository extends JpaRepository<Retweet,Long> {
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
    List<Retweet> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
