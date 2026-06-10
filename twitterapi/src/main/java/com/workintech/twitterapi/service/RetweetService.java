package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.retweet.CreateRetweetRequest;
import com.workintech.twitterapi.dto.retweet.RetweetResponse;
import com.workintech.twitterapi.entity.Retweet;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.exception.ResourceNotFoundException;
import com.workintech.twitterapi.exception.UnauthorizedActionException;
import com.workintech.twitterapi.repository.IRetweetRepository;
import com.workintech.twitterapi.repository.ITweetRepository;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetweetService {
    private final IRetweetRepository retweetRepository;
    private final IUserRepository userRepository;
    private final ITweetRepository tweetRepository;

    private final CurrentUserService currentUserService;

    public RetweetService(IRetweetRepository retweetRepository,
                          IUserRepository userRepository,
                          ITweetRepository tweetRepository,
                          CurrentUserService currentUserService) {
        this.retweetRepository = retweetRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
        this.currentUserService = currentUserService;
    }

    public List<RetweetResponse> findAll() {
        return retweetRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RetweetResponse> findByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return retweetRepository.findByUser_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RetweetResponse retweet(CreateRetweetRequest request){
        User user = currentUserService.getCurrentUser();

        Tweet tweet =tweetRepository.findById(request.tweetId())
                .orElseThrow(()-> new ResourceNotFoundException("Tweet not found"));

        if(retweetRepository.existsByUserIdAndTweetId(user.getId(), request.tweetId())){
            throw new BadRequestException("User already retweeted this tweet");
        }

        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet);

        return toResponse(retweetRepository.save(retweet));
    }

    public RetweetResponse delete(Long id){
        Retweet retweet = retweetRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Retweet not found"));

        User currentUser = currentUserService.getCurrentUser();

        if (!retweet.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You can only delete your own retweet");
        }

        retweetRepository.delete(retweet);
        return toResponse(retweet);
    }

    private RetweetResponse toResponse(Retweet retweet){
        return new RetweetResponse(
                retweet.getId(),
                retweet.getUser().getId() != null ? retweet.getUser().getId() : null,
                retweet.getTweet().getId() != null ? retweet.getTweet().getId() : null
        );
    }
}
