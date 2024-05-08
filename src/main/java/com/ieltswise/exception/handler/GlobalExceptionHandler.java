package com.ieltswise.exception.handler;

import com.ieltswise.controller.response.ErrorMessage;
import com.ieltswise.exception.BookingSessionException;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.exception.EventFetchingException;
import com.ieltswise.exception.NoAvailableLessonsException;
import com.ieltswise.exception.NoPurchasedLessonsException;
import com.ieltswise.exception.TutorCreationException;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.ieltswise.enums.ApplicationErrorCodes.BOOKING_SESSION_ERROR;
import static com.ieltswise.enums.ApplicationErrorCodes.EMAIL_NOT_FOUND_ERROR;
import static com.ieltswise.enums.ApplicationErrorCodes.EVENT_FETCHING_ERROR;
import static com.ieltswise.enums.ApplicationErrorCodes.FATAL_ERROR;
import static com.ieltswise.enums.ApplicationErrorCodes.ILLEGAL_ARGUMENT_ERROR;
import static com.ieltswise.enums.ApplicationErrorCodes.METHOD_ARGUMENT_NOT_VALID_ERROR;
import static com.ieltswise.enums.ApplicationErrorCodes.NO_AVAILABLE_LESSONS_ERROR;
import static com.ieltswise.enums.ApplicationErrorCodes.NO_PURCHASED_LESSONS_ERROR;
import static com.ieltswise.enums.ApplicationErrorCodes.PAYPAL_ERROR;
import static com.ieltswise.enums.ApplicationErrorCodes.TUTOR_CREATION_ERROR;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static void logError(Exception e) {
        String exceptionId = UUID.randomUUID().toString();
        log.error(String.format("%s : %s", exceptionId, e.getMessage()), e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleOthersException(Exception e) {
        logError(e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        FATAL_ERROR.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEmailNotFoundException(EmailNotFoundException e) {
        logError(e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        EMAIL_NOT_FOUND_ERROR.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingSessionException.class)
    public ResponseEntity<ErrorMessage> handleBookingSessionException(BookingSessionException e) {
        logError(e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        BOOKING_SESSION_ERROR.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PayPalRESTException.class)
    public ResponseEntity<ErrorMessage> handlePayPalRESTException(PayPalRESTException e) {
        logError(e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        PAYPAL_ERROR.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException e) {
        logError(e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        ILLEGAL_ARGUMENT_ERROR.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAvailableLessonsException.class)
    public ResponseEntity<ErrorMessage> handleNoAvailableLessonsException(NoAvailableLessonsException e) {
        logError(e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        NO_AVAILABLE_LESSONS_ERROR.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoPurchasedLessonsException.class)
    public ResponseEntity<ErrorMessage> handleNoPurchasedLessonsException(NoPurchasedLessonsException e) {
        logError(e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        NO_PURCHASED_LESSONS_ERROR.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        logError(e);
        Set<String> errors = new HashSet<>();
        BindingResult bindingResult = e.getBindingResult();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.add(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
        }
        String msg = String.join("; ", errors);

        return new ResponseEntity<>(
                new ErrorMessage(
                        METHOD_ARGUMENT_NOT_VALID_ERROR.getCodeId(),
                        msg
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventFetchingException.class)
    public ResponseEntity<ErrorMessage> handleEventFetchingException(EventFetchingException e) {
        logError(e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        EVENT_FETCHING_ERROR.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TutorCreationException.class)
    public ResponseEntity<ErrorMessage> handleTutorCreationException(TutorCreationException e) {
        logError(e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        TUTOR_CREATION_ERROR.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
