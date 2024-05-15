package com.ieltswise.controller;

import com.ieltswise.controller.request.RegularSessionDataRequest;
import com.ieltswise.controller.request.SessionDataRequest;
import com.ieltswise.controller.response.ErrorMessage;
import com.ieltswise.controller.response.SessionDataResponse;
import com.ieltswise.exception.EmailNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@Tag(name = "student controller")
public interface StudentController {

    @Operation(
            summary = "Book a trial lesson",
            description = "Allows a student to book a trial lesson with a selected tutor for a specified time",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The invitation is sent to the student and the teacher at their email",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SessionDataResponse.class)
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
                            responseCode = "404",
                            description = "The tutor with the specified email address is not registered",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "You have already used the trial lesson",
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
    ResponseEntity<SessionDataResponse> bookTrialSession(@Valid SessionDataRequest sessionDataRequest) throws Exception;

    @Operation(
            summary = "Book a regular lesson",
            description = "Allows the student to sign up for a regular lesson, while payment must be made",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = """
                                    The transaction is completed, the funds are debited, and the lessons are booked""",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SessionDataResponse.class)
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
                            description = "No available lessons were found for this student",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = """
                                    The student or tutor with the specified email address is not registered or the\s
                                    payment has not been approved""",
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
    ResponseEntity<SessionDataResponse> bookRegularSession(@Valid RegularSessionDataRequest regularSessionDataRequest)
            throws Exception;

    @Operation(
            summary = "Get user lesson count",
            description = "Extracts the number of available lessons for the user based on the email address",
            parameters = @Parameter(
                    name = "email",
                    in = ParameterIn.PATH,
                    required = true,
                    description = "Email of the student",
                    schema = @Schema(
                            type = "string",
                            format = "email",
                            example = "bestStudent001@gmail.com"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return the number of user lessons available",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object",
                                            example = "{\"Number of lessons available\": 5}"
                                    )
                            )
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
    ResponseEntity<String> getUserLessonCount(String email) throws EmailNotFoundException;

    @Operation(
            summary = "Check trial lesson availability",
            description = "Determines the availability of a trial lesson for a student",
            parameters = @Parameter(
                    name = "studentEmail",
                    in = ParameterIn.PATH,
                    required = true,
                    description = "Email of the student",
                    schema = @Schema(
                            type = "string",
                            format = "email",
                            example = "bestStudent001@gmail.com"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Returns whether trial lesson is available or not (true or false)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class)
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
    ResponseEntity<Boolean> isTrialAvailable(String studentEmail);
}
