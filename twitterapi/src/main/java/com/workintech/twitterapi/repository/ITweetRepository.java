package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITweetRepository extends JpaRepository<Tweet,Long> {
    @Query("SELECT t FROM Tweet t WHERE t.user.id = :userId")
    List<Tweet> findByUserId(Long userId);
}
