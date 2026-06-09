package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.user.CreateUserRequest;
import com.workintech.twitterapi.dto.user.UpdateUserRequest;
import com.workintech.twitterapi.dto.user.UserResponse;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.exception.ResourceNotFoundException;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> findAll(){
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public User findById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        throw new ResourceNotFoundException("User not found!");
    }

    public UserResponse save(CreateUserRequest request){
        validate(request.email(),request.userName(),request.password());

        User user = new User();
        user.setEmail(request.email());
        user.setUserName(request.userName());
        user.setPassword(request.password());

        return toResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UpdateUserRequest request){
        User user = findById(id);
        validate(request.email(),request.userName(),request.password());
        user.setEmail(request.email());
        user.setUserName(request.userName());
        user.setPassword(request.password());

        return toResponse(userRepository.save(user));
    }

    public UserResponse delete(Long id){
        User user = findById(id);
        userRepository.delete(user);
        return toResponse(user);
    }

    private void validate(String email, String userName, String password) {
        if (email == null || email.isBlank()
                || userName == null || userName.isBlank()
                || password == null || password.isBlank()) {
            throw new BadRequestException("Invalid User data!");
        }
    }

    private UserResponse toResponse(User user){
        return new UserResponse(user.getId(), user.getEmail(), user.getUserName());
    }
}
