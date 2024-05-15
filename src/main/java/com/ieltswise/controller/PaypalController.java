package com.ieltswise.controller;

import com.ieltswise.controller.response.ErrorMessage;
import com.ieltswise.exception.EmailNotFoundException;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "paypal controller")
public interface PaypalController {

    @Operation(
            summary = "Get payment link",
            description = "A payment link is being prepared at this endpoint.",
            parameters = {
                    @Parameter(
                            name = "successUrl",
                            in = ParameterIn.QUERY,
                            required = true,
                            description = "Redirect URL in case of successful payment",
                            schema = @Schema(
                                    type = "string",
                                    format = "url",
                                    example = "http://localhost:8080/payment/success"
                            )
                    ),
                    @Parameter(
                            name = "cancelUrl",
                            in = ParameterIn.QUERY,
                            required = true,
                            description = "Redirect URL in case of cancellation payment",
                            schema = @Schema(
                                    type = "string",
                                    format = "url",
                                    example = "http://localhost:8080/payment/cancel"
                            )
                    ),
                    @Parameter(
                            name = "studentEmail",
                            in = ParameterIn.QUERY,
                            required = true,
                            description = "Email of the student the lesson is planned",
                            schema = @Schema(
                                    type = "string",
                                    format = "email",
                                    example = "bestStudent001@gmail.com"
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The payment link was successfully received",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            format = "url",
                                            example = "https://ieltswise.vercel.app/?paymentId=PAYID-MWWSJ4I8N178399" +
                                                    "DR650864Y&token=EC-11Y67263SC580704L&PayerID=L984M7GB226JW"
                                    )
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
                            description = "Failed to create payment link",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = "Failed to create payment link"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> getPaymentLink(String successUrl, String cancelUrl, String studentEmail
//                                        TODO:
//                                        @RequestParam("tutorEmail") String tutorEmail,
    ) throws EmailNotFoundException, PayPalRESTException;

    @Operation(
            summary = "Cancel payment",
            description = "Returns a message about the cancellation of the payment",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment cancellation successful",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = "Cancellation of payment"
                                    )
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
    ResponseEntity<String> cancelPay();
}
