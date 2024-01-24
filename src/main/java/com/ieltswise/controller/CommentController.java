package com.ieltswise.controller;

import com.ieltswise.dto.StudentCommentDto;
import com.ieltswise.entity.StudentComment;
import com.ieltswise.repository.StudentCommentRepository;
import com.ieltswise.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final StudentCommentRepository commentRepository;

    @Autowired
    public CommentController(CommentService commentService, StudentCommentRepository commentRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
    }

    @GetMapping
    ResponseEntity<List<StudentComment>> getAllComments() {
        List<StudentComment> comments = commentRepository.findAll();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<?> createComment(@RequestBody StudentCommentDto commentDto) {
        try {
            StudentComment comment = commentService.createComment(commentDto);
            if (comment != null)
                return new ResponseEntity<>(comment, HttpStatus.CREATED);
            else
                return new ResponseEntity<>("User has not purchased any lessons", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to save comment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
