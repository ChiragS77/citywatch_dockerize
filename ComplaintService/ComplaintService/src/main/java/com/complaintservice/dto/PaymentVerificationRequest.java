package com.complaintservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerificationRequest {

    private Long notificationId;
    private String razorpay_order_id;
    private String razorpay_payment_id;
    private String razorpay_signature;
}
