package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.user.UpdateUserRequest;
import com.workintech.twitterapi.dto.user.UserResponse;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.exception.ResourceNotFoundException;
import com.workintech.twitterapi.exception.UnauthorizedActionException;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final IUserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository userRepository, CurrentUserService currentUserService,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.passwordEncoder = passwordEncoder;
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

    public UserResponse update(Long id, UpdateUserRequest request){
        User user = findById(id);
        authorizeSelfOrAdmin(user);

        validate(request.email(),request.userName(),request.password());
        String encodedPassword = passwordEncoder.encode(request.password());

        if(userRepository.findUserByUsername(request.userName()).isPresent() && !user.getUsername().equals(request.userName())){
            throw new BadRequestException("Username is already taken");
        }

        user.setEmail(request.email());
        user.setUserName(request.userName());
        user.setPassword(encodedPassword);

        return toResponse(userRepository.save(user));
    }

    public UserResponse delete(Long id){
        User user = findById(id);
        authorizeSelfOrAdmin(user);

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
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
    }

    private void authorizeSelfOrAdmin(User target) {
        User currentUser = currentUserService.getCurrentUser();

        boolean isOwner = target.getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedActionException("You can only modify your own account");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Given username is not exist."));
    }
}
