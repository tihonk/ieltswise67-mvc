package com.ieltswise.service;

import com.paypal.base.rest.AccessToken;
import com.paypal.base.rest.PayPalRESTException;

public interface PaymentTokenService {

    /**
     * Obtaining an access token based on email.
     *
     * @param email user's email address
     * @return Access Token
     */
    AccessToken getAccessToken(String email);

    /**
     * Updating an email-based access token
     *
     * @param email user's email address
     * @throws PayPalRESTException an exception that occurs when there are problems with updating the access token
     */

    void updateAccessToken(String email) throws PayPalRESTException;
}
