package com.ieltswise.controller;

import com.ieltswise.controller.request.StudentCommentRequest;
import com.ieltswise.controller.response.ErrorMessage;
import com.ieltswise.entity.StudentComment;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.exception.NoAvailableLessonsException;
import com.ieltswise.exception.NoPurchasedLessonsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "comment controller")
public interface CommentController {

    @Operation(
            summary = "Get all the student comments",
            description = "The method processes GET requests to get a list of all comments",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Student comments have been successfully uploaded",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = StudentComment.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<List<StudentComment>> getAllComments();

    @Operation(
            summary = "Create a new comment",
            description = "With this endpoint, a user who has bought at least 1 lesson can leave a comment",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "The comment was created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentComment.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data format",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User has not purchased any lessons",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user with the specified email address is not registered",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<StudentComment> createComment(@Valid StudentCommentRequest studentCommentRequest)
            throws EmailNotFoundException, NoAvailableLessonsException, NoPurchasedLessonsException;
}
