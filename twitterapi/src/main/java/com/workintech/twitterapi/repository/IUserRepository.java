package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User,Long> {
}
