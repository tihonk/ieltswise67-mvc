package com.ieltswise.controller;

import com.ieltswise.controller.request.StudentCommentRequest;
import com.ieltswise.entity.StudentComment;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.exception.NoPurchasedLessonsException;
import com.ieltswise.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    ResponseEntity<List<StudentComment>> getAllComments() {
        List<StudentComment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @CrossOrigin(origins = "*")
    @PostMapping()
    ResponseEntity<StudentComment> createComment(@RequestBody @Valid StudentCommentRequest studentCommentRequest)
            throws EmailNotFoundException, NoPurchasedLessonsException {
        StudentComment comment = commentService.createComment(studentCommentRequest);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }
}
