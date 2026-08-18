package com.complaintservice.service;

import com.complaintservice.dto.PaymentVerificationRequest;
import com.complaintservice.entity.PaymentNotification;
import com.complaintservice.repository.PaymentNotificationRepository;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class PaymentService {

    @Value("${razorpay.key_secret}")
    private String secret;

    @Autowired
    private PaymentNotificationRepository notificationRepo;

    public boolean verifyPayment(PaymentVerificationRequest request) {

        try {

            String payload =
                    request.getRazorpay_order_id() + "|" +
                            request.getRazorpay_payment_id();

            String generatedSignature =
                    hmacSHA256(payload, secret);

            if (generatedSignature.equals(
                    request.getRazorpay_signature())) {

                PaymentNotification notification =
                        notificationRepo.findById(
                                        request.getNotificationId())
                                .orElseThrow();

                notification.setPaymentDone(true);

                notificationRepo.save(notification);

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private String hmacSHA256(String data, String secret)
            throws Exception {

        Mac sha256Hmac =
                Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey =
                new SecretKeySpec(
                        secret.getBytes(),
                        "HmacSHA256");

        sha256Hmac.init(secretKey);

        byte[] hash =
                sha256Hmac.doFinal(data.getBytes());

        return Hex.encodeHexString(hash);
    }
}
