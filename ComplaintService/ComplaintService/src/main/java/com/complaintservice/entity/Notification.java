package com.complaintservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private Long complaintId;

    private String workerEmail;

    @Enumerated(EnumType.STRING)
    private Status status; // PENDING, ACCEPTED, REJECTED

    private Boolean isRead = false;
    private Boolean isDeleted = false;

    private LocalDateTime createdAt;
    private LocalDateTime actionTakenAt;


    private String proofImageUrl;
    private String qrImageUrl;
    private String upiId;
    private Boolean paymentDone = false;
    private Double amount;



    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    private Integer wardNo;


    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
