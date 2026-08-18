package com.complaintservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_notifications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentNotification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workerEmail;

    private Double amount;

    private String proofImageUrl;

    private boolean paymentDone = false;
}
