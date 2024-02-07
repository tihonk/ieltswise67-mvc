package com.ieltswise.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PayPalPaymentService {

    /**
     * Creates a payment for the specified number of lessons
     *
     * @param quantity   the number of lessons that will be paid for
     * @param cancelUrl  URL for redirection in case of payment cancellation
     * @param successUrl URL for redirection after successful completion of the payment
     * @param tutorEmail   the email address of the teacher registered in PayPal to which the money will be sent
     * @param studentEmail the email address of the user making the payment
     * @return created payment
     * @throws PayPalRESTException if an error occurs when creating a payment
     */
    Payment createPayment(int quantity, String cancelUrl, String successUrl, String tutorEmail, String studentEmail)
            throws PayPalRESTException;

    /**
     * Performs a PayPal payment using the transmitted payment and payer IDs.
     * Updates the number of available lessons for the user based on the completed payment.
     *
     * @param paymentId PayPal payment ID
     * @param payerId   PayPal Payer ID
     * @param tutorEmail the email address of the teacher registered in PayPal to which the money will be sent
     * @return completed payment
     * @throws PayPalRESTException if an error occurs when making a payment
     */
    Payment executePayment(String paymentId, String payerId, String tutorEmail)
            throws PayPalRESTException;

    /**
     * Prepare PayPal payment link
     *
     * @param successUrl   redirection url on success
     * @param cancelUrl    redirection url on cancel
     * @param tutorEmail   tutor e-mail
     * @param studentEmail student e-mail
     * @return payment link
     */
    String preparePaymentLink(String successUrl, String cancelUrl, String tutorEmail, String studentEmail);
}
