package com.workintech.twitterapi.dto.user;

public record CreateUserRequest(
        String email,
        String userName,
        String password
) {
}
