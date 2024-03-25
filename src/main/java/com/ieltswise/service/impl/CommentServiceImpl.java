package com.ieltswise.service.impl;

import com.ieltswise.controller.request.StudentCommentRequest;
import com.ieltswise.entity.StudentComment;
import com.ieltswise.entity.UserLessonData;
import com.ieltswise.mapper.StudentCommentMapper;
import com.ieltswise.repository.StudentCommentRepository;
import com.ieltswise.repository.UserLessonDataRepository;
import com.ieltswise.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    private final UserLessonDataRepository userLessonDataRepository;
    private final StudentCommentRepository commentRepository;
    private final StudentCommentMapper mapper;

    @Autowired
    public CommentServiceImpl(UserLessonDataRepository userLessonDataRepository,
                              StudentCommentRepository commentRepository,
                              StudentCommentMapper mapper) {

        this.userLessonDataRepository = userLessonDataRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    public StudentComment createComment(StudentCommentRequest studentCommentRequest) {
        String email = studentCommentRequest.getEmail();
        UserLessonData lessonData = userLessonDataRepository.findByEmail(email);
        System.out.println(email + " email is trying to add a comment, here is the user data: " + lessonData);

        if (lessonData != null && lessonData.getAllPaidLessons() > 0) {
            StudentComment comment = mapper.mapToStudentComment(studentCommentRequest);
            if (lessonData.getName() == null) {
                comment.setName("User" + lessonData.getUserId());
            } else {
                comment.setName(lessonData.getName());
            }
            comment.setCreated(LocalDateTime.now());

            System.out.println("The comment is ready to be saved:" + comment);
            return commentRepository.save(comment);

        } else return null;
    }
}
