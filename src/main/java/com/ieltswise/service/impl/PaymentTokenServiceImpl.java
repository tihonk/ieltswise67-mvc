package com.ieltswise.service.impl;

import com.ieltswise.config.PaypalConfig;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.repository.PaymentCredentialsRepository;
import com.ieltswise.service.PaymentTokenService;
import com.paypal.base.rest.AccessToken;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentTokenServiceImpl implements PaymentTokenService {
    private final PaymentCredentialsRepository paymentCredentialsRepository;
    private final PaypalConfig paypalConfig;

    @Autowired
    public PaymentTokenServiceImpl(PaymentCredentialsRepository paymentCredentialsRepository, PaypalConfig paypalConfig) {
        this.paymentCredentialsRepository = paymentCredentialsRepository;
        this.paypalConfig = paypalConfig;
    }

    public AccessToken getAccessToken(String email) {
        PaymentCredentials paymentCredentials = getPaymentCredentialsByEmail(email);
        if (paymentCredentials.getAccessToken() == null || paymentCredentials.getExpires() == null)
            return null;

        return new AccessToken(paymentCredentials.getAccessToken(),
                paymentCredentials.getExpires());
    }

    public void updateAccessToken(String email) throws PayPalRESTException {
        PaymentCredentials paymentCredentials = getPaymentCredentialsByEmail(email);

        OAuthTokenCredential tokenCredential = new OAuthTokenCredential(paymentCredentials.getClientId(),
                paymentCredentials.getClientSecret(), paypalConfig.paypalSdkConfig());

        refreshTokenInfo(tokenCredential, paymentCredentials);
    }

    private void refreshTokenInfo(OAuthTokenCredential tokenCredential, PaymentCredentials paymentCredentials)
            throws PayPalRESTException {

        paymentCredentials.setAccessToken(tokenCredential.getAccessToken());
        paymentCredentials.setExpires(tokenCredential.expiresIn());
        try {
            paymentCredentialsRepository.save(paymentCredentials);
        } catch (RuntimeException e) {
            System.err.printf("Error saving payment credentials: %s", e.getMessage());
            throw new PayPalRESTException("Failed to save updated payment credentials", e);
        }
    }

    private PaymentCredentials getPaymentCredentialsByEmail(String email) {
        PaymentCredentials credentials = paymentCredentialsRepository.findByTutorEmail(email);
        if (credentials == null)
            throw new IllegalArgumentException(String.format("Payment credentials not found for tutorEmail: %s", email));
        return credentials;
    }
}
