package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.comment.CommentResponse;
import com.workintech.twitterapi.dto.comment.CreateCommentRequest;
import com.workintech.twitterapi.entity.Comment;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.BadRequestException;
import com.workintech.twitterapi.exception.ResourceNotFoundException;
import com.workintech.twitterapi.repository.ICommentRepository;
import com.workintech.twitterapi.repository.ITweetRepository;
import com.workintech.twitterapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final ICommentRepository commentRepository;
    private final IUserRepository userRepository;
    private final ITweetRepository tweetRepository;

    @Autowired

    public CommentService(ICommentRepository commentRepository,IUserRepository userRepository,ITweetRepository tweetRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    public List<CommentResponse> findAll(){
        return commentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Comment findById(Long id){
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()){
            return comment.get();
        }
        throw new ResourceNotFoundException("Comment not found");
    }

    public CommentResponse createComment(CreateCommentRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Tweet tweet = tweetRepository.findById(request.tweetId())
                        .orElseThrow(()-> new ResourceNotFoundException("Tweet not found"));

        if(request.content().isBlank() || request.content() == null){
            throw new BadRequestException("Comment data is empty");
        }

        Comment comment = new Comment();
        comment.setContent(request.content());
        comment.setUser(user);
        comment.setTweet(tweet);

        return toResponse(commentRepository.save(comment));
    }

    public CommentResponse update(Long commentId,String content){
        Comment comment = findById(commentId);

        if(comment.getContent().isBlank() || comment.getContent() == null){
            throw new BadRequestException("Comment data is empty");
        }

        comment.setContent(content);

        return toResponse(commentRepository.save(comment));
    }

    public CommentResponse delete(Long commentId){
        Comment comment = findById(commentId);
        commentRepository.delete(comment);
        return toResponse(comment);
    }

    private CommentResponse toResponse(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getTweet() != null ? comment.getTweet().getId() : null
        );
    }

}
