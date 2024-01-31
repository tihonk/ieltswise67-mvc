package com.ieltswise.service.impl;

import com.ieltswise.config.PaypalConfig;
import com.ieltswise.entity.UserLessonData;
import com.ieltswise.repository.UserLessonDataRepository;
import com.ieltswise.service.PayPalPaymentService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.AccessToken;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalPaymentServiceImpl implements PayPalPaymentService {

    @Value("${ieltswise67.base.url}")
    private String baseUrl;

    private APIContext context;
    private final PaypalConfig paypalConfig;
    private final UserLessonDataRepository userLessonDataRepository;

    @Autowired
    public PayPalPaymentServiceImpl(PaypalConfig paypalConfig, UserLessonDataRepository userLessonDataRepository) {
        this.paypalConfig = paypalConfig;
        this.userLessonDataRepository = userLessonDataRepository;
    }

    @Override
    public String preparePaymentLink(final String successUrl, final String cancelUrl, final String studentEmail) {
        try {
            final Payment payment = createPayment(1, studentEmail, cancelUrl, successUrl);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return null;
    }

    private APIContext getAPIContext() throws PayPalRESTException {
        AccessToken token = paypalConfig.getAccessToken();

        if (token == null || token.expiresIn() <= 0) {
            paypalConfig.updateAccessToken();
            token = paypalConfig.getAccessToken();
            context = new APIContext(token.getAccessToken());
            context.setConfigurationMap(paypalConfig.paypalSdkConfig());
        }
        return context;
    }

    public Payment createPayment(int quantity, String email, String cancelUrl, String successUrl) throws PayPalRESTException {

        double lessonPrice = 15.0;
        double total = lessonPrice * quantity;

        Amount amount = new Amount();
        amount.setCurrency("USD");
        BigDecimal roundedNumber = new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
        amount.setTotal(String.valueOf(roundedNumber));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(String.format("Lesson quantity: %d, user email: %s", quantity, email));

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(successUrl);
        redirectUrls.setCancelUrl(cancelUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(getAPIContext());
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(getAPIContext(), paymentExecution);
        getQuantityAndEmail(executedPayment);

        return executedPayment;
    }

    private void getQuantityAndEmail(Payment executedPayment) {
        String description = executedPayment.getTransactions().get(0).getDescription();
        String[] parts = description.split(", ");

        int quantity = Integer.parseInt(parts[0].split(": ")[1]);
        String email = parts[1].split(": ")[1];
        updateUserLessonCount(email, quantity);
    }

    private void updateUserLessonCount(String email, int quantity) {
        UserLessonData userLessonData = userLessonDataRepository.findByEmail(email);
        if (userLessonData != null) {
            int availableLessons = userLessonData.getAvailableLessons() + quantity;
            userLessonData.setAvailableLessons(availableLessons);
            userLessonDataRepository.save(userLessonData);
        } else {
            UserLessonData newUserLessonData = new UserLessonData();
            newUserLessonData.setEmail(email);
            newUserLessonData.setAvailableLessons(quantity);
            userLessonDataRepository.save(newUserLessonData);
        }
    }
}
