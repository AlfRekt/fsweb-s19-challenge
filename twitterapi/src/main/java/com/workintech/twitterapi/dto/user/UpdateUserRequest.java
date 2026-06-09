package com.workintech.twitterapi.dto.user;

public record UpdateUserRequest(
        String email,
        String userName,
        String password
) {}