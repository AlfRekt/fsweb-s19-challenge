package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFollowRepository extends JpaRepository<Follow,Long> {
}
