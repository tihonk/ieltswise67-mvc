package com.ieltswise.service;

import com.ieltswise.controller.request.StudentCommentRequest;
import com.ieltswise.entity.StudentComment;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.exception.NoPurchasedLessonsException;

import java.util.List;

public interface CommentService {

    /**
     * Gets a list of all comments from students
     *
     * @return list of comments from students
     */
    List<StudentComment> getAllComments();

    /**
     * Creates a comment based on the transmitted text {@link StudentCommentRequest}
     *
     * @param studentCommentRequest an object with comment data
     * @return the created comment, or null if the user with the specified email does not exist
     * * or an error occurred while saving the comment
     * @throws EmailNotFoundException      if the user with the specified email addresses is not registered
     * @throws NoPurchasedLessonsException if the user has not purchased any lessons
     */
    StudentComment createComment(StudentCommentRequest studentCommentRequest)
            throws EmailNotFoundException, NoPurchasedLessonsException;
}
