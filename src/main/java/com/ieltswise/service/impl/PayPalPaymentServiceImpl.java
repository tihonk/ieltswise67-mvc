package com.ieltswise.service.impl;

import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.entity.UserLessonData;
import com.ieltswise.repository.PaymentCredentialsRepository;
import com.ieltswise.repository.UserLessonDataRepository;
import com.ieltswise.service.PayPalPaymentService;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
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

    @Value("${paypal.mode}")
    private String mode;
    @Value("${ieltswise67.lesson.price}")
    private double lessonPrice;
    private final UserLessonDataRepository userLessonDataRepository;
    private final PaymentCredentialsRepository paymentCredentialsRepository;

    @Autowired
    public PayPalPaymentServiceImpl(UserLessonDataRepository userLessonDataRepository,
                                    PaymentCredentialsRepository paymentCredentialsRepository) {
        this.userLessonDataRepository = userLessonDataRepository;
        this.paymentCredentialsRepository = paymentCredentialsRepository;
    }

    @Override
    public String preparePaymentLink(final String successUrl, final String cancelUrl, final String tutorEmail,
                                     final String studentEmail) {
        try {
            final Payment payment = createPayment(1, cancelUrl, successUrl, tutorEmail, studentEmail);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private APIContext getAPIContext(String email) {
        PaymentCredentials credentials = paymentCredentialsRepository.findByTutorEmail(email).orElseThrow(() ->
                new IllegalArgumentException(String.format("Payment credentials not found for tutorEmail: %s", email)));

        return new APIContext(credentials.getClientId(), credentials.getClientSecret(), mode);
    }

    public Payment createPayment(
            int quantity,
            String cancelUrl,
            String successUrl,
            String tutorEmail,
            String studentEmail) throws PayPalRESTException {

        double total = calculateTotalPrice(quantity, studentEmail);

        Amount amount = new Amount();
        amount.setCurrency("USD");
        BigDecimal roundedNumber = new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
        amount.setTotal(String.valueOf(roundedNumber));

        Transaction transaction = new Transaction();
        transaction.setDescription(String.format("Lesson quantity: %d, user email: %s", quantity, studentEmail));
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(successUrl);
        redirectUrls.setCancelUrl(cancelUrl);

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(getAPIContext(tutorEmail));
    }

    private double calculateTotalPrice(int quantity, String email) {
        UserLessonData userLessonData = userLessonDataRepository.findByEmail(email);
        if (userLessonData != null && (userLessonData.getAllPaidLessons() + quantity) % 5 == 0) {
            return applyDiscount(quantity);
        } else {
            return calculateRegularPrice(quantity);
        }
    }

    private double applyDiscount(int quantity) {
        double totalPrice = calculateRegularPrice(quantity);
        double discount = lessonPrice * 5 * 0.05;
        return totalPrice - discount;
    }

    private double calculateRegularPrice(int quantity) {
        return lessonPrice * quantity;
    }

    public Payment executePayment(
            String paymentId,
            String payerId,
            String tutorEmail) throws PayPalRESTException {

        PaymentCredentials credentials = verifyPaymentNotCompleted(paymentId, tutorEmail);

        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(getAPIContext(tutorEmail), paymentExecution);
        getQuantityAndEmail(executedPayment);

        credentials.setPaymentId(paymentId);
        paymentCredentialsRepository.save(credentials);

        return executedPayment;
    }

    private PaymentCredentials verifyPaymentNotCompleted(String paymentId, String tutorEmail) throws PayPalRESTException {
        PaymentCredentials paymentCredentials = paymentCredentialsRepository.findByTutorEmail(tutorEmail).orElseThrow(() ->
                new IllegalArgumentException(String.format("Payment credentials not found for tutorEmail: %s", tutorEmail)));
        if (paymentId.equals(paymentCredentials.getPaymentId())) {
            throw new PayPalRESTException("Payment has been done already for this cart.");
        }
        return paymentCredentials;
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
            int allLessons = userLessonData.getAllPaidLessons() + quantity;
            userLessonData.setAvailableLessons(availableLessons);
            userLessonData.setAllPaidLessons(allLessons);
            userLessonDataRepository.save(userLessonData);
        } else {
            UserLessonData newUserLessonData = new UserLessonData();
            newUserLessonData.setEmail(email);
            newUserLessonData.setUsedTrial(false);
            newUserLessonData.setAvailableLessons(quantity);
            newUserLessonData.setAllPaidLessons(quantity);
            userLessonDataRepository.save(newUserLessonData);
        }
    }
}
