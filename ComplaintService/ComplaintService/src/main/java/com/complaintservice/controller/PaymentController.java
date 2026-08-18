package com.complaintservice.controller;

import com.complaintservice.dto.PaymentVerificationRequest;
import com.complaintservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody PaymentVerificationRequest request) {

        boolean verified = paymentService.verifyPayment(request);

        if (verified) {
            return ResponseEntity.ok("Payment verified successfully");
        }

        return ResponseEntity.badRequest().body("Payment verification failed");
    }
}
