package com.ieltswise.mapper.impl;

import com.ieltswise.dto.StudentCommentDto;
import com.ieltswise.entity.StudentComment;
import com.ieltswise.mapper.StudentCommentMapper;
import org.springframework.stereotype.Component;

@Component
public class StudentCommentMapperImpl implements StudentCommentMapper {
    @Override
    public StudentComment mapToStudentComment(StudentCommentDto studentCommentDto) {
        if (studentCommentDto == null) {
            return null;
        } else {
            StudentComment studentComment = new StudentComment();
            studentComment.setEmail(studentCommentDto.getEmail());
            studentComment.setValue(studentCommentDto.getValue());
            return studentComment;
        }
    }
}
