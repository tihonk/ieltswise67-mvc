package com.ieltswise.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PayPalPaymentService {

    /**
     * Creates a payment for the specified number of lessons
     *
     * @param quantity   the number of lessons that will be paid for
     * @param email      the email address of the user making the payment
     * @param cancelUrl  URL for redirection in case of payment cancellation
     * @param successUrl URL for redirection after successful completion of the payment
     * @return created payment
     * @throws PayPalRESTException if an error occurs when creating a payment
     */
    Payment createPayment(int quantity, String email, String cancelUrl, String successUrl)
            throws PayPalRESTException;

    /**
     * Performs a PayPal payment using the transmitted payment and payer IDs.
     * Updates the number of available lessons for the user based on the completed payment.
     *
     * @param paymentId PayPal payment ID
     * @param payerId   PayPal Payer ID
     * @return completed payment
     * @throws PayPalRESTException if an error occurs when making a payment
     */
    Payment executePayment(String paymentId, String payerId)
            throws PayPalRESTException;

    /**
     * Prepare PayPal payment link
     *
     * @param successUrl   redirection url on success
     * @param cancelUrl    redirection url on cancel
     * @param studentEmail student e-mail
     * @return payment link
     */
    String preparePaymentLink(String successUrl, String cancelUrl, String studentEmail);
}
