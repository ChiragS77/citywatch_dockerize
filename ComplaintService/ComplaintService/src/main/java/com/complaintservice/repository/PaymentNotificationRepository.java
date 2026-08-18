package com.complaintservice.repository;

import com.complaintservice.entity.PaymentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentNotificationRepository extends JpaRepository<PaymentNotification, Long> {
}
