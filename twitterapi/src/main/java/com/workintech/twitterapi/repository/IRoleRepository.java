package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role,Long> {
    @Query("SELECT r FROM Role r WHERE r.authority = :authority")
    Optional<Role> findByAuthority(String authority);
}
