package com.ieltswise.service;

import com.ieltswise.dto.StudentCommentDto;
import com.ieltswise.entity.StudentComment;

public interface CommentService {
    /**
     * Creates a comment based on the transmitted text {@link StudentCommentDto}
     *
     * @param studentCommentDto an object with comment data
     * @return the created comment, or null if the user with the specified email does not exist
     * * or an error occurred while saving the comment
     */
    StudentComment createComment(StudentCommentDto studentCommentDto);
}
