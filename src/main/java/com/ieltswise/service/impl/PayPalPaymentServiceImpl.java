package com.ieltswise.service.impl;

import com.ieltswise.entity.UserLessonData;
import com.ieltswise.repository.UserLessonDataRepository;
import com.ieltswise.service.PayPalPaymentService;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalPaymentServiceImpl implements PayPalPaymentService {

    private final APIContext apiContext;
    private final UserLessonDataRepository userLessonDataRepository;

    @Autowired
    public PayPalPaymentServiceImpl(APIContext apiContext, UserLessonDataRepository userLessonDataRepository) {
        this.apiContext = apiContext;
        this.userLessonDataRepository = userLessonDataRepository;
    }

    public Payment createPayment(int quantity, String email, String cancelUrl, String successUrl) throws PayPalRESTException {

        double lessonPrice = 2.0;
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

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);
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
