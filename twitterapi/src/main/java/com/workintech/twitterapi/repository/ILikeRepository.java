package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ILikeRepository extends JpaRepository<Like,Long> {
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);
}
