package com.ieltswise.mapper.impl;

import com.ieltswise.controller.request.StudentCommentRequest;
import com.ieltswise.entity.StudentComment;
import com.ieltswise.mapper.StudentCommentMapper;
import org.springframework.stereotype.Component;

@Component
public class StudentCommentMapperImpl implements StudentCommentMapper {

    @Override
    public StudentComment mapToStudentComment(StudentCommentRequest studentCommentRequest) {
        if (studentCommentRequest == null) {
            return null;
        } else {
            StudentComment studentComment = new StudentComment();
            studentComment.setEmail(studentCommentRequest.getEmail());
            studentComment.setValue(studentCommentRequest.getValue());
            return studentComment;
        }
    }
}
