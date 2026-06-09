package com.workintech.twitterapi.dto.user;

public record UserResponse(
        Long id,
        String email,
        String userName
) {}
