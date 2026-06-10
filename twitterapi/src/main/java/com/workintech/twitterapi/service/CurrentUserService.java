package com.workintech.twitterapi.service;

import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.UnauthorizedActionException;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final IUserRepository userRepository;

    @Autowired
    public CurrentUserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new UnauthorizedActionException("No authorized user found");
        }

        String username = authentication.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(()->new UnauthorizedActionException("Authenticated user not found"));
    }

    public boolean isAdmin(User user) {
        return user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }
}
