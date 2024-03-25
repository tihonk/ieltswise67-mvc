package com.ieltswise.mapper;

import com.ieltswise.controller.request.StudentCommentRequest;
import com.ieltswise.entity.StudentComment;

public interface StudentCommentMapper {

    /**
     * Converts a StudentCommentDto object to a StudentComment object
     * @param studentCommentRequest the DTO object of the student's comment
     * @return a StudentComment object representing a comment from a student
     */
    StudentComment mapToStudentComment(StudentCommentRequest studentCommentRequest);
}
