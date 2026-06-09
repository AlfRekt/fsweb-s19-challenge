package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.comment.CommentResponse;
import com.workintech.twitterapi.dto.comment.CreateCommentRequest;
import com.workintech.twitterapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/")
    public List<CommentResponse> getAll(){
        return commentService.findAll();
    }

    @PostMapping("/")
    public CommentResponse createComment(@RequestBody CreateCommentRequest request){
        return commentService.createComment(request);
    }

    @PutMapping("/{id}")
    public CommentResponse updateComment(@PathVariable Long id, @RequestBody CreateCommentRequest request){
        return commentService.update(id,request.content());
    }

    @DeleteMapping("/{id}")
    public CommentResponse delete(@PathVariable Long id){
        return commentService.delete(id);
    }
}
