package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.user.UserResponse;
import com.workintech.twitterapi.entity.Role;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.repository.IRoleRepository;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {

    private IUserRepository userRepository;
    private IRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(IUserRepository userRepository, IRoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(String username, String email, String password){
        validate(username,email,password);
        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already registered");
        }


        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = new User();
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setAuthorities(roles);
        return toResponse(userRepository.save(user));
    }

    private UserResponse toResponse(User user){
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
    }

    private void validate(String email, String userName, String password) {
        if (email == null || email.isBlank()
                || userName == null || userName.isBlank()
                || password == null || password.isBlank()) {
            throw new BadRequestException("Invalid User data!");
        }
    }
}
