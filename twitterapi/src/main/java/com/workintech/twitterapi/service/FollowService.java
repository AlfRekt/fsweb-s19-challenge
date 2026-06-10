package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.follow.CreateFollowRequest;
import com.workintech.twitterapi.dto.follow.FollowResponse;
import com.workintech.twitterapi.dto.user.UserResponse;
import com.workintech.twitterapi.entity.Follow;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.exception.ResourceNotFoundException;
import com.workintech.twitterapi.repository.IFollowRepository;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    private final IFollowRepository followRepository;
    private final IUserRepository userRepository;
    private final CurrentUserService currentUserService;

    public FollowService(IFollowRepository followRepository,
                         IUserRepository userRepository,
                         CurrentUserService currentUserService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public FollowResponse follow(CreateFollowRequest request) {
        User follower = currentUserService.getCurrentUser();
        User following = userRepository.findById(request.followingId())
                .orElseThrow(() -> new ResourceNotFoundException("User to follow not found"));

        if (follower.getId().equals(following.getId())) {
            throw new BadRequestException("You cannot follow yourself");
        }
        if (followRepository.existsByFollower_IdAndFollowing_Id(follower.getId(), following.getId())) {
            throw new BadRequestException("You already follow this user");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return toResponse(followRepository.save(follow));
    }

    public FollowResponse unfollow(Long followingId) {
        User follower = currentUserService.getCurrentUser();
        Follow follow = followRepository
                .findByFollower_IdAndFollowing_Id(follower.getId(), followingId)
                .orElseThrow(() -> new ResourceNotFoundException("You do not follow this user"));
        followRepository.delete(follow);
        return toResponse(follow);
    }

    public List<UserResponse> getFollowing(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return followRepository.findByFollower_Id(userId)
                .stream()
                .map(follow -> toUserResponse(follow.getFollowing()))
                .toList();
    }

    public List<UserResponse> getFollowers(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return followRepository.findByFollowing_Id(userId)
                .stream()
                .map(follow -> toUserResponse(follow.getFollower()))
                .toList();
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
    }

    private FollowResponse toResponse(Follow follow) {
        return new FollowResponse(
                follow.getId(),
                follow.getFollower().getId(),
                follow.getFollowing().getId());
    }
}
