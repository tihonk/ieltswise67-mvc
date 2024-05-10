package com.ieltswise.service.impl;

import com.ieltswise.controller.request.StudentCommentRequest;
import com.ieltswise.entity.StudentComment;
import com.ieltswise.entity.UserLessonData;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.exception.NoPurchasedLessonsException;
import com.ieltswise.mapper.StudentCommentMapper;
import com.ieltswise.repository.StudentCommentRepository;
import com.ieltswise.repository.UserLessonDataRepository;
import com.ieltswise.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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

    @Override
    public List<StudentComment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public StudentComment createComment(StudentCommentRequest studentCommentRequest)
            throws EmailNotFoundException, NoPurchasedLessonsException {

        String email = studentCommentRequest.getEmail();
        log.info("{} is trying to add a comment", email);

        UserLessonData lessonData = userLessonDataRepository.findByEmail(email);
        log.info("User data for email {}: {}", email, lessonData);

        if (lessonData == null)
            throw new EmailNotFoundException("Student", email);

        if (lessonData.getAllPaidLessons() < 1)
            throw new NoPurchasedLessonsException("The user did not purchase any lessons");

        StudentComment comment = mapper.mapToStudentComment(studentCommentRequest);
        if (lessonData.getName() == null) {
            comment.setName("User" + lessonData.getUserId());
        } else {
            comment.setName(lessonData.getName());
        }
        comment.setCreated(LocalDateTime.now());
        log.info("Comment ready to be saved: {}", comment);
        return commentRepository.save(comment);
    }
}
