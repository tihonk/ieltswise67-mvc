package com.ieltswise.mapper;

import com.ieltswise.dto.StudentCommentDto;
import com.ieltswise.entity.StudentComment;
import org.mapstruct.Mapper;

@Mapper
public interface StudentCommentMapper {
    StudentCommentDto mapToStudentCommentDto(StudentComment studentComment);
    StudentComment mapToStudentComment(StudentCommentDto studentCommentDto);
}
