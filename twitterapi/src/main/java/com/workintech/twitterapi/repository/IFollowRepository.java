package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IFollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    Optional<Follow> findByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    List<Follow> findByFollower_Id(Long followerId);
    List<Follow> findByFollowing_Id(Long followingId);
}
