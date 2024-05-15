package com.ieltswise.controller;

import com.ieltswise.controller.request.PaymentCredentialsRequest;
import com.ieltswise.controller.request.ScheduleUpdateRequest;
import com.ieltswise.controller.request.TutorCreateRequest;
import com.ieltswise.controller.response.ErrorMessage;
import com.ieltswise.controller.response.Event;
import com.ieltswise.dto.FreeAndBusyHoursOfTheDay;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.entity.Schedule;
import com.ieltswise.entity.TutorInfo;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.exception.EventFetchingException;
import com.ieltswise.exception.TutorCreationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "tutor controller")
public interface TutorController {

    @Operation(
            summary = "Get future scheduled events",
            description = "Returns all future scheduled events for the selected teacher",
            parameters = @Parameter(
                    name = "tutorId",
                    in = ParameterIn.PATH,
                    required = true,
                    description = "Tutor email, with pre-open access to the calendar",
                    schema = @Schema(
                            type = "string",
                            format = "email",
                            example = "test.tutor1.ieltswise67@gmail.com"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Returns all future scheduled events for the selected teacher",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Event.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = """
                                    The tutor with the specified email address is not registered or an exception\s
                                    occurred when receiving events from the tutor's calendar""",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    ResponseEntity<List<Event>> getEvents(String tutorId) throws EmailNotFoundException, EventFetchingException;

    @Operation(
            summary = "Get available time with a tutor for a specific month",
            description = """
                    This endpoint retrieves a list of available time slots with a tutor for the specified month.
                    The available time slots represent the hours during which the tutor is free to conduct sessions.
                    The response includes information about the availability of the tutor for each day of the\s
                    requested month, with each day segmented into hourly intervals.""",
            parameters = {
                    @Parameter(
                            name = "tutorId",
                            in = ParameterIn.PATH,
                            required = true,
                            description = "Tutor email, with pre-open access to the calendar",
                            schema = @Schema(
                                    type = "string",
                                    format = "email",
                                    example = "test.tutor1.ieltswise67@gmail.com"
                            )
                    ),
                    @Parameter(
                            name = "year",
                            in = ParameterIn.PATH,
                            required = true,
                            description = "Year",
                            schema = @Schema(
                                    type = "integer",
                                    example = "2024"
                            )
                    ),
                    @Parameter(
                            name = "month",
                            in = ParameterIn.PATH,
                            required = true,
                            description = "Number from 1 to 12",
                            schema = @Schema(
                                    type = "integer",
                                    example = "8"
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = """
                                    Returns a list of available time with a tutor for the requested month with an\s
                                    interval of 1 hour""",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation =
                                            FreeAndBusyHoursOfTheDay.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = """
                                    The tutor with the specified email address is not registered or an exception occurred when\s
                                    receiving events from the tutor's calendar""",
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
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    ResponseEntity<List<FreeAndBusyHoursOfTheDay>> getEventsByYearAndMonth(String tutorId, int year, int month)
            throws EmailNotFoundException, EventFetchingException;

    @Operation(
            summary = "Create a tutor",
            description = """
                    Using this endpoint, a new teacher is created in the system based on information from the object\s
                    TutorCreateRequest""",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "The tutor has been successfully created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TutorInfo.class)
                            )
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
                            responseCode = "500",
                            description = "Failed to create tutor",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<TutorInfo> createTutor(@Valid TutorCreateRequest tutorCreateRequest) throws TutorCreationException;

    @Operation(
            summary = "Get a tutor's schedule",
            description = "The endpoint for getting the tutor's schedule at his email address",
            parameters = @Parameter(
                    name = "tutorId",
                    in = ParameterIn.PATH,
                    required = true,
                    description = "Tutor email",
                    schema = @Schema(
                            type = "string",
                            format = "email",
                            example = "test.tutor1.ieltswise67@gmail.com"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = """
                                    Returns the tutor's schedule as a list of days of the week, indicating the hours\s
                                    and their employment status""",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Schedule.class)
                            )
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
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<Schedule> schedule(String tutorId) throws EmailNotFoundException;

    @Operation(
            summary = "Update the tutor's schedule",
            description = "The endpoint for updating the schedule for the tutor",
            parameters = @Parameter(
                    name = "tutorId",
                    in = ParameterIn.PATH,
                    required = true,
                    description = "Tutor email",
                    schema = @Schema(
                            type = "string",
                            format = "email",
                            example = "test.tutor1.ieltswise67@gmail.com"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The tutor's schedule has been updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Schedule.class)
                            )
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
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<Schedule> updateSchedule(String tutorId, @Valid ScheduleUpdateRequest scheduleUpdateRequest)
            throws EmailNotFoundException;

    @Operation(
            summary = "Update payment information",
            description = "The endpoint for updating information about the teacher's payment details",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The payment information has been successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentCredentials.class)
                            )
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
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    ResponseEntity<PaymentCredentials> updatePaymentInformation(
            @Valid PaymentCredentialsRequest paymentCredentialsRequest) throws EmailNotFoundException;
}
