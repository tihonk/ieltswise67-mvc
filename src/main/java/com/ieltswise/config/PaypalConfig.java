package com.ieltswise.config;

import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.repository.PaymentCredentialsRepository;
import com.paypal.base.rest.AccessToken;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfig {
    @Value("${paypal.mode}")
    private String mode;
    private final PaymentCredentialsRepository paymentCredentialsRepository;

    public PaypalConfig(PaymentCredentialsRepository paymentCredentialsRepository) {
        this.paymentCredentialsRepository = paymentCredentialsRepository;
    }

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
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
                paymentCredentials.getClientSecret(), paypalSdkConfig());

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
            throw new NullPointerException(String.format("Payment credentials not found for tutorEmail: %s", email));
        return credentials;
    }
}
