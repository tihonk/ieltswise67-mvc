package com.ieltswise.service.impl;

import com.ieltswise.dto.StudentCommentDto;
import com.ieltswise.entity.StudentComment;
import com.ieltswise.mapper.StudentCommentMapper;
import com.ieltswise.repository.StudentCommentRepository;
import com.ieltswise.repository.UserLessonDataRepository;
import com.ieltswise.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public StudentComment createComment(StudentCommentDto studentCommentDto) {

        if (Optional.ofNullable(userLessonDataRepository.findByEmail(studentCommentDto.getStudentEmail())).isPresent()) {

            StudentComment comment = mapper.mapToStudentComment(studentCommentDto);
            comment.setCreated(LocalDateTime.now());
            try {
                return commentRepository.save(comment);
            } catch (Exception e) {
                throw new RuntimeException("Failed to save comment", e);
            }
        } else return null;
    }
}
